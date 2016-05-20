package com.sunnet.trackapp.client.ui.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.sunnet.trackapp.client.db.entity.LocationEntity;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class MarkerItem implements ClusterItem {
    private final LatLng mPosition;
    private LocationEntity entity;

    public MarkerItem(LocationEntity entity) {
        this.entity = entity;
        mPosition = new LatLng(Double.parseDouble(entity.getLatitude()),
                Double.parseDouble(entity.getLongitude()));
    }

    public LocationEntity getLocationEntity() {
        return entity;
    }

    public String getTitle() {
        return entity.getPlace();
    }

    public String getSnippet() {
        return entity.getAddress();
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
