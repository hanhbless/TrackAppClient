package com.sunnet.trackapp.client.task.request;

import com.sunnet.trackapp.client.log.Log;
import com.sunnet.trackapp.client.util.ConfigApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class BaseRequest {

    protected OkHttpClient okHttpClient;
    protected Retrofit restAdapter;

    public void execute() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.readTimeout(ConfigApi.TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(ConfigApi.TIME_OUT, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("x-track-app-key", ConfigApi.HEADER_API).build();

                return chain.proceed(request);
            }
        });

        okHttpClient = builder.addInterceptor(interceptor).build();

        restAdapter = new Retrofit.Builder()
                .baseUrl(ConfigApi.URL_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        Log.i("URL: " + ConfigApi.URL_HOST + getUrl());
        Log.i("Param: " + getStringSender());
    }

    protected abstract String getUrl();

    protected abstract String getStringSender();
}
