package com.sunnet.trackapp.client.db.bo;

import com.sunnet.trackapp.client.db.config.OrmliteManager;
import com.sunnet.trackapp.client.db.dao.ICallVoiceDao;
import com.sunnet.trackapp.client.db.entity.CallVoiceEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class CallVoiceBo implements ICallVoiceDao {
    public CallVoiceBo() {
    }

    private ICallVoiceDao getDao() throws SQLException {
        return OrmliteManager.manager().getICallVoiceDao();
    }

    @Override
    public void createAll(List<CallVoiceEntity> list) throws SQLException {
        getDao().createAll(list);
    }

    @Override
    public void createOrUpdateEntity(CallVoiceEntity entity) throws SQLException {
        getDao().createOrUpdateEntity(entity);
    }

    @Override
    public List<CallVoiceEntity> getAll() throws SQLException {
        return getDao().getAll();
    }

    @Override
    public void deleteEntity(CallVoiceEntity entity) throws SQLException {
        getDao().deleteEntity(entity);
    }
}
