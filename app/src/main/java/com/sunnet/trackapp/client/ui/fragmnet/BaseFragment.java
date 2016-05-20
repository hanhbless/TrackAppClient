package com.sunnet.trackapp.client.ui.fragmnet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunnet.trackapp.client.application.MyApplication;
import com.sunnet.trackapp.client.log.Log;
import com.sunnet.trackapp.client.ui.activity.MainActivity;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public abstract class BaseFragment extends Fragment {
    protected abstract int getContentView();

    protected abstract void initView();

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("onCreateView: " + this.getClass().getSimpleName());
        int layoutId = getContentView();
        if (layoutId > 0) {
            view = inflater.inflate(layoutId, container, false);
        } else {
            view = super.onCreateView(inflater, container, savedInstanceState);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    protected View findViewById(int id) {
        if (view != null) {
            return view.findViewById(id);
        }
        return null;
    }

    protected String getResourceString(int resId) {
        if (getActivity() == null) {
            Log.i("getResourceString: activity null");
        }
        return MyApplication.getContext().getString(resId);
    }

    public MainActivity getMainActivity() {
        if (getActivity() != null && getActivity() instanceof MainActivity)
            return (MainActivity) getActivity();
        return null;
    }

}
