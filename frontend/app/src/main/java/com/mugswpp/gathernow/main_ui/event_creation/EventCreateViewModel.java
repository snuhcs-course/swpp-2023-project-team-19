package com.mugswpp.gathernow.main_ui.event_creation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mugswpp.gathernow.main_ui.CallbackInterface;
import com.mugswpp.gathernow.main_ui.EventRepository;
import com.mugswpp.gathernow.utils.ImageHelper;

import java.util.Arrays;
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
//            Log.d("EventCreateViewModel", "handleEventThumbnailUpload: " + eventFileThumbnail);
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
    private final MutableLiveData<Calendar> eventLastRegistrationDate = new MutableLiveData<>();

    public LiveData<Calendar> getEventDate() {
        return eventDate;
    }

    public LiveData<Calendar> getEventLastRegistrationDate() {
        return eventLastRegistrationDate;
    }

    private final MutableLiveData<Calendar> eventTime = new MutableLiveData<>();
    private final MutableLiveData<Calendar> eventLastRegistrationTime = new MutableLiveData<>();

    public LiveData<Calendar> getEventTime() {
        return eventTime;
    }

    public LiveData<Calendar> getEventLastRegistrationTime() {
        return eventLastRegistrationTime;
    }

    // Languages
    private final String[] languageChoices = {"Korean", "English", "Japanese", "Chinese", "Russian", "Vietnamese", "Thai", "Uzbek", "Khmer", "Filipino", "Nepali", "Indonesian", "Kazakh", "Mongolian", "Burmese", "Spanish", "Portuguese", "French", "German", "Hindi", "Arabic", "Bengali", "Urdu", "Turkish", "Other (specify in descriptions)"};
    private final MutableLiveData<String> selectedLanguages = new MutableLiveData<>();
    private final boolean[] selectedLanguage = new boolean[languageChoices.length];

    public String[] getLanguageChoices() {
        return languageChoices;
    }

    public MutableLiveData<String> getSelectedLanguages() {
        return selectedLanguages;
    }

    public void clearSelectedLanguages() {
        selectedLanguages.setValue("");
        Arrays.fill(selectedLanguage, false);
    }

    public void updateSelectedLanguages() {
        StringBuilder selectedLanguages = new StringBuilder();
        for (int i = 0; i < languageChoices.length; i++) {
            if (selectedLanguage[i]) {
                selectedLanguages.append(languageChoices[i]).append(", ");
            }
        }
        if (selectedLanguages.length() > 0) {
            selectedLanguages.delete(selectedLanguages.length() - 2, selectedLanguages.length());
        }
        String selectedLanguagesStr = selectedLanguages.toString().replace("Other (specify in descriptions)", "Other");
        this.selectedLanguages.setValue(selectedLanguagesStr);
    }

    public boolean[] getLanguageChoicesSelected() {
        return selectedLanguage;
    }

    public void setLanguageChoicesSelected(int i, boolean b) {
        selectedLanguage[i] = b;
    }

    // Event price
    private final MutableLiveData<Integer> eventPrice = new MutableLiveData<>();

    public MutableLiveData<Integer> getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(String eventPriceStr) {
        try {
            int eventPrice = Integer.parseInt(eventPriceStr);
            if (eventPrice < 0) {
                throw new NumberFormatException();
            }
            this.eventPrice.setValue(eventPrice);
        } catch (NumberFormatException e) {
            this.eventPrice.setValue(0);
            alertMessage.postValue("Please enter a valid price");
        }
    }

    // Number of participants
    private final MutableLiveData<Integer> eventMaxParticipants = new MutableLiveData<>();

    public MutableLiveData<Integer> getEventMaxParticipants() {
        return eventMaxParticipants;
    }

    public void setEventMaxParticipants(String eventMaxParticipantsStr) {
        try {
            int eventMaxParticipants = Integer.parseInt(eventMaxParticipantsStr);
            if (eventMaxParticipants < 1) {
                throw new NumberFormatException();
            }
            this.eventMaxParticipants.setValue(eventMaxParticipants);
        } catch (NumberFormatException e) {
            this.eventMaxParticipants.setValue(0);
            alertMessage.postValue("Please enter a valid number of participants");
        }
    }

    // Event name
    private final MutableLiveData<String> eventName = new MutableLiveData<>();
    public MutableLiveData<String> getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        if (eventName.isEmpty()) {
            alertMessage.postValue("Event name cannot be empty");
            this.eventName.setValue(eventName);
            return;
        }
        this.eventName.setValue(eventName);
    }

    // Event description
    private final MutableLiveData<String> eventDescription = new MutableLiveData<>();
    public MutableLiveData<String> getEventDescription() {
        return eventDescription;
    }
    public void setEventDescription(String eventDescription) {
        if (eventDescription.isEmpty()) {
            alertMessage.postValue("Event description cannot be empty");
            this.eventDescription.setValue(eventDescription);
            return;
        }
        this.eventDescription.setValue(eventDescription);
    }

    // Event duration
    private final MutableLiveData<String> eventDuration = new MutableLiveData<>();
    public MutableLiveData<String> getEventDuration() {
        return eventDuration;
    }
    public void setEventDuration(String eventDuration) {
        if (eventDuration.isEmpty()) {
            alertMessage.postValue("Event duration cannot be empty");
            this.eventDuration.setValue(eventDuration);
            return;
        }
        this.eventDuration.setValue(eventDuration);
    }

    // Event location
    private final MutableLiveData<String> eventLocation = new MutableLiveData<>();
    public MutableLiveData<String> getEventLocation() {
        return eventLocation;
    }
    public void setEventLocation(String eventLocation) {
        if (eventLocation.isEmpty()) {
            alertMessage.postValue("Event location cannot be empty");
            this.eventLocation.setValue(eventLocation);
            return;
        }
        this.eventLocation.setValue(eventLocation);
    }


    public void createEvent(String thumbnailFilePath, String creator, String type, String name, String description, String date, String time, String duration, String location, Double event_longitude, Double event_latitude, String languages, String maxParticipants, String price, String lastRegisterDate, String lastRegisterTime) {
//        Log.d("EventCreateViewModel Testing", "createEvent: " + thumbnailFilePath + " " + creator + " " + type + " " + name + " " + description + " " + date + " " + time + " " + duration + " " + location + " " + event_longitude + " " + event_latitude + " " + languages + " " + maxParticipants + " " + price + " " + lastRegisterDate + " " + lastRegisterTime);
        if (!areEventCreationInputsValid(type, name, description, languages, date, time, duration, location, maxParticipants, price, lastRegisterDate, lastRegisterTime)) {
            return;
        }
        eventRepository.createEvent(thumbnailFilePath, creator, type, name, description, date, time, duration, location, event_longitude, event_latitude, languages, maxParticipants, price, lastRegisterDate, lastRegisterTime, new CallbackInterface() {
            @Override
            public void onSuccess(Object result) {
                alertMessage.postValue(result.toString());
            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
            }
        });

    }

    public boolean areEventCreationInputsValid(String type, String name, String description, String languages, String date, String time, String duration, String location, String maxParticipants, String price, String lastRegisterDate, String lastRegisterTime) {
        if (name.isEmpty() || duration.isEmpty() || Integer.parseInt(price) == -1 || description.isEmpty() || location.isEmpty() || date.isEmpty() || time.isEmpty() || Integer.parseInt(maxParticipants) == -1 || type.isEmpty() || lastRegisterDate.isEmpty() || lastRegisterTime.isEmpty() || languages.isEmpty()) {
            String alert_msg = "Please fill in all required fields";
            alertMessage.postValue(alert_msg);
            return false;
        } else if (Integer.parseInt(maxParticipants) < 1) {
            // compare the string value of max_participants with 0
            String alert_msg = "Number of participants should be at least 1!";
            alertMessage.postValue(alert_msg);
            return false;
        }
        return true;
    }
}
