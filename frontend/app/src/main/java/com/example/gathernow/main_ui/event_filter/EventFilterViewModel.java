package com.example.gathernow.main_ui.event_filter;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventDataSource;
import com.example.gathernow.main_ui.EventRepository;

import java.util.Collections;
import java.util.List;

public class EventFilterViewModel extends ViewModel {
    private EventRepository eventRepository;
    private Context context;
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final MutableLiveData<List<EventDataModel>> allEvents = new MutableLiveData<>();

    public EventFilterViewModel(Context context) {
        this.context = context;
        this.eventRepository = new EventRepository(new EventDataSource());
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public MutableLiveData<List<EventDataModel>> getAllEvents() {
        return allEvents;
    }

    public void fetchAllEvents() {
        eventRepository.getAllEvents(new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                Log.d("EventFilterViewModel", "Loaded all events successfully");
                if (result instanceof List<?>) {
                    List<?> resultList = (List<?>) result;
                    if (!resultList.isEmpty() && resultList.get(0) instanceof EventDataModel) {
                        List<EventDataModel> events = (List<EventDataModel>) resultList;
                        Collections.reverse(events);
                        allEvents.setValue(events);
                    }
                }
            }

            @Override
            public void onError(String message) {
                alertMessage.setValue(message);
            }
        });
    }


}
