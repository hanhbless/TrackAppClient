package com.sunnet.trackapp.client.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sunnet.trackapp.client.db.dao.CaptureDao;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */

@DatabaseTable(tableName = CaptureEntity.TABLE_NAME, daoClass = CaptureDao.class)
public class CaptureEntity extends BaseEntity {
    public static final String TABLE_NAME = "Capture";

    public static final String ID_COL = "ID_COL";
    public static final String DATE_COL = "DATE_COL";
    public static final String PICTURE_COL = "PICTURE_COL";

    @DatabaseField(columnName = ID_COL, canBeNull = false, id = true)
    private String id;
    @DatabaseField(columnName = DATE_COL)
    private String date;
    @DatabaseField(columnName = PICTURE_COL)
    private String picture;

    public CaptureEntity() {
    }

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
