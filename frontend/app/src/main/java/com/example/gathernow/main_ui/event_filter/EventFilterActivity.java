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
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.authenticate.UserLocalDataSource;
import com.example.gathernow.main_ui.home.HomeViewModel;
import com.example.gathernow.utils.EventCardHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventFilterActivity extends AppCompatActivity {

    private EventFilterViewModel eventFilterViewModel;
    List<String> selectedLanguageChips;
    List<String> selectedEventTypeChips;
    List<String> selectedDateChips;
    List<String> selectedTimeChips;
    boolean isFreeEvent;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_filter);
        View rootView = findViewById(android.R.id.content);
        eventFilterViewModel = new EventFilterViewModel(this);

        RelativeLayout no_event_layout = findViewById(R.id.no_event_layout);

        setupFilterListener(rootView);

        // Retrieve filter data from the Intent
        Intent intent = getIntent();
        checkFilter(intent);
        query = getQueryString();
        List<EventDataModel> filtered_event_list = new ArrayList<>();


        // TODO: return filter results (put in event card)
        // filtered_event_list: fetch filtered event list
        // query: filter lists, send to API

        // if there exist events related with selected filters
        if(!filtered_event_list.isEmpty()){
            no_event_layout.setVisibility(View.GONE);
            fetchEventsUI(filtered_event_list, rootView);
            Log.d("FilterActivity", "Show events!");
        } else { // if there is no related events
            Log.d("FilterActivity", "No related events :(");
            updateBlankUI(rootView);
        }
    }

    private void checkFilter(Intent intent) {
        if (intent != null) {
            isFreeEvent = intent.getBooleanExtra("isFreeEvent", false);
            selectedLanguageChips = intent.getStringArrayListExtra("selectedLanguageChips");
            selectedEventTypeChips = intent.getStringArrayListExtra("selectedEventTypeChips");
            selectedDateChips = intent.getStringArrayListExtra("selectedDateChips");
            selectedTimeChips = intent.getStringArrayListExtra("selectedTimeChips");
        }
    }

    // show event cards
    private void fetchEventsUI(List<EventDataModel> eventDataList, View rootView) {
        //get current login user id
        UserLocalDataSource userLocalDataSource = new UserLocalDataSource(this);
        int userId = Integer.parseInt(userLocalDataSource.getUserId());

        if (eventDataList == null || eventDataList.isEmpty()){
            Log.d("EventFilterActivity", "Event data is null or empty");
        } else {
            Log.d("EventFilterActivity", "Loaded user events");
            LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);
            EventCardHelper.createEventCardList(this, eventDataList, eventCardContainer, userId);
        }

    }

    private void setupFilterListener(View rootView) {
        ImageButton filterButton = rootView.findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new FilterFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }

    // call sad frog
    private void updateBlankUI(View rootView) {
        RelativeLayout no_event_layout = rootView.findViewById(R.id.no_event_layout);
        no_event_layout.setVisibility(View.VISIBLE);

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

    private String getQueryString(){
        StringBuilder query = new StringBuilder();

        // Add is_free parameter
        query.append("?is_free=").append(isFreeEvent);

        // Add "&" if there are additional parameters
        if (!selectedLanguageChips.isEmpty() || !selectedEventTypeChips.isEmpty() ||
                !selectedDateChips.isEmpty() || !selectedTimeChips.isEmpty()) {
            query.append("&");
        }

        // Helper method to append formatted strings
        appendParameterList(query, "language", selectedLanguageChips);
        appendParameterList(query, "event_type", selectedEventTypeChips);
        appendParameterList(query, "date", selectedDateChips);
        appendParameterList(query, "time", selectedTimeChips);

        // Remove the trailing "&" if the query is not empty
        if (query.length() > 0) {
            query.deleteCharAt(query.length() - 1);
        }
        Log.d("FilterActivity", "Query: " + query.toString());
        return query.toString();
    }

    private static void appendParameterList(StringBuilder query, String parameterName, List<String> values) {
        for (String value : values) {
            query.append(parameterName).append("=").append(value).append("&");
        }
    }


    /*
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
     */
}
