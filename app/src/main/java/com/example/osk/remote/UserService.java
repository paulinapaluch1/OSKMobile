package com.example.osk.remote;

import com.example.osk.model.Instructor;
import com.example.osk.model.LocationToSend;
import com.example.osk.model.Message;
import com.example.osk.sqlite.Timetable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Path;

public interface UserService {

    @GET("login/{username}/{password}")
    Call<Instructor> login(@Path("username") String username,
                           @Path("password") String password);

    @HTTP(method = "POST", path = "send/{instructor}", hasBody = true)
    Call<Message> sendCoordinates(@Body ArrayList<LocationToSend> coordinates,
                                  @Path("instructor") Integer currentLoggedInstructorId);

    @GET("timetable/{id}")
    Call<List<Timetable>> getTodayTimetable(@Path("id") Integer instructorId);


}


