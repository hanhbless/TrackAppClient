package com.sunnet.trackapp.client.db;

import com.sunnet.trackapp.client.db.bo.CallLogBo;
import com.sunnet.trackapp.client.db.bo.CallVoiceBo;
import com.sunnet.trackapp.client.db.bo.CaptureBo;
import com.sunnet.trackapp.client.db.bo.ContactBo;
import com.sunnet.trackapp.client.db.bo.LocationBo;
import com.sunnet.trackapp.client.db.bo.SMSBo;
import com.sunnet.trackapp.client.db.entity.CallLogEntity;
import com.sunnet.trackapp.client.db.entity.CallVoiceEntity;
import com.sunnet.trackapp.client.db.entity.ContactEntity;
import com.sunnet.trackapp.client.db.entity.LocationEntity;
import com.sunnet.trackapp.client.db.entity.SMSEntity;
import com.sunnet.trackapp.client.util.Constants;
import com.sunnet.trackapp.client.util.ThreadPool;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class DatabaseHelper {
    private static CallVoiceBo callVoiceBo = new CallVoiceBo();
    private static CallLogBo callLogBo = new CallLogBo();
    private static LocationBo locationBo = new LocationBo();
    private static CaptureBo captureBo = new CaptureBo();
    private static SMSBo smsBo = new SMSBo();
    private static ContactBo contactBo = new ContactBo();

    public static SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

    /**
     * Location
     */

    public static List<LocationEntity> getAllLocation() {
        try {
            if (locationBo == null)
                locationBo = new LocationBo();
            return locationBo.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createAllLocation(final List<LocationEntity> locationList) {
        if (locationList != null && locationList.size() > 0) {
            ThreadPool.doDatabase(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (locationBo == null)
                            locationBo = new LocationBo();
                        locationBo.createAll(locationList);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * Call Log
     */
    public static List<CallLogEntity> getAllCallLog() {
        return null;
    }

    /**
     * Call voice
     */
    public static List<CallVoiceEntity> getAllCallVoice() {
        try {
            if (callVoiceBo == null)
                callVoiceBo = new CallVoiceBo();
            return callVoiceBo.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createAllCallVoice(final List<CallVoiceEntity> callVoiceList) {
        if (callVoiceList != null && callVoiceList.size() > 0) {
            ThreadPool.doDatabase(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (callVoiceBo == null)
                            callVoiceBo = new CallVoiceBo();
                        callVoiceBo.createAll(callVoiceList);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * SMS
     */
    public static List<SMSEntity> getAllSms() {
        try {
            if (smsBo == null)
                smsBo = new SMSBo();
            return smsBo.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createAllSms(final List<SMSEntity> smsList) {
        if (smsList != null && smsList.size() > 0) {
            ThreadPool.doDatabase(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (smsBo == null)
                            smsBo = new SMSBo();
                        smsBo.createAll(smsList);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * Contact
     */
    public static List<ContactEntity> getAllContact() {
        try {
            if (contactBo == null)
                contactBo = new ContactBo();
            return contactBo.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createAllContact(final List<ContactEntity> contactList) {
        if (contactList != null && contactList.size() > 0) {
            ThreadPool.doDatabase(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (contactBo == null)
                            contactBo = new ContactBo();
                        contactBo.createAll(contactList);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
