package com.example.gathernow.main_ui.event_creation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gathernow.main_ui.EventCallback;
import com.example.gathernow.utils.ImageHelper;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public class EventCreateViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private MutableLiveData<String> eventFileThumbnail = new MutableLiveData<>();
    private MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final EventRepository eventRepository;

    // get methods for the above variables
    public MutableLiveData<String> getEventFileThumbnail() {
        return eventFileThumbnail;
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }


    public EventCreateViewModel(Context context, EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.context = context;
    }

    public void handleEventThumbnailUpload(Uri uri) {
        String eventFileThumbnail = ImageHelper.handleImagePicker(context, uri, "event_thumbnail");
        if (eventFileThumbnail != null) {
            this.eventFileThumbnail.postValue(eventFileThumbnail);
            Log.d("EventCreateViewModel", "handleEventThumbnailUpload: " + eventFileThumbnail);
        }
    }

    // Event type
    private final String[] typeChoices = {"Leisure", "Sports", "Workshops", "Parties", "Cultural activities", "Others"};
    private final AtomicInteger eventTypeInputIdx = new AtomicInteger(0);
    public String[] getTypeChoices() {
        return typeChoices;
    }

    public int getEventTypeInputIdx() {
        return eventTypeInputIdx.get();
    }

    public void setEventTypeInputIdx(int eventTypeInputIdx) {
        this.eventTypeInputIdx.set(eventTypeInputIdx);
    }

    // Event Datetime
    private final MutableLiveData<Calendar> eventDate = new MutableLiveData<>();
    private final MutableLiveData<Calendar> eventRegistrationDate = new MutableLiveData<>();
    public LiveData<Calendar> getEventDate() {
        return eventDate;
    }

    public LiveData<Calendar> getEventRegistrationDate() {
        return eventRegistrationDate;
    }

    public void setEventDate(Calendar date) {
        eventDate.setValue(date);
    }

    public void setEventRegistrationDate(Calendar date) {
        eventRegistrationDate.setValue(date);
    }


    public void createEvent(String thumbnailFilePath, String creator, String type, String name, String description, String date, String time, String duration, String location, String languages, String maxParticipants, String price, String lastRegisterDate, String lastRegisterTime) {
        if (!areEventCreationInputsValid(type, name, description, languages, date, time, duration, location, maxParticipants, price, lastRegisterDate, lastRegisterTime)) {
            return;
        }
        eventRepository.createEvent(thumbnailFilePath, creator, type, name, description, date, time, duration, location, languages, maxParticipants, price, lastRegisterDate, lastRegisterTime, new EventCallback() {
            @Override
            public void onSuccess() {
                alertMessage.postValue("Event created successfully");
            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
            }
        });

    }

    public boolean areEventCreationInputsValid(String type, String name, String description, String languages, String date, String time, String duration, String location, String maxParticipants, String price, String lastRegisterDate, String lastRegisterTime) {
//        if (name.isEmpty() || duration.isEmpty() || price.isEmpty() || description.isEmpty() || location.isEmpty() || event_date_input[0] == 0 || event_hour_input[0] == 0 || maxParticipants.isEmpty() || type.isEmpty() || event_reg_date_input[0] == 0 || event_reg_hour_input[0] == 0 || languages.isEmpty()) {
//            String alert_msg = "Please fill in all required fields";
//            alertMessage.postValue(alert_msg);
//            return false;
//        } else if (Integer.parseInt(maxParticipants) < 1) {
//            // compare the string value of max_participants with 0
//            String alert_msg = "Number of participants should be at least 1!";
//            alertMessage.postValue(alert_msg);
//            return false;
//        }
        return true;
    }






}
