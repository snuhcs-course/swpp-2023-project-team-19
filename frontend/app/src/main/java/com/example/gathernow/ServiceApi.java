package com.example.gathernow;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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

}
