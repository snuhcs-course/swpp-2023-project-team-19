package com.example.gathernow;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.sql.Time;

public class EventData {
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

    @SerializedName("event_description")
    String event_description;

    @SerializedName("event_num_joined")
    Integer event_num_joined;

    @SerializedName("host_id")
    Integer host_id;

    @SerializedName("event_id")
    Integer event_id;

    // Create event
    public EventData(String event_type, String event_title, Integer event_num_participants, String event_date, String event_time, String event_duration, String event_language, Integer event_price, String event_location, String event_description, Integer event_num_joined, Integer host_id){
        this.event_type = event_type;
        this.event_title = event_title;
        this.event_num_participants = event_num_participants;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_duration = event_duration;
        this.event_language = event_language;
        this.event_price = event_price;
        this.event_location = event_location;
        this.event_description = event_description;
        this.event_num_joined = event_num_joined;
        this.host_id = host_id;
    }

    // Query event data
    public EventData(String event_type, String event_title, Integer event_num_participants, String event_date, String event_time, String event_duration, String event_language, Integer event_price, String event_location, String event_description, Integer event_num_joined, Integer host_id, Integer event_id){
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
        this.event_description = event_description;
        this.event_num_joined = event_num_joined;
        this.host_id = host_id;
    }

}
