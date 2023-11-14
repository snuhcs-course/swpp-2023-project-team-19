package com.example.gathernow.main_ui.event_creation;

import com.example.gathernow.main_ui.EventCallback;

public class EventRepository {
    private final EventDataSource eventDataSource;

    public EventRepository(EventDataSource eventDataSource) {
        this.eventDataSource = eventDataSource;

    }
    public void createEvent(String thumbnailFilePath, String creator, String type, String name, String description, String date, String time, String duration, String location, String languages, String maxParticipants, String price, String lastRegisterDate, String lastRegisterTime, EventCallback eventCallback) {
        eventDataSource.createEvent(thumbnailFilePath, creator, type, name, description, date, time, duration, location, languages, maxParticipants, price, lastRegisterDate, lastRegisterTime, eventCallback);
    }
}
