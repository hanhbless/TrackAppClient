package com.sunnet.trackapp.client.task.request;

import com.sunnet.trackapp.client.task.api.IStatisticalApi;
import com.sunnet.trackapp.client.task.response.StatisticalResponse;
import com.sunnet.trackapp.client.task.sender.StatisticalSender;

import retrofit2.Call;
import retrofit2.Callback;

public class StatisticalRequest extends BaseRequest {
    private StatisticalSender sender;
    private Callback<StatisticalResponse> callback;

    public StatisticalRequest(StatisticalSender sender, Callback<StatisticalResponse> callback) {
        this.sender = sender;
        this.callback = callback;
    }

    @Override
    public void execute() {
        super.execute();
        IStatisticalApi api = restAdapter.create(IStatisticalApi.class);
        Call call = api.statistical(sender.token, sender.timeStamp, sender.apiKey);
        call.enqueue(callback);
    }

    @Override
    protected String getUrl() {
        return IStatisticalApi.url;
    }

    @Override
    protected String getStringSender() {
        return sender.toString();
    }
}
