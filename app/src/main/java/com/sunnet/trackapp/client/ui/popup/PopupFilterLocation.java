package com.sunnet.trackapp.client.ui.popup;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupWindow;

import com.sunnet.trackapp.client.R;
import com.sunnet.trackapp.client.util.GlobalSingleton;

public class PopupFilterLocation extends PopupWindows {
    private IPopup iPopup;
    private SwitchCompat switchToday, switchAll, switchAscending, switchDescending;

    /**
     * Store old status
     */
    private boolean isOldFilterToday;
    private boolean isOldFilterAll;
    private boolean isOldFilterAscending;

    private View anchorView;
    private int xPos, yPos;
    private int screenWidth;
    private int offset;

    /**
     * Constructor.
     *
     * @param context Context
     */
    public PopupFilterLocation(Context context, final IPopup iPopup, View anchor) {
        super(context);
        this.iPopup = iPopup;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = mInflater.inflate(R.layout.popup_filter_location, null);

        switchToday = (SwitchCompat) mRootView.findViewById(R.id.switch_today);
        switchAll = (SwitchCompat) mRootView.findViewById(R.id.switch_all);
        switchAscending = (SwitchCompat) mRootView.findViewById(R.id.switch_ascending);
        switchDescending = (SwitchCompat) mRootView.findViewById(R.id.switch_descending);

        isOldFilterAll = GlobalSingleton.getInstance().isFilterAll();
        isOldFilterToday = GlobalSingleton.getInstance().isFilterToday();
        isOldFilterAscending = GlobalSingleton.getInstance().isFilterAscending();

        switchAll.setChecked(isOldFilterAll);
        switchToday.setChecked(isOldFilterToday);
        switchAscending.setChecked(isOldFilterAscending);
        switchDescending.setChecked(!isOldFilterAscending);

        setSwitchTodayListener();
        setSwitchAllListener();
        setSwitchAscendingListener();
        setSwitchDescendingListener();
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (iPopup != null) {
                    if (isOldFilterAll != GlobalSingleton.getInstance().isFilterAll()
                            || isOldFilterToday != GlobalSingleton.getInstance().isFilterToday()
                            || isOldFilterAscending != GlobalSingleton.getInstance().isFilterAscending()) {
                        iPopup.onChange();
                    }
                    iPopup.onDismiss();
                }
            }
        });

        setContentView(mRootView);

        if (anchor == null)
            return;

        this.anchorView = anchor;

        preShow();

        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private void setSwitchAllListener() {
        switchAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GlobalSingleton.getInstance().setFilterAll(true);
                GlobalSingleton.getInstance().setFilterToday(true);
                switchToday.setOnCheckedChangeListener(null);
                switchToday.setChecked(true);
                setSwitchTodayListener();

                if(!isChecked) {
                    switchAll.setOnCheckedChangeListener(null);
                    switchAll.setChecked(true);
                    setSwitchAllListener();
                }
            }
        });
    }

    private void setSwitchTodayListener() {
        switchToday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GlobalSingleton.getInstance().setFilterToday(isChecked);
                switchAll.setOnCheckedChangeListener(null);
                GlobalSingleton.getInstance().setFilterAll(!isChecked);
                switchAll.setChecked(!isChecked);
                setSwitchAllListener();
            }
        });
    }

    private void setSwitchAscendingListener() {
        switchAscending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GlobalSingleton.getInstance().setFilterAscending(isChecked);
                switchDescending.setOnCheckedChangeListener(null);
                switchDescending.setChecked(!isChecked);
                setSwitchDescendingListener();
            }
        });
    }

    private void setSwitchDescendingListener() {
        switchDescending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GlobalSingleton.getInstance().setFilterAscending(!isChecked);
                switchAscending.setOnCheckedChangeListener(null);
                switchAscending.setChecked(!isChecked);
                setSwitchAscendingListener();
            }
        });
    }

    public void show() {
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);

        if (mRootView.getMeasuredWidth() == 0 || mRootView.getMeasuredHeight() == 0)
            mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        xPos = screenWidth - mRootView.getMeasuredWidth();
        yPos = location[1] + anchorView.getHeight() - mRootView.getMeasuredHeight() - offset;

        mWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, xPos, yPos);
    }

    public interface IPopup {
        void onChange();

        void onDismiss();
    }
}
