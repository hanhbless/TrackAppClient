package com.sunnet.trackapp.client.db.dao;

import android.os.Build;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.sunnet.trackapp.client.db.entity.ContactEntity;
import com.sunnet.trackapp.client.util.Utils;

import java.sql.SQLException;
import java.util.List;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class ContactDao extends BaseDaoImpl<ContactEntity, String> implements IContactDao {
    public ContactDao(Class<ContactEntity> dataClass) throws SQLException {
        super(dataClass);
    }

    public ContactDao(ConnectionSource connectionSource, Class<ContactEntity> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public ContactDao(ConnectionSource connectionSource, DatabaseTableConfig<ContactEntity> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    @Override
    public void createAll(List<ContactEntity> list) throws SQLException {
        if (list == null || list.isEmpty())
            return;

        //-- Check system version because Ormlite 4.48 only support android version >= JELLY_BEAN
        //-- multiple: insert, update, delete
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            for (ContactEntity item : list) {
                createOrUpdate(item);
            }
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("INSERT OR REPLACE INTO '" + ContactEntity.TABLE_NAME + "'");
        builder.append("(");
        builder.append("'" + ContactEntity.ID_COL + "', ");
        builder.append("'" + ContactEntity.DATE_COL + "', ");
        builder.append("'" + ContactEntity.NAME_COL + "', ");
        builder.append("'" + ContactEntity.PHONE_COL + "'");
        builder.append(")");
        builder.append(" VALUES ");

        int count = list.size();
        for (int i = 0; i < count; i++) {
            ContactEntity item = list.get(i);
            builder.append("(");
            builder.append("'" + item.getId() + "', ");
            builder.append("'" + item.getDate() + "', ");
            builder.append("'" + item.getName() + "', ");
            builder.append("'" + item.getPhone() + "'");
            builder.append(")");

            if (i < count - 1)
                builder.append(",");
        }
        builder.append(";");

        //-- Execute sql
        executeRawNoArgs(builder.toString());
    }

    @Override
    public void createOrUpdateEntity(ContactEntity entity) throws SQLException {
        createOrUpdate(entity);
    }

    @Override
    public List<ContactEntity> getAll() throws SQLException {
        return queryForAll();
    }

    @Override
    public void deleteEntity(ContactEntity entity) throws SQLException {
        delete(entity);
    }

    @Override
    public void deleteAll(List<ContactEntity> list) throws SQLException {
        String userStr = Utils.getStringContactId(list);

        if (Utils.isEmptyString(userStr))
            return;

        DeleteBuilder<ContactEntity, String> deleteBuilder = deleteBuilder();
        // Remove 'quote' in the first and bottom of String Because of [Where In Clause in Ormlite format: "('" + value_1' + 'value_2' + ... 'value_n + "')" ]
        userStr = userStr.substring(1, userStr.length() - 1);
        deleteBuilder.where().in(ContactEntity.ID_COL, userStr);
        deleteBuilder.delete();
    }
}
