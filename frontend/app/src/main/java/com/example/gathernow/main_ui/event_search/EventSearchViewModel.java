package com.example.gathernow.main_ui.event_search;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventDataSource;
import com.example.gathernow.main_ui.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventSearchViewModel extends ViewModel {
    private EventRepository eventRepository;
    private Context context;
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final MutableLiveData<List<EventDataModel>> searchedEvents = new MutableLiveData<>();

    public EventSearchViewModel(Context context) {
        this.context = context;
        this.eventRepository = new EventRepository(new EventDataSource());
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public MutableLiveData<List<EventDataModel>> returnSearchedEvents() {
        return searchedEvents;
    }

    public void fetchSearchedEvents(String query) {
        eventRepository.getSearchedEvents(query, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                if (result instanceof List<?>) {
                    List<?> resultList = (List<?>) result;
                    Log.d("EventSearchedViewModel", result.toString());
                    if (!resultList.isEmpty() && resultList.get(0) instanceof EventDataModel) {
                        List<EventDataModel> events = (List<EventDataModel>) resultList;
                        searchedEvents.setValue(events);
                    } else {
                        searchedEvents.setValue(new ArrayList<>());
                    }
                }

            }

            @Override
            public void onError(String message) {
                alertMessage.setValue(message);
                Log.d("EventSearchedViewModel", "Searched event not found");
            }
        });
    }

}

