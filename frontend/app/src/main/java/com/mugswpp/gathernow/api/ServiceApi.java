package com.mugswpp.gathernow.api;

import com.mugswpp.gathernow.api.models.ApplicationDataModel;
import com.mugswpp.gathernow.api.models.EventDataModel;
import com.mugswpp.gathernow.api.models.UserDataModel;

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
import retrofit2.http.Url;

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
    Call<CodeMessageResponse> userLogIn(@Body UserDataModel data);

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
            @Part("event_longitude") RequestBody event_longitude,
            @Part("event_latitude") RequestBody event_latitude,
            @Part("event_description") RequestBody event_description,
            @Part("event_num_joined") RequestBody event_num_joined,
            @Part("event_register_date") RequestBody event_register_date,
            @Part("event_register_time") RequestBody event_register_time
    );

    // Get all events in the database
    @GET("/api/events/")
    Call<List<EventDataModel>> getALlEvents();

    // get user info based on user id
    @GET("/api/userinfo/{user_id}/")
    Call<UserDataModel> getUserInfo(@Path("user_id") int userId);

    // Get events hosted by user using user id
    @GET("/api/events/by_user/{user_id}/")
    Call<List<EventDataModel>> getEventsByUser(@Path("user_id") int userId);

    // Get events by Event ID API
    @GET("/api/events/by_id/{event_id}/")
    Call<List<EventDataModel>> getEventByEventId(@Path("event_id") int eventId);

    // Delete event by event id API
    @DELETE("/api/events/by_id/{event_id}/")
    Call<CodeMessageResponse> deleteEventByEventId(@Path("event_id") int eventId);

    // Post new event application to application
    @POST("/api/application/")
    Call<CodeMessageResponse> apply_event(@Body ApplicationDataModel data);

    // Check if user has applied ot the event
    @GET("/api/application/check/{user_id}/{event_id}/")
    Call<ApplicationDataModel> check_if_applied(@Path("user_id") int user_id, @Path("event_id") int event_id);

    @DELETE("/api/application/check/{user_id}/{event_id}/")
    Call<ApplicationDataModel> delete_if_applied(@Path("user_id") int user_id, @Path("event_id") int event_id);

    // Delete application based on application id
    // Applicant is rejected by host and related application will be deleted
    @DELETE("/api/application/one/{application_id}/")
    Call<ApplicationDataModel> delete_application(@Path("application_id") int application_id);

    // Get applications for an event using event_id
    @GET("/api/application/by_event/{event_id}/")
    Call<List<ApplicationDataModel>> getEventApplications(@Path("event_id") int event_id);

    // Update request_status to 1 when application is accepted
    @PUT("/api/application/accept/{application_id}/{status}/")
    Call<ApplicationDataModel> acceptStatus(@Path("application_id") int application_id, @Path("status") int status );

    // get list of events that a user applied for
    @GET("api/application/by_user/{user_id}/")
    Call<List<ApplicationDataModel>> getUserAppliedEvents(@Path("user_id") int user_id);

    @GET
    Call<List<EventDataModel>> getFilteredEvents(@Url String url);

    @GET
    Call<List<EventDataModel>> getSearchedEvents(@Url String url);

    // Increase number of user joined for an event by 1
    @PUT("/api/events/num_joined/add/{event_id}/")
    Call<ApplicationDataModel> increaseNumJoined(@Path("event_id") int event_id);

    // Decrease number of user joined for an event by 1
    @PUT("/api/events/num_joined/delete/{event_id}/")
    Call<ApplicationDataModel> decreaseNumJoined(@Path("event_id") int event_id);

    // Delete application of an event using event_id
    @DELETE("/api/application/by_event/{event_id}/")
    Call<List<ApplicationDataModel>> deleteEventApplications(@Path("event_id") int event_id);
}
