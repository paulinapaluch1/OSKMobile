package com.example.osk.remote;

public class ApiUtils {

 //   public static final String BASE_URL="http://192.168.43.115:7070/mobile/";
      public static final String BASE_URL="http://192.168.1.11:7070/mobile/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}
