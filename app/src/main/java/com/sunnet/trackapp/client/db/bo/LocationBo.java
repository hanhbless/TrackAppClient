package com.sunnet.trackapp.client.db.bo;

import com.sunnet.trackapp.client.db.config.OrmliteManager;
import com.sunnet.trackapp.client.db.dao.ILocationDao;
import com.sunnet.trackapp.client.db.entity.LocationEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class LocationBo implements ILocationDao {
    public LocationBo() {
    }

    private ILocationDao getDao() throws SQLException {
        return OrmliteManager.manager().getILocationDao();
    }

    @Override
    public void createAll(List<LocationEntity> list) throws SQLException {
        getDao().createAll(list);
    }

    @Override
    public void createOrUpdateEntity(LocationEntity entity) throws SQLException {
        getDao().createOrUpdateEntity(entity);
    }

    @Override
    public List<LocationEntity> getAll() throws SQLException {
        return getDao().getAll();
    }

    @Override
    public void deleteEntity(LocationEntity entity) throws SQLException {
        getDao().deleteEntity(entity);
    }

    @Override
    public void deleteAll(List<LocationEntity> list) throws SQLException {
        getDao().deleteAll(list);
    }
}
