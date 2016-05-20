package com.sunnet.trackapp.client.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sunnet.trackapp.client.R;
import com.sunnet.trackapp.client.db.entity.SMSEntity;

public class DialogSmsDetail extends Dialog {
    Activity activity;
    SMSEntity entity;
    TextView tvNameOrPhone, tvMessage, tvTime;
    Button btnClose;
    IDialog mListener;

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
        tvNameOrPhone = (TextView) findViewById(R.id.tv1);
        tvMessage = (TextView) findViewById(R.id.tv2);
        tvTime = (TextView) findViewById(R.id.tv3);
        btnClose = (Button) findViewById(R.id.btn_close);

        tvNameOrPhone.setText(entity.getSender());
        tvMessage.setText(entity.getBody());
        tvTime.setText(entity.getDate());
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
