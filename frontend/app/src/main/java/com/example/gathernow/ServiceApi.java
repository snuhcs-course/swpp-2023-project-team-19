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
import retrofit2.http.PUT;
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
    @Multipart
    @POST("/api/events/")
    Call<CodeMessageResponse> eventlist(
            @Part MultipartBody.Part thumbnail,
            @Part("host_id") RequestBody host_id,
            @Part("event_type") RequestBody event_type,
            @Part("event_title") RequestBody event_title,
            @Part("event_num_participants") RequestBody event_num_participants,
            @Part("event_date") RequestBody event_date,
            @Part("event_time") RequestBody event_time,
            @Part("event_duration") RequestBody event_duration,
            @Part("event_language") RequestBody event_language,
            @Part("event_price") RequestBody event_price,
            @Part("event_location") RequestBody event_location,
            @Part("event_description") RequestBody event_description,
            @Part("event_num_joined") RequestBody event_num_joined,
            @Part("event_register_date") RequestBody event_register_date,
            @Part("event_register_time") RequestBody event_register_time
    );

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

    // Check if user has applied ot the event
    @GET("/api/application/check/{user_id}/{event_id}/")
    Call<ApplicationData> check_if_applied( @Path("user_id") int user_id, @Path("event_id") int event_id);


    // Delete application based on application id
    // Applicant is rejected by host and related application will be deleted
    @DELETE("/api/application/one/{application_id}/")
    Call<ApplicationData> delete_application(@Path("application_id") int application_id);

    // Get applications for an event using event_id
    @GET("/api/application/by_event/{event_id}/")
    Call<List<ApplicationData>> getEventApplications(@Path("event_id") int event_id);

    // Update request_status to 1 when application is accepted
    @PUT("/api/application/accept/{application_id}/")
    Call<ApplicationData> acceptStatus(@Path("application_id") int application_id);

    // get list of events that a user applied for
    @GET("api/application/by_user/{user_id}/")
    Call<List<ApplicationData>> getUserAppliedEvents(@Path("user_id") int user_id);

}
