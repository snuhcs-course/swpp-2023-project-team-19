package com.example.gathernow;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceApi {

    // SignUp API
    @POST("/api/signup/")
    Call<CodeMessageResponse> userSignUp(@Body UserData data);

    // LogIn API
    @POST("/api/login/")
    Call<CodeMessageResponse> userLogIn(@Body UserData data);

    // Event API: add new events
    @POST("/api/events/")
    Call<CodeMessageResponse> eventlist(@Body EventData data);

    @GET("/api/events/")
    Call<List<EventData>> getALlEvents();

    @GET("/api/userinfo/{user_id}/")
    Call<UserData1> getUserInfo(@Path("user_id") int userId);

    // Events by User API
    @GET("/api/events/by_user/{user_id}/")
    Call<List<EventData>> getEventsByUser(@Path("user_id") int userId);

    // Events by Event ID API
    @GET("/api/events/by_id/{event_id}/")
    Call<List<EventData>> getEventByEventId(@Path("event_id") int eventId);


}
