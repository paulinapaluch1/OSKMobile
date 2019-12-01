package com.example.osk.remote;
import com.example.osk.model.Instructor;
import com.example.osk.model.Location;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @GET("login/{username}/{password}")
    Call<Instructor> login(@Path("username") String username,
                           @Path("password") String password);

    @POST("sendCoordinates/{coordinates}/")
    Call<Boolean> sendCoordinates(@Path("coordinates") List<Location> coordinates);




}
