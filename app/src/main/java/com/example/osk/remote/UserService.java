package com.example.osk.remote;

import com.example.osk.model.Instructor;
import com.example.osk.model.LocationToSend;
import com.example.osk.model.Message;
import com.example.osk.model.TimetableJson;

import java.util.ArrayList;

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
    Call<ArrayList<TimetableJson>> getTodayTimetable(@Path("id") Integer instructorId);
}


