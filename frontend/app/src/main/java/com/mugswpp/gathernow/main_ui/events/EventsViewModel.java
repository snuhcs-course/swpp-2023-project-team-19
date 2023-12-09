package com.mugswpp.gathernow.main_ui.events;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mugswpp.gathernow.api.models.ApplicationDataModel;
import com.mugswpp.gathernow.api.models.EventDataModel;
import com.mugswpp.gathernow.authenticate.UserLocalDataSource;
import com.mugswpp.gathernow.main_ui.CallbackInterface;
import com.mugswpp.gathernow.main_ui.EventDataSource;
import com.mugswpp.gathernow.main_ui.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventsViewModel extends ViewModel {

    public EventRepository eventRepository;
    private Context context;
    private int userId;
    public MutableLiveData<String> alertMessage = new MutableLiveData<>();
    public MutableLiveData<List<ApplicationDataModel>> userAppliedEvents = new MutableLiveData<>();
    public MutableLiveData<List<ApplicationDataModel>> pendingApplications = new MutableLiveData<>();
    public MutableLiveData<List<ApplicationDataModel>> confirmedApplications = new MutableLiveData<>();
    public MutableLiveData<List<EventDataModel>> pendingEvents = new MutableLiveData<>();
    public MutableLiveData<List<EventDataModel>> confirmedEvents = new MutableLiveData<>();

    public EventsViewModel(Context context) {
        this.context = context;
        this.eventRepository = new EventRepository(new EventDataSource());
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public MutableLiveData<List<ApplicationDataModel>> getAllEvents() {
        return userAppliedEvents;
    }

    public MutableLiveData<List<EventDataModel>> getConfirmedEvents() {
        return confirmedEvents;
    }

    public MutableLiveData<List<EventDataModel>> getPendingEvents() {
        return pendingEvents;
    }

    public void fetchUserAppliedEvents(UserLocalDataSource userLocalDataSource) {

        //get current login user id
        //UserLocalDataSource userLocalDataSource = new UserLocalDataSource(this.context);
        userId = Integer.valueOf(userLocalDataSource.getUserId());
        eventRepository.getUserAppliedEvents(userId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                Log.e("EventsViewModel", "Loaded all events successfully");
                if (result instanceof List<?>) {
                    List<?> resultList = (List<?>) result;
                    Log.e("EventsViewModel", "1st point");
                    if (!resultList.isEmpty() && resultList.get(0) instanceof ApplicationDataModel) {
                        Log.e("EventsViewModel", "2nd point");
                        List<ApplicationDataModel> events = (List<ApplicationDataModel>) resultList;
                        Collections.reverse(events);
                        userAppliedEvents.postValue(events);
                        Log.e("EventsViewModel", "About to loop through events");

                        // Categorize events
                        for (ApplicationDataModel event : events) {
                            Log.e("EventsViewModel", "loop through events");

                            int status = event.getRequestStatus();
                            if (status == 0) { // Pending events
                                //pendingList.add(event);
                                loadEventInfo(event.getEventId(),0);
                                Log.e("EventsViewModel", "Str: "+pendingEvents.toString());
                            } else if (status == 1){
                                //confirmedList.add(event);
                                loadEventInfo(event.getEventId(),1);
                                Log.e("EventsViewModel", "Str: "+confirmedEvents.toString());
                            }
                            else{
                                Log.e("EventsViewModel", "Rejected events");
                                loadEventInfo(event.getEventId(),2);
                            }

                        }

                        Log.e("EventsViewModel", pendingEvents.toString());
                        Log.e("EventsViewModel", confirmedEvents.toString());

                    }
                    else{
                        pendingEvents.postValue(new ArrayList<>());
                        confirmedEvents.postValue(new ArrayList<>());
                    }
                }
            }
            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
            }
        });
    }


    void loadEventInfo(int eventId, int isPending) {
        eventRepository.getEventInfo(eventId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                Log.d("EventInfoViewModel", "Load event info successfully");
                EventDataModel event = (EventDataModel) result;

                if (event != null) {
                    Log.e("EventInfoViewModel", "Event is not null");
                    // Post the event to the correct LiveData
                    if (isPending == 0) {
                        // Add to pending events list and post value
                        // Ensure this is done on the main thread
                        runOnUiThread(() -> {
                            List<EventDataModel> events = pendingEvents.getValue();
                            if (events == null) {
                                events = new ArrayList<>();
                            }
                            events.add(event);
                            pendingEvents.postValue(events);
                        });
                    } else if (isPending == 1){
                        // Add to confirmed events list and post value
                        // Ensure this is done on the main thread
                        runOnUiThread(() -> {
                            List<EventDataModel> events = confirmedEvents.getValue();
                            if (events == null) {
                                events = new ArrayList<>();
                            }
                            events.add(event);
                            confirmedEvents.postValue(events);
                        });
                    }
                    else{
                        runOnUiThread(() -> {
                            List<EventDataModel> events = pendingEvents.getValue();
                            if (events == null) {
                                events = new ArrayList<>();
                            }
                            //events.add(event);
                            pendingEvents.postValue(events);
                        });
                    }
                }
                else{
                    Log.e("EventInfoViewModel", "Event is null");
                }

            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);

            }
        });
    }

    // Utility method to ensure code runs on the UI thread
    public void runOnUiThread(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}
