package com.example.gathernow.main_ui.event_creation;

import com.example.gathernow.api.CodeMessageResponse;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;
import com.example.gathernow.main_ui.EventCallback;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDataSource {
    private ServiceApi service;

    public EventDataSource() {
    }

    public void createEvent(String thumbnailFilePath, String creator, String type, String name, String description, String date, String time, String duration, String location, String languages, String maxParticipants, String price, String lastRegisterDate, String lastRegisterTime, EventCallback callback) {
        // Thumbnail
        MultipartBody.Part thumbnailPart = null;

        if (thumbnailFilePath != null) {
            File thumbnailFile = new File(thumbnailFilePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), thumbnailFile);
            thumbnailPart = MultipartBody.Part.createFormData("event_images", thumbnailFile.getName(), requestFile);
        }

        RequestBody eventTypeBody = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody eventNameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody eventNumParticipantsBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(maxParticipants));
        RequestBody eventDateBody = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody eventTimeBody = RequestBody.create(MediaType.parse("text/plain"), time);
        RequestBody eventDurationBody = RequestBody.create(MediaType.parse("text/plain"), duration);
        RequestBody eventLanguageBody = RequestBody.create(MediaType.parse("text/plain"), languages);
        RequestBody eventPriceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
        RequestBody eventLocationBody = RequestBody.create(MediaType.parse("text/plain"), location);
        RequestBody eventDescriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody eventNumJoinedBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(0));
        RequestBody hostIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(creator));
        RequestBody eventRegDateBody = RequestBody.create(MediaType.parse("text/plain"), lastRegisterDate);
        RequestBody eventRegTimeBody = RequestBody.create(MediaType.parse("text/plain"), lastRegisterTime);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        Call<CodeMessageResponse> call = service.eventlist(thumbnailPart, hostIdBody, eventTypeBody, eventNameBody, eventNumParticipantsBody, eventDateBody, eventTimeBody, eventDurationBody, eventLanguageBody, eventPriceBody, eventLocationBody, eventDescriptionBody, eventNumJoinedBody, eventRegDateBody, eventRegTimeBody);
        call.enqueue(new Callback<CodeMessageResponse>() {
            @Override
            public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
                if (response.isSuccessful()) {
                    CodeMessageResponse result = response.body();
                    if (result != null) {
                        if (response.code() == 201) {
                            callback.onSuccess();
                        }
                    } else {
                        callback.onError("Empty response from the server");
                    }
                } else {
                    callback.onError("Event creation failed.");
                }
            }

            @Override
            public void onFailure(Call<CodeMessageResponse> call, Throwable t) {
                callback.onError("Network error");
            }
        });
    }


}

