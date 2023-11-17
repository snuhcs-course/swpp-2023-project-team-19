package com.example.gathernow.main_ui;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.api.CodeMessageResponse;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDataSource {
    private final ServiceApi service;

    public EventDataSource() {
        service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    public void createEvent(String thumbnailFilePath, String creator, String type, String name, String description, String date, String time, String duration, String location, String languages, String maxParticipants, String price, String lastRegisterDate, String lastRegisterTime, CallbackInterface callback) {
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

        Call<CodeMessageResponse> call = service.eventlist(thumbnailPart, hostIdBody, eventTypeBody, eventNameBody, eventNumParticipantsBody, eventDateBody, eventTimeBody, eventDurationBody, eventLanguageBody, eventPriceBody, eventLocationBody, eventDescriptionBody, eventNumJoinedBody, eventRegDateBody, eventRegTimeBody);
        call.enqueue(new Callback<CodeMessageResponse>() {
            @Override
            public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
                if (response.isSuccessful()) {
                    CodeMessageResponse result = response.body();
                    if (result != null) {
                        if (response.code() == 201) {
                            callback.onSuccess("Event created successfully");
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

    public void getEventInfo(int eventId, CallbackInterface callback) {
        service.getEventByEventId(eventId).enqueue(new Callback<List<EventDataModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<EventDataModel>> call, @NonNull Response<List<EventDataModel>> response) {
                if (response.isSuccessful()) {
                    List<EventDataModel> result = response.body();
                    if (result != null) {
                        callback.onSuccess(result.get(0));
                    } else {
                        callback.onError("Empty response from the server");
                    }
                } else {
                    callback.onError("Event creation failed.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EventDataModel>> call, @NonNull Throwable t) {
                callback.onError("Network error");
            }
        });
    }

    public void checkUserAppliedEvent(int userId, int eventId, CallbackInterface callback) {
        service.check_if_applied(userId, eventId).enqueue(new Callback<ApplicationDataModel>() {
            @Override
            public void onResponse(Call<ApplicationDataModel> call, Response<ApplicationDataModel> response) {
                if (response.isSuccessful()) {
                    ApplicationDataModel result = response.body();
                    // return the application status
                    callback.onSuccess(result);
                } else if (response.code() == 404) {
                    callback.onError("No application found");
                } else {
                    callback.onError("Check application status failed");
                }
            }

            @Override
            public void onFailure(Call<ApplicationDataModel> call, Throwable t) {
                callback.onError("Network error");
            }
        });
    }

    public void deleteEvent(int eventId, CallbackInterface callback) {
        service.deleteEventByEventId(eventId).enqueue(new Callback<CodeMessageResponse>() {
            @Override
            public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
                Log.d("EventInfo Testing", "Event deleted");
                if (response.isSuccessful()) {
                    callback.onSuccess("Event deleted successfully");
                } else {
                    callback.onError("Failed to delete event");
                }
            }

            @Override
            public void onFailure(Call<CodeMessageResponse> call, Throwable t) {
                callback.onError("Network error");
            }
        });
    }

    public void deleteApplication(int applicationId, CallbackInterface callback) {
        service.delete_application(applicationId).enqueue(new Callback<ApplicationDataModel>() {
            @Override
            public void onResponse(Call<ApplicationDataModel> call, Response<ApplicationDataModel> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Application deleted successfully");
                } else {
                    callback.onError("Failed to delete application");
                }
            }

            @Override
            public void onFailure(Call<ApplicationDataModel> call, Throwable t) {
                callback.onError("Network error");
            }
        });
    }

    public void getUserEvents(int userId, CallbackInterface callback) {
        service.getEventsByUser(userId).enqueue(new Callback<List<EventDataModel>>() {
            @Override
            public void onResponse(Call<List<EventDataModel>> call, Response<List<EventDataModel>> response) {
                if (response.isSuccessful()) {
                    List<EventDataModel> result = response.body();
                    if (result != null) {
                        callback.onSuccess(result);
                    } else {
                        callback.onError("Empty response from the server");
                    }
                } else {
                    callback.onError("Failed to get user events");
                }
            }

            @Override
            public void onFailure(Call<List<EventDataModel>> call, Throwable t) {
                callback.onError("Network error");
            }
        });
    }

    public void getEventApplication(int eventId, CallbackInterface eventCallback) {
        service.getEventApplications(eventId).enqueue(new Callback<List<ApplicationDataModel>>() {
            @Override
            public void onResponse(Call<List<ApplicationDataModel>> call, Response<List<ApplicationDataModel>> response) {
                if (response.isSuccessful()) {
                    List<ApplicationDataModel> result = response.body();
                    if (result != null) {
                        eventCallback.onSuccess(result);
                    } else {
                        eventCallback.onError("Empty response from the server");
                    }
                } else if (response.code() == 404) {
                    eventCallback.onError("No applications found");
                } else {
                    eventCallback.onError("Failed to get event applications");
                }
            }

            @Override
            public void onFailure(Call<List<ApplicationDataModel>> call, Throwable t) {
                eventCallback.onError("Network error");
            }
        });
    }
}

