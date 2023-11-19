package com.example.gathernow.api.models;

import com.google.gson.annotations.SerializedName;

public class EventDataModel {
    @SerializedName("event_type")
    String event_type;

    @SerializedName("event_title")
    String event_title;

    @SerializedName("event_num_participants")
    Integer event_num_participants;

    @SerializedName("event_date")
    String event_date;

    @SerializedName("event_time")
    String event_time;

    @SerializedName("event_duration")
    String event_duration;

    @SerializedName("event_language")
    String event_language;

    @SerializedName("event_price")
    Integer event_price;

    @SerializedName("event_location")
    String event_location;

    @SerializedName("event_longitude")
    Double event_longitude;

    @SerializedName("event_latitude")
    Double event_latitude;

    @SerializedName("event_description")
    String event_description;

    @SerializedName("event_num_joined")
    Integer event_num_joined;

    @SerializedName("host_id")
    Integer host_id;

    @SerializedName("event_id")
    Integer event_id;

    @SerializedName("event_register_date")
    String event_register_date;

    @SerializedName("event_register_time")
    String event_register_time;

    @SerializedName("event_images")
    String event_images;

    // Create event
    public EventDataModel(String event_type, String event_title, Integer event_num_participants, String event_date, String event_time, String event_duration, String event_language, Integer event_price, String event_location, Double event_longitude, Double event_latitude, String event_description, Integer event_num_joined, Integer host_id, String event_register_date, String event_register_time){
        this.event_type = event_type;
        this.event_title = event_title;
        this.event_num_participants = event_num_participants;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_duration = event_duration;
        this.event_language = event_language;
        this.event_price = event_price;
        this.event_location = event_location;
        this.event_longitude = event_longitude;
        this.event_latitude = event_latitude;
        this.event_description = event_description;
        this.event_num_joined = event_num_joined;
        this.host_id = host_id;
        this.event_register_date = event_register_date;
        this.event_register_time = event_register_time;
    }

    // Query event data
    public EventDataModel(String event_type, String event_title, Integer event_num_participants, String event_date, String event_time, String event_duration, String event_language, Integer event_price, String event_location, Double event_longitude, Double event_latitude, String event_description, Integer event_num_joined, Integer host_id, Integer event_id, String event_register_date, String event_register_time, String event_images){
        this.event_id = event_id;
        this.event_type = event_type;
        this.event_title = event_title;
        this.event_num_participants = event_num_participants;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_duration = event_duration;
        this.event_language = event_language;
        this.event_price = event_price;
        this.event_location = event_location;
        this.event_longitude = event_longitude;
        this.event_description = event_description;
        this.event_num_joined = event_num_joined;
        this.host_id = host_id;
        this.event_register_date = event_register_date;
        this.event_register_time = event_register_time;
        this.event_images = event_images;
    }

    public String getEventType() {
        return event_type;
    }

    public String getEventTitle() {
        return event_title;
    }

    public Integer getEventNumParticipants() {
        return event_num_participants;
    }

    public String getEventDate() {
        return event_date;
    }

    public String getEventTime() {
        return event_time;
    }

    public String getEventDuration() {
        return event_duration;
    }

    public String getEventLanguage() {
        return event_language;
    }

    public Integer getEventPrice() {
        return event_price;
    }

    public String getEventLocation() {
        return event_location;
    }

    public String getEventDescription() {
        return event_description;
    }

    public Integer getEventNumJoined() {
        return event_num_joined;
    }

    public Integer getHostId() {
        return host_id;
    }

    public Integer getEventId() {
        return event_id;
    }

    public String getEventRegisterDate() {
        return event_register_date;
    }

    public String getEventRegisterTime() {
        return event_register_time;
    }

    public String getEventImages() {
        return event_images;
    }

    public Double getEventLongitude() {
        return event_longitude;
    }

    public Double getEventLatitude() {
        return event_latitude;
    }

}
