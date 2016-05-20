package com.sunnet.trackapp.client.db.bo;

import com.sunnet.trackapp.client.db.config.OrmliteManager;
import com.sunnet.trackapp.client.db.dao.ICallLogDao;
import com.sunnet.trackapp.client.db.entity.CallLogEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class CallLogBo implements ICallLogDao {
    public CallLogBo() {
    }

    private ICallLogDao getDao() throws SQLException {
        return OrmliteManager.manager().getICallLogDao();
    }

    @Override
    public void createAll(List<CallLogEntity> list) throws SQLException {
        getDao().createAll(list);
    }

    @Override
    public void createOrUpdateEntity(CallLogEntity entity) throws SQLException {
        getDao().createOrUpdateEntity(entity);
    }

    @Override
    public List<CallLogEntity> getAll() throws SQLException {
        return getDao().getAll();
    }

    @Override
    public void deleteEntity(CallLogEntity entity) throws SQLException {
        getDao().deleteEntity(entity);
    }
}
