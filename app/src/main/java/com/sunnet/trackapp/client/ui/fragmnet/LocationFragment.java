package com.sunnet.trackapp.client.ui.fragmnet;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easyandroidanimations.library.FlipVerticalToAnimation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.sunnet.trackapp.client.R;
import com.sunnet.trackapp.client.asynctask.ConcurrentAsyncTask;
import com.sunnet.trackapp.client.db.DatabaseHelper;
import com.sunnet.trackapp.client.db.entity.BaseEntity;
import com.sunnet.trackapp.client.db.entity.LocationEntity;
import com.sunnet.trackapp.client.ui.activity.MainActivity;
import com.sunnet.trackapp.client.ui.adapter.IOnCLick;
import com.sunnet.trackapp.client.ui.adapter.LocationAdapter;
import com.sunnet.trackapp.client.ui.model.MarkerItem;
import com.sunnet.trackapp.client.util.GlobalSingleton;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends BaseFragment implements OnMapReadyCallback,
        SwipeRefreshLayout.OnRefreshListener, ClusterManager.OnClusterItemClickListener<MarkerItem> {
    private FrameLayout toggleMapOrDetail;
    private FrameLayout containerMap;
    private GoogleMap googleMap;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar loading;
    private TextView tvDataNotFound;

    /**
     * Resources
     */
    private List<LocationEntity> locationList = new ArrayList<>();
    private List<LocationEntity> locationListTemp = new ArrayList<>();
    private boolean isShowMap = true;
    private MarkerItem markerItemClicked;
    private ClusterManager<MarkerItem> mClusterManager;
    private LatLngBounds.Builder boundsBuilder;
    private PolygonOptions polygonOptions;
    private LocationAdapter adapter;
    private boolean isFirst = true;
    private boolean isAnim = false;

    @Override
    protected int getContentView() {
        return R.layout.location_layout;
    }

    @Override
    protected void initView() {
        toggleMapOrDetail = (FrameLayout) findViewById(R.id.toggle_map_detail);
        loading = (ProgressBar) findViewById(R.id.loading);
        tvDataNotFound = (TextView) findViewById(R.id.tv_no_data);
        tvDataNotFound.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        containerMap = (FrameLayout) findViewById(R.id.container_map);
        FragmentManager fragManager = getChildFragmentManager();
        SupportMapFragment supportMapFragment = (SupportMapFragment) fragManager.findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        toggleMapOrDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowMap = !isShowMap;
                isAnim = true;
                switchMapOrDetail();
            }
        });
    }

    /**
     * Load data from server
     */
    private void loadDataFromServer(boolean isShowProgress) {
        MainActivity mainActivity = getMainActivity();
        if (mainActivity != null) {
            mainActivity.loadAllDataFromServer(isShowProgress, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg != null && 1 == msg.what) {
                        initData();
                    }
                }
            });
        } else {
            swipeRefreshLayout.setRefreshing(false);
            loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        loadDataFromServer(false);
    }

    /**
     * Preparing map data
     */
    private void prepareMapData() {
        getPolygonOptions(locationListTemp);
    }

    /**
     * Preparing detail list data
     */
    private void prepareDetailAdapter() {
        if (adapter == null) {
            adapter = new LocationAdapter(locationList, new IOnCLick() {
                @Override
                public void onClick(BaseEntity entity) {

                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setAdapter(adapter);
        }
    }

    private void addMarker() {
        mClusterManager.clearItems();
        googleMap.setOnCameraChangeListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(this);
        googleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setRenderer(new MyClusterRenderer(getContext(), googleMap, mClusterManager));

        if (locationList.size() > 0) {
            for (LocationEntity loc : locationList) {
                mClusterManager.addItem(new MarkerItem(loc));
            }
        }
        mClusterManager.cluster();
    }


    private void setUI() {
        locationList.clear();
        locationList.addAll(locationListTemp);
        if (isShowMap) {
            googleMap.addPolygon(polygonOptions);
            addMarker();
            //-- Goto center bound
            changeCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 10));
        } else {
            prepareDetailAdapter();
        }
        switchMapOrDetail();
    }

    /**
     * Preparing data
     */
    private void initData() {
        new ConcurrentAsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!swipeRefreshLayout.isRefreshing())
                    loading.setVisibility(View.VISIBLE);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                List<LocationEntity> list = GlobalSingleton.getInstance().getLocationList();
                if (list == null || list.size() == 0) {
                    list = DatabaseHelper.getAllLocation();
                    if (list != null && list.size() > 0) {
                        for (LocationEntity loc : list) {
                            loc.decrypt();
                        }
                    }
                }
                if (list != null && list.size() > 0) {
                    locationListTemp.clear();
                    locationListTemp.addAll(list);
                    prepareMapData();
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean haveData) {
                if (haveData) {
                    setUI();
                    tvDataNotFound.setVisibility(View.GONE);
                    if (!swipeRefreshLayout.isRefreshing())
                        loading.setVisibility(View.GONE);
                    else
                        swipeRefreshLayout.setRefreshing(false);
                } else if (isFirst) {
                    isFirst = false;
                    loadDataFromServer(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    loading.setVisibility(View.GONE);
                    switchMapOrDetail();
                }
            }
        }.executeConcurrently();
    }

    private void switchMapOrDetail() {
        if (isAnim) {
            if (isShowMap) {
                new FlipVerticalToAnimation(swipeRefreshLayout).setFlipToView(containerMap)
                        .setInterpolator(new LinearInterpolator()).animate();
            } else {
                new FlipVerticalToAnimation(containerMap).setFlipToView(swipeRefreshLayout)
                        .setInterpolator(new LinearInterpolator()).animate();
            }
        }
        if (locationList.size() == 0 && !isShowMap) {
            tvDataNotFound.setVisibility(View.VISIBLE);
        } else {
            tvDataNotFound.setVisibility(View.GONE);
        }
        if (!isAnim) {
            swipeRefreshLayout.setVisibility(isShowMap ? View.GONE : View.VISIBLE);
            containerMap.setVisibility(!isShowMap ? View.GONE : View.VISIBLE);
        }
        if (!isShowMap) {
            prepareDetailAdapter();
        }
        isAnim = false;
    }

    private PolygonOptions getPolygonOptions(List<LocationEntity> locList) {
        polygonOptions = new PolygonOptions();
        polygonOptions.addAll(initPositions(locList));
        polygonOptions.strokeWidth(2);
        polygonOptions.strokeColor(Color.BLUE);
        return polygonOptions;
    }

    private List<LatLng> initPositions(List<LocationEntity> locList) {
        List<LatLng> list = new ArrayList<>();
        for (LocationEntity loc : locList) {
            list.add(new LatLng(Double.parseDouble(loc.getLatitude()), Double.parseDouble(loc.getLongitude())));
        }
        boundsBuilder = new LatLngBounds.Builder();
        for (LatLng lng : list)
            boundsBuilder.include(lng);

        return list;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);

        mClusterManager = new ClusterManager<MarkerItem>(getContext(), this.googleMap);
        initData();
    }

    @Override
    public boolean onClusterItemClick(MarkerItem markerItem) {
        markerItemClicked = markerItem;
        return true;
    }

    /**
     * When the map is not ready the features cannot be used. This should be called on
     * all entry points that call methods on the Google Maps API.
     */
    private boolean checkReady() {
        if (googleMap == null) {
            Toast.makeText(getActivity(), R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void changeCamera(CameraUpdate update) {
        changeCamera(update, null);
    }

    /**
     * Change the camera position by moving or animating the camera depending on the state of the
     * animate toggle button.
     */
    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {
        if (checkReady()) {
            int duration = 300;
            // The duration must be strictly positive so we make it at least 1.
            googleMap.animateCamera(update, Math.max(duration, 1), callback);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (fragment != null)
                getFragmentManager().beginTransaction().remove(fragment).commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /***********************************************/
    // Custom adapter info view :

    class MyClusterRenderer extends DefaultClusterRenderer<MarkerItem> {

        public MyClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<MarkerItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MarkerItem item, MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);

            markerOptions.title(item.getTitle()).snippet(item.getSnippet());
        }

        @Override
        protected void onClusterItemRendered(MarkerItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);

            //here you have access to the marker itself
        }
    }
}
