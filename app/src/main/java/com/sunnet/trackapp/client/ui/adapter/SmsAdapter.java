package com.sunnet.trackapp.client.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunnet.trackapp.client.R;
import com.sunnet.trackapp.client.db.entity.SMSEntity;

import java.util.List;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsHolder> {
    private static IOnCLick iOnCLick;
    private static List<SMSEntity> smsList;

    public SmsAdapter(List<SMSEntity> smsList, IOnCLick iOnCLick) {
        this.smsList = smsList;
        this.iOnCLick = iOnCLick;
    }

    @Override
    public SmsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sms_item, parent, false);
        return new SmsHolder(v);
    }

    @Override
    public void onBindViewHolder(SmsHolder holder, int position) {
        holder.setUi(smsList.get(position));
    }

    @Override
    public int getItemCount() {
        return smsList != null ? smsList.size() : 0;
    }

    static class SmsHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvNameOrPhone, tvMessage, tvTime;
        View.OnClickListener onClickListener;

        public SmsHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvNameOrPhone = (TextView) itemView.findViewById(R.id.tv1);
            tvMessage = (TextView) itemView.findViewById(R.id.tv2);
            tvTime = (TextView) itemView.findViewById(R.id.tv3);
        }

        public void setUi(final SMSEntity entity) {
            tvNameOrPhone.setText(entity.showSender());
            tvMessage.setText(entity.getBody());
            tvTime.setText(entity.getDate().split(" ")[1]);
            if (onClickListener == null) {
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iOnCLick.onClick(smsList.get(getPosition()));
                    }
                };
                itemView.setOnClickListener(onClickListener);
            }
        }
    }
}
