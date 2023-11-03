package com.example.gathernow;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
