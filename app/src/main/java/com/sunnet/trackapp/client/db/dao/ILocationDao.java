package com.sunnet.trackapp.client.db.dao;

import com.sunnet.trackapp.client.db.entity.LocationEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public interface ILocationDao {

    List<LocationEntity> getAll() throws SQLException;

    void createAll(List<LocationEntity> list) throws SQLException;

    void createOrUpdateEntity(LocationEntity entity) throws SQLException;

    void deleteEntity(LocationEntity entity) throws SQLException;

    void deleteAll(List<LocationEntity> list) throws SQLException;
}
