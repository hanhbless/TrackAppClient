package com.sunnet.trackapp.client.task.sender;

/**
 * All software created will be owned by
 * Patient Doctor Technologies, Inc. in USA
 */
public class BaseSender {
    public String token;
    public long timeStamp;
    public String apiKey;

    @Override
    public String toString() {
        return "token='" + token + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", apiKey='" + apiKey + '\'';
    }
}
