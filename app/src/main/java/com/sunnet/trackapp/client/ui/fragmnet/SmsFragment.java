package com.sunnet.trackapp.client.ui.fragmnet;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sunnet.trackapp.client.R;
import com.sunnet.trackapp.client.asynctask.ConcurrentAsyncTask;
import com.sunnet.trackapp.client.db.DatabaseHelper;
import com.sunnet.trackapp.client.db.entity.BaseEntity;
import com.sunnet.trackapp.client.db.entity.SMSEntity;
import com.sunnet.trackapp.client.ui.DialogSmsDetail;
import com.sunnet.trackapp.client.ui.activity.MainActivity;
import com.sunnet.trackapp.client.ui.adapter.IOnCLick;
import com.sunnet.trackapp.client.ui.adapter.SmsAdapter;
import com.sunnet.trackapp.client.util.GlobalSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class SmsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar loading;
    private TextView tvDataNotFound;

    /**
     * Resources
     */
    private SmsAdapter adapter;
    private List<SMSEntity> smsList = new ArrayList<>();
    private List<SMSEntity> smsListTemp = new ArrayList<>();
    private boolean isFirst = true;
    private boolean isShowDialog = false;

    @Override
    protected int getContentView() {
        return R.layout.sms_layout;
    }

    @Override
    protected void initView() {
        loading = (ProgressBar) findViewById(R.id.loading);
        tvDataNotFound = (TextView) findViewById(R.id.tv_no_data);
        tvDataNotFound.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        initData();
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
     * Preparing detail list data
     */
    private void prepareDetailAdapter() {
        if (adapter == null) {
            adapter = new SmsAdapter(smsList, new IOnCLick() {
                @Override
                public void onClick(BaseEntity entity) {
                    //-- Open Detail a SMS
                    if (!isShowDialog) {
                        isShowDialog = true;
                        DialogSmsDetail dialogSmsDetail = new DialogSmsDetail(getActivity(), (SMSEntity) entity,
                                new DialogSmsDetail.IDialog() {
                                    @Override
                                    public void close() {
                                        isShowDialog = false;
                                    }
                                });
                        dialogSmsDetail.show();
                    }
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }
    }

    private void setUI() {
        smsList.clear();
        smsList.addAll(smsListTemp);
        prepareDetailAdapter();
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
                List<SMSEntity> list = GlobalSingleton.getInstance().getSmsList();
                if (list == null || list.size() == 0) {
                    list = DatabaseHelper.getAllSms();
                    if (list != null && list.size() > 0) {
                        for (SMSEntity sms : list) {
                            sms.decrypt();
                        }
                    }
                }
                if (list != null && list.size() > 0) {
                    smsListTemp.clear();
                    smsListTemp.addAll(list);
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
                    if (smsList.size() == 0) {
                        tvDataNotFound.setVisibility(View.VISIBLE);
                    } else {
                        tvDataNotFound.setVisibility(View.GONE);
                    }
                }
            }
        }.executeConcurrently();
    }
}
