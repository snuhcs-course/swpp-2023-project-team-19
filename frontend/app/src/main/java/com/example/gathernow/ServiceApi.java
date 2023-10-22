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
    @GET("/api/login")
    Call<CodeMessageResponse> userLogin(@Body UserData data);

}
