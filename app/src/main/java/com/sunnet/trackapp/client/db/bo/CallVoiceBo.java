package com.sunnet.trackapp.client.db.bo;

import com.sunnet.trackapp.client.db.config.OrmliteManager;
import com.sunnet.trackapp.client.db.dao.ICallVoiceDao;
import com.sunnet.trackapp.client.db.entity.CallVoiceEntity;

import java.sql.SQLException;
import java.util.ArrayList;
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
        if (list.size() > 400) {
            int size = list.size();
            int countList = size / 400 + 1;

            int next = 0;
            for (int i = 0; i < countList; i++) {
                List<CallVoiceEntity> newList = new ArrayList<>();
                for (int j = next; j < size; j++) {
                    newList.add(list.get(j));
                    if (j >= (400 * (i + 1))) {
                        next = j;
                        break;
                    }
                }
                if (newList.size() > 0) {
                    getDao().createAll(newList);
                }
            }
        } else
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
