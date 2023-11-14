package com.example.gathernow.main_ui.event_creation;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

public class EventCreateViewModelFactory implements ViewModelProvider.Factory {
    private final EventRepository eventRepository;
    private final Context context;

    public EventCreateViewModelFactory(Context context, EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.context = context;
    }

    @Override
    public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EventCreateViewModel.class)) {
            return (T) new EventCreateViewModel(context, eventRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
