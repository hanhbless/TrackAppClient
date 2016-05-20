package com.sunnet.trackapp.client.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunnet.trackapp.client.R;
import com.sunnet.trackapp.client.db.entity.ContactEntity;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    private static IOnCLick iOnCLick;
    private static List<ContactEntity> contactList;

    public ContactAdapter(List<ContactEntity> contactList, IOnCLick iOnCLick) {
        this.contactList = contactList;
        this.iOnCLick = iOnCLick;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact_item, parent, false);
        return new ContactHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        holder.setUi(contactList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactList != null ? contactList.size() : 0;
    }

    static class ContactHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvName, tvPhone;
        View.OnClickListener onClickListener;

        public ContactHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvName = (TextView) itemView.findViewById(R.id.tv1);
            tvPhone = (TextView) itemView.findViewById(R.id.tv2);
        }

        public void setUi(final ContactEntity entity) {
            tvName.setText(entity.getName());
            tvPhone.setText(entity.getPhone());
            if (onClickListener == null) {
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iOnCLick.onClick(contactList.get(getPosition()));
                    }
                };
                itemView.setOnClickListener(onClickListener);
            }
        }
    }
}
