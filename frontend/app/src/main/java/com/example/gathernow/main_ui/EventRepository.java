package com.example.gathernow.main_ui;

public class EventRepository {
    private final EventDataSource eventDataSource;

    public EventRepository(EventDataSource eventDataSource) {
        this.eventDataSource = eventDataSource;

    }
    public void createEvent(String thumbnailFilePath, String creator, String type, String name, String description, String date, String time, String duration, String location, String languages, String maxParticipants, String price, String lastRegisterDate, String lastRegisterTime, CallbackInterface eventCallback) {
        eventDataSource.createEvent(thumbnailFilePath, creator, type, name, description, date, time, duration, location, languages, maxParticipants, price, lastRegisterDate, lastRegisterTime, eventCallback);
    }

    public void getEventInfo(int eventId, CallbackInterface eventCallback) {
        eventDataSource.getEventInfo(eventId, eventCallback);
    }

    public void checkUserAppliedEvent(int eventId, int userId, CallbackInterface eventCallback) {
        eventDataSource.checkUserAppliedEvent(eventId, userId, eventCallback);
    }

    public void deleteEvent(int eventId, CallbackInterface eventCallback) {
        eventDataSource.deleteEvent(eventId, eventCallback);
    }

    public void deleteApplication(int applicationId, CallbackInterface applicationCallback) {
        eventDataSource.deleteApplication(applicationId, applicationCallback);
    }
}
