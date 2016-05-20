package com.sunnet.trackapp.client.task.api;

import com.sunnet.trackapp.client.task.response.StatisticalResponse;
import com.sunnet.trackapp.client.util.ConfigApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public interface IStatisticalApi {
    String url = ConfigApi.STATISTICAL;

    @GET(url)
    Call<StatisticalResponse> statistical(
            @Query("token") String token,
            @Query("timestamp") long timeStamp,
            @Query("apikey") String apiKey
    );
}
