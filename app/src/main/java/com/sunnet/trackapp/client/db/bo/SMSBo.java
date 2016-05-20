package com.sunnet.trackapp.client.db.bo;

import com.sunnet.trackapp.client.db.config.OrmliteManager;
import com.sunnet.trackapp.client.db.dao.ISMSDao;
import com.sunnet.trackapp.client.db.entity.SMSEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class SMSBo implements ISMSDao {
    public SMSBo() {
    }

    private ISMSDao getDao() throws SQLException {
        return OrmliteManager.manager().getISMSDao();
    }

    @Override
    public void createAll(List<SMSEntity> list) throws SQLException {
        getDao().createAll(list);
    }

    @Override
    public void createOrUpdateEntity(SMSEntity entity) throws SQLException {
        getDao().createOrUpdateEntity(entity);
    }

    @Override
    public List<SMSEntity> getAll() throws SQLException {
        return getDao().getAll();
    }

    @Override
    public void deleteEntity(SMSEntity entity) throws SQLException {
        getDao().deleteEntity(entity);
    }
}
