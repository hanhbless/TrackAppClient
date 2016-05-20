package com.sunnet.trackapp.client.db.config;

import com.sunnet.trackapp.client.db.dao.ICallLogDao;
import com.sunnet.trackapp.client.db.dao.ICallVoiceDao;
import com.sunnet.trackapp.client.db.dao.ICaptureDao;
import com.sunnet.trackapp.client.db.dao.IContactDao;
import com.sunnet.trackapp.client.db.dao.ILocationDao;
import com.sunnet.trackapp.client.db.dao.ISMSDao;

import java.sql.SQLException;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */

public interface IOrmliteManager {
    ICallVoiceDao getICallVoiceDao() throws SQLException;

    ICallLogDao getICallLogDao() throws SQLException;

    ICaptureDao getICaptureDao() throws SQLException;

    ILocationDao getILocationDao() throws SQLException;

    ISMSDao getISMSDao() throws SQLException;

    IContactDao getIContactDao() throws SQLException;

}
