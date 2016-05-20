package com.sunnet.trackapp.client.util;

import android.os.Environment;

import com.sunnet.trackapp.client.application.MyApplication;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class Constants {
    public static String DATE_FORMAT = "yyyy, MMM dd HH:mm:ss";

    public static final String FOLDER_VOICE = "/voices";
    public static String DIR_ROOT;

    static {
        if (MyApplication.getContext().getExternalCacheDir() == null)
            DIR_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
        else
            DIR_ROOT = MyApplication.getContext().getExternalCacheDir().getAbsolutePath();
    }


}
