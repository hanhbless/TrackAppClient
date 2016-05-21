package com.sunnet.trackapp.client.util;

import com.sunnet.trackapp.client.db.entity.CallVoiceEntity;
import com.sunnet.trackapp.client.db.entity.ContactEntity;
import com.sunnet.trackapp.client.db.entity.LocationEntity;
import com.sunnet.trackapp.client.db.entity.SMSEntity;

import java.util.List;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class GlobalSingleton {
    private static GlobalSingleton ourInstance = new GlobalSingleton();

    public static GlobalSingleton getInstance() {
        return ourInstance;
    }

    private GlobalSingleton() {
    }

    /*******************************************************************************/
    private List<LocationEntity> locationList;
    private List<ContactEntity> contactList;
    private List<SMSEntity> smsList;
    private List<CallVoiceEntity> voiceList;

    public List<LocationEntity> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<LocationEntity> locationList) {
        this.locationList = locationList;
    }

    public List<ContactEntity> getContactList() {
        return contactList;
    }

    public void setContactList(List<ContactEntity> contactList) {
        this.contactList = contactList;
    }

    public List<SMSEntity> getSmsList() {
        return smsList;
    }

    public void setSmsList(List<SMSEntity> smsList) {
        this.smsList = smsList;
    }

    public List<CallVoiceEntity> getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(List<CallVoiceEntity> voiceList) {
        this.voiceList = voiceList;
    }

    /*******************************************************************************/
    private boolean isFilterToday = true;
    private boolean isFilterAll = true;
    private boolean isFilterAscending = true;

    public boolean isFilterToday() {
        return isFilterToday;
    }

    public void setFilterToday(boolean filterToday) {
        isFilterToday = filterToday;
    }

    public boolean isFilterAll() {
        return isFilterAll;
    }

    public void setFilterAll(boolean filterAll) {
        isFilterAll = filterAll;
    }

    public boolean isFilterAscending() {
        return isFilterAscending;
    }

    public void setFilterAscending(boolean filterAscending) {
        isFilterAscending = filterAscending;
    }
}
