package com.example.gathernow.main_ui.events;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.api.models.UserDataModel;
import com.example.gathernow.authenticate.UserLocalDataSource;
import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventDataSource;
import com.example.gathernow.main_ui.EventRepository;
import com.example.gathernow.main_ui.UserRemoteDataSource;
import com.example.gathernow.main_ui.UserRemoteRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EventsViewModel extends ViewModel {

    private EventRepository eventRepository;
    private Context context;
    private int userId;
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final MutableLiveData<List<ApplicationDataModel>> userAppliedEvents = new MutableLiveData<>();
    private final MutableLiveData<List<ApplicationDataModel>> pendingApplications = new MutableLiveData<>();
    private final MutableLiveData<List<ApplicationDataModel>> confirmedApplications = new MutableLiveData<>();
    private final MutableLiveData<List<EventDataModel>> pendingEvents = new MutableLiveData<>();
    private final MutableLiveData<List<EventDataModel>> confirmedEvents = new MutableLiveData<>();

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

    public MutableLiveData<List<ApplicationDataModel>> getPendingApplications() {
        return pendingApplications;
    }

    public MutableLiveData<List<ApplicationDataModel>> getConfirmedApplications() {
        return confirmedApplications;
    }

    public MutableLiveData<List<EventDataModel>> getConfirmedEvents() {
        return confirmedEvents;
    }

    public MutableLiveData<List<EventDataModel>> getPendingEvents() {
        return pendingEvents;
    }

    public void fetchUserAppliedEvents() {

        //get current login user id
        UserLocalDataSource userLocalDataSource = new UserLocalDataSource(this.context);
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
                        userAppliedEvents.setValue(events);
                        Log.e("EventsViewModel", "About to loop through events");

                        // Categorize events
                        //List<ApplicationDataModel> pendingList = new ArrayList<>();
                        //List<ApplicationDataModel> confirmedList = new ArrayList<>();
                        for (ApplicationDataModel event : events) {
                            Log.e("EventsViewModel", "loop through events");

                            int status = event.getRequestStatus();
                            if (status == 0) { // Pending events
                                //pendingList.add(event);
                                loadEventInfo(event.getEventId(),true);
                                Log.e("EventsViewModel", "Str: "+pendingEvents.toString());
                            } else {
                                //confirmedList.add(event);
                                loadEventInfo(event.getEventId(),false);
                                Log.e("EventsViewModel", "Str: "+confirmedEvents.toString());
                            }
                        }
                        //pendingApplications.setValue(pendingList);
                        //confirmedApplications.setValue(confirmedList);
                        Log.e("EventsViewModel", pendingEvents.toString());
                        Log.e("EventsViewModel", confirmedEvents.toString());

                        //categorizeEventsBasedOnApplications();
                    }
                    else{
                        pendingEvents.setValue(new ArrayList<>());
                        confirmedEvents.setValue(new ArrayList<>());
                    }
                }
            }
            @Override
            public void onError(String message) {
                alertMessage.setValue(message);
            }
        });
    }
       /*

    public void fetchEventLists() {
        fetchEventDetails(Objects.requireNonNull(pendingApplications.getValue()), pendingEvents);
        fetchEventDetails(Objects.requireNonNull(confirmedApplications.getValue()), confirmedEvents);
    }

    private void fetchEventDetails(List<ApplicationDataModel> applications, MutableLiveData<List<EventDataModel>> eventList) {
        List<EventDataModel> events = new ArrayList<>();
        for (ApplicationDataModel application : applications) {
            int eventId = application.getEventId();
            loadEventInfo(eventId, events, eventList);
        }
    }

     */




    private void categorizeEventsBasedOnApplications() {
        // Fetch the values on the main thread
        List<ApplicationDataModel> pendingApps = pendingApplications.getValue();
        List<ApplicationDataModel> confirmedApps = confirmedApplications.getValue();

        // Now pass these lists to your background task
        processApplications(pendingApps, true); // true for pending
        processApplications(confirmedApps, false); // false for confirmed
    }

    private void processApplications(List<ApplicationDataModel> applications, boolean isPending) {
        // This method can be run on a background thread
        for (ApplicationDataModel app : applications) {
            // Note: Ensure that this method call is thread-safe
            loadEventInfo(app.getEventId(), isPending);
        }
    }

    private void loadEventInfo(int eventId, boolean isPending) {
        eventRepository.getEventInfo(eventId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                Log.d("EventInfoViewModel", "Load event info successfully");
                EventDataModel event = (EventDataModel) result;

                if (event != null) {
                    Log.e("EventInfoViewModel", "Event is not null");
                    // Post the event to the correct LiveData
                    if (isPending) {
                        // Add to pending events list and post value
                        // Ensure this is done on the main thread
                        runOnUiThread(() -> {
                            List<EventDataModel> events = pendingEvents.getValue();
                            if (events == null) {
                                events = new ArrayList<>();
                            }
                            events.add(event);
                            pendingEvents.setValue(events);
                        });
                    } else {
                        // Add to confirmed events list and post value
                        // Ensure this is done on the main thread
                        runOnUiThread(() -> {
                            List<EventDataModel> events = confirmedEvents.getValue();
                            if (events == null) {
                                events = new ArrayList<>();
                            }
                            events.add(event);
                            confirmedEvents.setValue(events);
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
    private void runOnUiThread(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }



}
