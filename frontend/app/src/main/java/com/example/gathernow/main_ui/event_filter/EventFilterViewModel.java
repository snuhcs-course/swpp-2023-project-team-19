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
    private final MutableLiveData<List<EventDataModel>> filteredEvents = new MutableLiveData<>();

    public EventFilterViewModel(Context context) {
        this.context = context;
        this.eventRepository = new EventRepository(new EventDataSource());
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public MutableLiveData<List<EventDataModel>> getFilteredEvents() {
        return filteredEvents;
    }

    public void fetchFilteredEvents(String query) {
        eventRepository.getFilteredEvents(query, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                Log.d("EventFilterViewModel", "On Success");

                if (result instanceof List<?>) {
                    List<?> resultList = (List<?>) result;
                    if (!resultList.isEmpty() && resultList.get(0) instanceof EventDataModel) {
                        List<EventDataModel> events = (List<EventDataModel>) resultList;
                        filteredEvents.setValue(events);
                        Log.d("EventFilterViewModel", "Event:"+events);
                    }
                    else{
                        filteredEvents.setValue(Collections.emptyList());
                    }
                }
                Log.d("EventFilterViewModel", "Filtered event found");
            }

            @Override
            public void onError(String message) {
                alertMessage.setValue(message);
                Log.d("EventFilterViewModel", "Filtered event not found");
                filteredEvents.setValue(Collections.emptyList());
            }
        });
    }

}
