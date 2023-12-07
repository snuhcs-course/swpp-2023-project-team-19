package com.mugswpp.gathernow.api.models;

public class EventDataModelBuilder {
    // builder for EventDataModel
    private EventDataModel eventDataModel;
    public EventDataModelBuilder() {
        eventDataModel = new EventDataModel();
    }

    public EventDataModelBuilder setEventType(String event_type) {
        eventDataModel.setEventType(event_type);
        return this;
    }

    public EventDataModelBuilder setEventTitle(String event_title) {
        eventDataModel.setEventTitle(event_title);
        return this;
    }

    public EventDataModelBuilder setEventMaxParticipants(Integer event_num_participants) {
        eventDataModel.setEventNumParticipants(event_num_participants);
        return this;
    }

    public EventDataModelBuilder setEventJoined(Integer event_num_joined) {
        eventDataModel.setEventNumJoined(event_num_joined);
        return this;
    }

    public EventDataModelBuilder setEventDate(String event_date) {
        eventDataModel.setEventDate(event_date);
        return this;
    }

    public EventDataModelBuilder setEventTime(String event_time) {
        eventDataModel.setEventTime(event_time);
        return this;
    }

    public EventDataModelBuilder setEventDuration(String event_duration) {
        eventDataModel.setEventDuration(event_duration);
        return this;
    }

    public EventDataModelBuilder setEventLanguage(String event_language) {
        eventDataModel.setEventLanguage(event_language);
        return this;
    }

    public EventDataModelBuilder setEventPrice(Integer event_price) {
        eventDataModel.setEventPrice(event_price);
        return this;
    }

    public EventDataModelBuilder setEventLocation(String event_location) {
        eventDataModel.setEventLocation(event_location);
        return this;
    }

    public EventDataModelBuilder setEventLongitude(Double event_longitude) {
        eventDataModel.setEventLongitude(event_longitude);
        return this;
    }

    public EventDataModelBuilder setEventLatitude(Double event_latitude) {
        eventDataModel.setEventLatitude(event_latitude);
        return this;
    }

    public EventDataModelBuilder setEventDescription(String event_description) {
        eventDataModel.setEventDescription(event_description);
        return this;
    }

    public EventDataModelBuilder setHostId(Integer host_id) {
        eventDataModel.setHostId(host_id);
        return this;
    }

    public EventDataModelBuilder setEventId(Integer event_id) {
        eventDataModel.setEventId(event_id);
        return this;
    }

    public EventDataModelBuilder setEventRegisterDate(String event_register_date) {
        eventDataModel.setEventRegisterDate(event_register_date);
        return this;
    }

    public EventDataModelBuilder setEventRegisterTime(String event_register_time) {
        eventDataModel.setEventRegisterTime(event_register_time);
        return this;
    }

    public EventDataModelBuilder setEventImages(String event_images) {
        eventDataModel.setEventImages(event_images);
        return this;
    }

    public EventDataModel build() {
        return eventDataModel;
    }
}
