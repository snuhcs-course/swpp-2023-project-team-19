package com.example.gathernow.main_ui;

import com.example.gathernow.api.models.ApplicationDataModel;

public class EventRepository {
    private final EventDataSource eventDataSource;

    public EventRepository(EventDataSource eventDataSource) {
        this.eventDataSource = eventDataSource;

    }
    public void createEvent(String thumbnailFilePath, String creator, String type, String name, String description, String date, String time, String duration, String location, Double event_longitude, Double event_latitude, String languages, String maxParticipants, String price, String lastRegisterDate, String lastRegisterTime, CallbackInterface eventCallback) {
        eventDataSource.createEvent(thumbnailFilePath, creator, type, name, description, date, time, duration, location, event_longitude, event_latitude, languages, maxParticipants, price, lastRegisterDate, lastRegisterTime, eventCallback);
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
        eventDataSource.deleteApplication(applicationId,  applicationCallback);
    }

    public void decreaseNumJoined(int eventId, CallbackInterface eventCallback) {
        eventDataSource.decreaseNumJoined(eventId, eventCallback);
    }

    public void increaseNumJoined(int eventId, CallbackInterface eventCallback) {
        eventDataSource.increaseNumJoined(eventId, eventCallback);
    }

    public void getUserEvents(int userId, CallbackInterface eventCallback) {
        eventDataSource.getUserEvents(userId, eventCallback);
    }

    public void getEventApplication(int eventId, CallbackInterface eventCallback) {
        eventDataSource.getEventApplication(eventId, eventCallback);
    }

    public void getAllEvents(CallbackInterface eventCallback) {
        eventDataSource.getAllEvents(eventCallback);
    }

    public void getUserAppliedEvents(int userId, CallbackInterface eventCallback) {
        eventDataSource.getUserAppliedEvents(userId, eventCallback);
    }

    public void applyEvent(ApplicationDataModel applicationDataModel, CallbackInterface callbackInterface) {
        eventDataSource.applyEvent(applicationDataModel, callbackInterface);
    }

    public void acceptEventApplication(int applicationId, int status, CallbackInterface callbackInterface) {
        eventDataSource.acceptEventApplication(applicationId, status, callbackInterface);
    }

    public void rejectEventApplication(int applicationId, CallbackInterface callbackInterface) {
        eventDataSource.rejectEventApplication(applicationId, callbackInterface);
    }

    public void getFilteredEvents(String query, CallbackInterface callbackInterface) {
        eventDataSource.getFilteredEvents(query, callbackInterface);
    }

    public void getSearchedEvents(String query, CallbackInterface callbackInterface) {
        eventDataSource.getSearchedEvents(query, callbackInterface);
    }
}
