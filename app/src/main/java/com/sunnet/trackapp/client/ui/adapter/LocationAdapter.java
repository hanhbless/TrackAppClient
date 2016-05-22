package com.sunnet.trackapp.client.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunnet.trackapp.client.R;
import com.sunnet.trackapp.client.db.entity.LocationEntity;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {
    private static IOnCLick iOnCLick;
    private List<LocationEntity> locationList;

    public LocationAdapter(List<LocationEntity> locationList, IOnCLick iOnCLick) {
        this.locationList = locationList;
        this.iOnCLick = iOnCLick;
    }

    @Override
    public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_location_item, parent, false);
        return new LocationHolder(v);
    }

    @Override
    public void onBindViewHolder(LocationHolder holder, int position) {
        holder.setUi(locationList.get(position));
    }

    @Override
    public int getItemCount() {
        return locationList != null ? locationList.size() : 0;
    }

    static class LocationHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvTime, tvHeader, tvAddress;
        View.OnClickListener onClickListener;

        public LocationHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvTime = (TextView) itemView.findViewById(R.id.tv3);
            tvHeader = (TextView) itemView.findViewById(R.id.tv1);
            tvAddress = (TextView) itemView.findViewById(R.id.tv2);
        }

        public void setUi(final LocationEntity entity) {
            tvTime.setText(entity.getDate().substring(0, entity.getDate().length() - 3));
            tvHeader.setText(entity.getPlace());
            tvAddress.setText(entity.getAddress());
            /*if (onClickListener == null) {
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iOnCLick.onClick(entity);
                    }
                };
                itemView.setOnClickListener(onClickListener);
            }*/
        }
    }
}
