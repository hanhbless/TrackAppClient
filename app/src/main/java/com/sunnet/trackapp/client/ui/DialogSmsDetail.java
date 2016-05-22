package com.sunnet.trackapp.client.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sunnet.trackapp.client.R;
import com.sunnet.trackapp.client.db.entity.SMSEntity;
import com.sunnet.trackapp.client.ui.adapter.SmsDetailAdapter;
import com.sunnet.trackapp.client.util.Utils;

public class DialogSmsDetail extends Dialog {
    Activity activity;
    SMSEntity entity;
    RecyclerView recyclerView;
    Button btnClose;
    TextView tvPhone, tvName;
    IDialog mListener;

    SmsDetailAdapter adapter;

    public DialogSmsDetail(Context context, SMSEntity entity, IDialog mListener) {
        super(context, R.style.FullHeightDialog);
        activity = (Activity) context;
        this.entity = entity;
        this.mListener = mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sms_detail);
        setCancelable(false);
        init();
    }

    private void init() {
        tvName = (TextView) findViewById(R.id.tv1);
        tvPhone = (TextView) findViewById(R.id.tv2);

        tvName.setVisibility(View.GONE);
        if (Utils.isEmptyString(entity.getNameSender())) {
            tvPhone.setText(entity.getSender());
        } else {
            tvName.setText(entity.getNameSender());
            tvPhone.setText(entity.getSender());
            tvName.setVisibility(View.VISIBLE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);

        adapter = new SmsDetailAdapter(entity.getSmsList());
        recyclerView.setAdapter(adapter);

        btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.close();
                dismiss();
            }
        });
    }

    public interface IDialog {
        void close();
    }
}
