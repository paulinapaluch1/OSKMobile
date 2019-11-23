package com.example.osk.remote;

import com.example.osk.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {

    @GET("login/{username}/{password}")
    Call<User> login(@Path("username") String username, @Path("password") String password);


}
