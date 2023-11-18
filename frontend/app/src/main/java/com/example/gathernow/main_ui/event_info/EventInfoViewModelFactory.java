package com.example.gathernow.main_ui.event_info;

import androidx.lifecycle.ViewModelProvider;

import com.example.gathernow.main_ui.EventRepository;

public class EventInfoViewModelFactory implements ViewModelProvider.Factory{
    private final EventRepository eventInfoRepository;
    public EventInfoViewModelFactory(EventRepository eventInfoRepository) {
        this.eventInfoRepository = eventInfoRepository;
    }

    @Override
    public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EventInfoViewModel.class)) {
            return (T) new EventInfoViewModel(eventInfoRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
