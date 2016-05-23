package com.sunnet.trackapp.client.util;

public class ConfigApi {
    //-- Crypto
    public static final String KEY_CRYPTO = "500b83454e65db1a6bf4152473247216577f87dec387ee563a9e46a248ca4385";
    public static final String IV_CRYPTO = "ac52060c76cbbc5be12cead76af8b2a3";

    public static final String DEFAULT_PHONE_NUMBER = "011";

    public static final String API_KEY = "wnTdm7Mx7VdZzgV6VU9az6gF";
    public static final int TIME_OUT = 90;
    public static final String HEADER_API = "";
    public static final String URL_HOST = "http://128.199.234.7:3003/";
    public static final String URL_VOICE = "http://128.199.234.7/upload/uploads/";

    public static final String STATISTICAL = "w33";

    public enum STATUS_RESPONSE {
        SUCCESS(1),
        INCORRECT_PARAM(-101),
        KEY_API_OUT_DATE(-102),
        SIZE_OUT(-103);

        private int value;

        STATUS_RESPONSE(int value) {
            this.value = value;
        }
    }

    public static String genToken(long time) {
        return Utils.hashMac(ConfigApi.API_KEY + "/" + time + "/tvhanh");
    }
}
