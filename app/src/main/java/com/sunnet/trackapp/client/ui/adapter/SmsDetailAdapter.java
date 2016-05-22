package com.sunnet.trackapp.client.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunnet.trackapp.client.R;
import com.sunnet.trackapp.client.db.entity.SMSEntity;

import java.util.List;

public class SmsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SMSEntity> smsList;

    public SmsDetailAdapter(List<SMSEntity> smsList) {
        this.smsList = smsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == 0) {
            viewHolder = new SmsSenderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sms_detail_sender, parent, false));
        } else {
            viewHolder = new SmsReceiverHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sms_detail_receiver, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            ((SmsSenderHolder) holder).setUi(smsList.get(position), position > 0 ? smsList.get(position - 1) : null);
        } else {
            ((SmsReceiverHolder) holder).setUi(smsList.get(position), position > 0 ? smsList.get(position - 1) : null);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return smsList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public static class SmsReceiverHolder extends RecyclerView.ViewHolder {
        View frDate;
        TextView tvMsg, tvDate, tvTime;

        public SmsReceiverHolder(View itemView) {
            super(itemView);
            frDate = itemView.findViewById(R.id.fr_date);
            tvDate = (TextView) itemView.findViewById(R.id.tv1);
            tvMsg = (TextView) itemView.findViewById(R.id.tv2);
            tvTime = (TextView) itemView.findViewById(R.id.tv3);
        }

        public void setUi(SMSEntity entity, SMSEntity entityPre) {
            tvMsg.setText(entity.getBody());
            String res[] = entity.getDate().split(" ");
            tvDate.setText(res[1]);
            tvTime.setText(res[2].substring(0, 5));
            if (entityPre != null) {
                String resPre[] = entityPre.getDate().split(" ");
                if ((res[0] + res[1]).equals(resPre[0] + resPre[1])) {
                    frDate.setVisibility(View.GONE);
                } else {
                    frDate.setVisibility(View.VISIBLE);
                }
            } else {
                frDate.setVisibility(View.VISIBLE);
            }
        }
    }

    public static class SmsSenderHolder extends RecyclerView.ViewHolder {
        View frDate;
        TextView tvMsg, tvDate, tvTime;

        public SmsSenderHolder(View itemView) {
            super(itemView);
            frDate = itemView.findViewById(R.id.fr_date);
            tvDate = (TextView) itemView.findViewById(R.id.tv1);
            tvMsg = (TextView) itemView.findViewById(R.id.tv2);
            tvTime = (TextView) itemView.findViewById(R.id.tv3);
        }

        public void setUi(SMSEntity entity, SMSEntity entityPre) {
            tvMsg.setText(entity.getBody());
            String res[] = entity.getDate().split(" ");
            tvDate.setText(res[1]);
            tvTime.setText(res[2].substring(0, 5));
            if (entityPre != null) {
                String resPre[] = entityPre.getDate().split(" ");
                if ((res[0] + res[1]).equals(resPre[0] + resPre[1])) {
                    frDate.setVisibility(View.GONE);
                } else {
                    frDate.setVisibility(View.VISIBLE);
                }
            } else {
                frDate.setVisibility(View.VISIBLE);
            }
        }
    }
}
