package com.mugswpp.gathernow.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL = "http://20.2.88.70:8000"; // Replace EC2 endpoint with yours
    // private final static String BASE_URL = "http://10.0.2.2:8000";


    private static Retrofit retrofit = null;

    private RetrofitClient() {
    }
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
