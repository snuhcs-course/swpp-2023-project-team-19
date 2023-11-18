package com.example.gathernow.main_ui.event_filter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.gathernow.EventCardView;
//import com.example.gathernow.EventData;
//import com.example.gathernow.EventInfo;
import com.example.gathernow.FilterFragment;
import com.example.gathernow.FragHome;
import com.example.gathernow.R;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_filter);

        RelativeLayout no_event_layout = findViewById(R.id.no_event_layout);

        // Retrieve filter data from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            boolean isFreeEvent = intent.getBooleanExtra("isFreeEvent", false);
            List<String> selectedLanguageChips = intent.getStringArrayListExtra("selectedLanguageChips");
            List<String> selectedEventTypeChips = intent.getStringArrayListExtra("selectedEventTypeChips");
            List<String> selectedDateChips = intent.getStringArrayListExtra("selectedDateChips");
            List<String> selectedTimeChips = intent.getStringArrayListExtra("selectedTimeChips");

            // TODO: implement filter feature here
            // if none of the filter is selected, just display all available events
            if(!isFreeEvent && selectedLanguageChips.isEmpty() && selectedEventTypeChips.isEmpty() && selectedDateChips.isEmpty() && selectedTimeChips.isEmpty()){
                Log.d("FilterActivity", "No filter is selected");
                // TODO: show all event cards here
                no_event_layout.setVisibility(View.GONE);
                //callAllEvents(getWindow().getDecorView().getRootView());
            }

            // use log to check
            Log.d("FilterActivity", "Is Free Event: " + isFreeEvent);

            if(!selectedLanguageChips.isEmpty()){
                Log.d("FilterActivity", "Selected Language Chips:");
                for (String chipId : selectedLanguageChips) {
                    Log.d("FilterActivity", "Chip ID: " + chipId);
                }
            }

            if(!selectedEventTypeChips.isEmpty()){ //selectedEventTypeChips != null
                Log.d("FilterActivity", "Selected EventType Chips:");
                for (String chipId : selectedEventTypeChips) {
                    Log.d("FilterActivity", "Chip ID: " + chipId);
                }
            }

            if(!selectedDateChips.isEmpty()){
                Log.d("FilterActivity", "Selected Date Chips:");
                for (String chipId : selectedDateChips) {
                    Log.d("FilterActivity", "Chip ID: " + chipId);
                }
            }

            if(!selectedTimeChips.isEmpty()){
                Log.d("FilterActivity", "Selected Time Chips:");
                for (String chipId : selectedTimeChips) {
                    Log.d("FilterActivity", "Chip ID: " + chipId);
                }
            }
        }

        ImageButton filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new FilterFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        // TODO: get the events based on selected keywords
        // if no event for selected filters, show sad frog; else show filtered events

        TextView go_home_text = (TextView) no_event_layout.findViewById(R.id.go_home_text);
        go_home_text.setPaintFlags(go_home_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        go_home_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FragHome.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /*
    private ServiceApi service;
    public void callAllEvents(View rootView){
        service = RetrofitClient.getClient().create(ServiceApi.class);
        service.getALlEvents().enqueue(new Callback<List<EventData>>() {
            @Override
            public void onResponse(Call<List<EventData>> call, Response<List<EventData>> response) {
                if (response.isSuccessful()) {
                    List<EventData> events_list = response.body();
                    LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);

                    Collections.reverse(events_list);

                    // Get the current date and time
                    Date currentDate = new Date(System.currentTimeMillis());

                    for (int i = 0; i < events_list.size(); i++) {

                        EventData currentEvent = events_list.get(i);
                        // Create Date and Time object from currentEvent.event_date
                        Date eventDate = Date.valueOf(currentEvent.event_date);
                        Time eventTime = Time.valueOf(currentEvent.event_time);

                        // Combine the Date and Time into a single Date object
                        long dateTimeMillis = eventDate.getTime() + eventTime.getTime();
                        Date eventDateTime = new Date(dateTimeMillis);

                        //Only display events that happens after the current datetime
                        if (eventDateTime.after(currentDate)) {
                            EventCardView newEventCard = new EventCardView(rootView.getContext(), null);

                            newEventCard.setEventName(currentEvent.event_title);
                            newEventCard.setEventPhoto(currentEvent.event_type, currentEvent.event_images);
                            newEventCard.setEventCapacity(currentEvent.event_num_joined, currentEvent.event_num_participants);
                            newEventCard.setEventLocation(currentEvent.event_location);
                            newEventCard.setEventLanguage(currentEvent.event_language);
                            newEventCard.setEventDateTime(Date.valueOf(currentEvent.event_date), Time.valueOf(currentEvent.event_time));

//                        Log.d("EventInfo Testing", "Clicked event id: " + events_list.get(i).event_id.toString());

                            // Add vertical padding to the newEventCard
                            int verticalPadding = (int) (10 * getResources().getDisplayMetrics().density); // 16dp converted to pixels
                            newEventCard.setPadding(newEventCard.getPaddingLeft(), verticalPadding, newEventCard.getPaddingRight(), verticalPadding);
                            eventCardContainer.addView(newEventCard);

                            newEventCard.setOnEventCardClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Handle the click event here
                                    //Toast.makeText(v.getContext(), "Event card clicked!", Toast.LENGTH_SHORT).show();

                                    // Send the user id to the EventInfo activity
                                    Intent intent = new Intent(v.getContext(), EventInfo.class);
                                    intent.putExtra("userId", getUserId(v.getContext()));
                                    intent.putExtra("eventId", currentEvent.event_id);

                                    startActivity(intent);
                                }

                                private String getUserId(Context context) {
                                    SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                                    return sharedPreferences.getString("user_id", null); // Return null if the user_id doesn't exist
                                }
                            });

                        }

                    }


                } else {
                    // Handle API error
                    // Handle the case where the response is not successful (e.g., non-2xx HTTP status)
                    Toast.makeText(rootView.getContext(), "Event display failed.", Toast.LENGTH_SHORT).show();

                    // Logging the error
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("EventDisplay", "Failed with response: " + errorBody);
                        } catch (IOException e) {
                            Log.e("EventDisplay", "Error while reading errorBody", e);
                        }
                    } else {
                        Log.e("EventDisplay", "Response not successful and error body is null");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EventData>> call, Throwable t) {
                Toast.makeText(rootView.getContext(), "Get Event Error", Toast.LENGTH_SHORT).show();
                Log.e("EventDisplay", "Error occurred", t);
            }
        });

    }

     */

}
