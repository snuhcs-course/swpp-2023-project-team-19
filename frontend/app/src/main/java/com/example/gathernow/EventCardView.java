package com.example.gathernow;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.Reference;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class EventCardView extends LinearLayout {

    private ImageView event_photo;
    private TextView event_name, event_capacity, event_language, event_datetime, event_location;

    public EventCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setClickable(true);
        setFocusable(true);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.event_card_layout, this, true);

        // Initialize UI elements
        event_photo = findViewById(R.id.event_photo);
        event_name = findViewById(R.id.event_name);
        event_capacity = findViewById(R.id.event_capacity);
        event_language = findViewById(R.id.event_language);
        event_datetime = findViewById(R.id.event_datetime);
        event_location = findViewById(R.id.event_location);
    }

    public void setOnEventCardClickListener(OnClickListener listener) {
        setOnClickListener(listener);
    }

    // Setter methods to update the UI elements
    public void setEventPhoto(String event_type) {
        switch (event_type) {
            case "Leisure":
                event_photo.setImageResource(R.mipmap.ic_image1_leisure);
                break;
            case "Sports":
                event_photo.setImageResource(R.mipmap.ic_image2_sports_foreground);
                break;
            case "Workshops":
                event_photo.setImageResource(R.mipmap.ic_image3_workshops_foreground);
                break;
            case "Parties":
                event_photo.setImageResource(R.mipmap.ic_image4_parties_foreground);
                break;
            case "Cultural activities":
                event_photo.setImageResource(R.mipmap.ic_image5_cultural_activities_foreground);
                break;
            case "Others":
                event_photo.setImageResource(R.mipmap.ic_image6_others_foreground);
                break;
        }
    }

    public void setEventName(String name) {
        event_name.setText(name);
    }

    public void setEventLocation(String eventLocation) {
        event_location.setText(eventLocation);
    }

    public void setEventLanguage(String eventLanguage) {
        event_language.setText(eventLanguage);
    }

    public void setEventDateTime(Date event_date, Time event_time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");  // "a" will display AM or PM

        String formattedDate = dateFormat.format(event_date);
        String formattedTime = timeFormat.format(event_time);

        String combinedDateTime = formattedDate + "   " + formattedTime;

        event_datetime.setText(combinedDateTime);
    }

    public void setEventCapacity(Integer event_num_joined, Integer event_num_participants) {
        String eventCapacity = event_num_joined.toString() + "/" + event_num_participants.toString();
        event_capacity.setText(eventCapacity);
    }

}

