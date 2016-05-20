package com.sunnet.trackapp.client.db.dao;

import android.os.Build;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.sunnet.trackapp.client.db.entity.SMSEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class SMSDao extends BaseDaoImpl<SMSEntity, String> implements ISMSDao {
    public SMSDao(Class<SMSEntity> dataClass) throws SQLException {
        super(dataClass);
    }

    public SMSDao(ConnectionSource connectionSource, Class<SMSEntity> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public SMSDao(ConnectionSource connectionSource, DatabaseTableConfig<SMSEntity> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public void createAll(List<SMSEntity> list) throws SQLException {
        if (list == null || list.isEmpty())
            return;

        //-- Check system version because Ormlite 4.48 only support android version >= JELLY_BEAN
        //-- multiple: insert, update, delete
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            for (SMSEntity item : list) {
                createOrUpdate(item);
            }
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("INSERT OR REPLACE INTO '" + SMSEntity.TABLE_NAME + "'");
        builder.append("(");
        builder.append("'" + SMSEntity.ID_COL + "', ");
        builder.append("'" + SMSEntity.DATE_COL + "', ");
        builder.append("'" + SMSEntity.SENDER_COL + "', ");
        builder.append("'" + SMSEntity.RECEIVER_COL + "', ");
        builder.append("'" + SMSEntity.BODY_COL + "'");
        builder.append(")");
        builder.append(" VALUES ");

        int count = list.size();
        for (int i = 0; i < count; i++) {
            SMSEntity item = list.get(i);
            builder.append("(");
            builder.append("'" + item.getId() + "', ");
            builder.append("'" + item.getDate() + "', ");
            builder.append("'" + item.getSender() + "', ");
            builder.append("'" + item.getReceiver() + "', ");
            builder.append("'" + item.getBody() + "'");
            builder.append(")");

            if (i < count - 1)
                builder.append(",");
        }
        builder.append(";");

        //-- Execute sql
        executeRawNoArgs(builder.toString());
    }

    @Override
    public List<SMSEntity> getAll() throws SQLException {
        return queryForAll();
    }

    @Override
    public void createOrUpdateEntity(SMSEntity entity) throws SQLException {
        createOrUpdate(entity);
    }

    @Override
    public void deleteEntity(SMSEntity entity) throws SQLException {
        delete(entity);
    }
}
