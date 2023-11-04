package com.example.gathernow;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ServiceApi {


    // SignUp API PUT UserData and File avatarFile
    @Multipart
    @POST("api/signup/")
    Call<CodeMessageResponse> userSignUp(
            @Part MultipartBody.Part avatar,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );


    // LogIn API
    @POST("/api/login/")
    Call<CodeMessageResponse> userLogIn(@Body UserData data);

    // Event API: add new events
    @POST("/api/events/")
    Call<CodeMessageResponse> eventlist(@Body EventData data);

    // Get all events in the database
    @GET("/api/events/")
    Call<List<EventData>> getALlEvents();

    // get user info based on user id
    @GET("/api/userinfo/{user_id}/")
    Call<UserData> getUserInfo(@Path("user_id") int userId);

    // Get events hosted by user using user id
    @GET("/api/events/by_user/{user_id}/")
    Call<List<EventData>> getEventsByUser(@Path("user_id") int userId);

    // Get events by Event ID API
    @GET("/api/events/by_id/{event_id}/")
    Call<List<EventData>> getEventByEventId(@Path("event_id") int eventId);

    // Delete event by event id API
    @DELETE("/api/events/by_id/{event_id}/")
    Call<CodeMessageResponse> deleteEventByEventId(@Path("event_id") int eventId);

    // Post new event application to application
    @POST("/api/application/")
    Call<CodeMessageResponse> apply_event(@Body ApplicationData data);

    @GET("/api/application/check/{user_id}/{event_id}/")
    Call<ApplicationData> check_if_applied( @Path("user_id") int user_id, @Path("event_id") int event_id);
}
