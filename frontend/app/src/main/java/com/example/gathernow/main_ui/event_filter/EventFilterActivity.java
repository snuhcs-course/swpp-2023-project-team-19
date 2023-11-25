package com.example.gathernow.main_ui.event_filter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private List<String> selectedLanguageChips;
    private List<String> selectedEventTypeChips;
    private List<String> selectedDateChips;
    private List<String> selectedTimeChips;
    boolean isFreeEvent;
    private String query;
    private RelativeLayout no_event_layout;
    public SharedPreferences preferences;
    private View rootView;
    private ImageView no_event_picture;
    private TextView no_event_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("EventFilterActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_filter);
        rootView = findViewById(android.R.id.content);
        eventFilterViewModel = new EventFilterViewModel(this);
    }

    @Override
    public void onResume() {
        Log.e("EventFilterActivity", "onResume");
        super.onResume();

        setContentView(R.layout.activity_event_filter);
        rootView = findViewById(android.R.id.content);
        eventFilterViewModel = new EventFilterViewModel(this);
        no_event_layout = rootView.findViewById(R.id.no_event_layout);

        setupFilterListener(rootView);
        // Retrieve filter data from the Intent
        Intent intent = getIntent();
        checkFilter(intent);
        query = getQueryString();

        eventFilterViewModel.getAlertMessage().observe(this, message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
        eventFilterViewModel.getFilteredEvents().observe(this, eventDataModels -> updateFilteredEventsUI(eventDataModels, findViewById(android.R.id.content)));
        eventFilterViewModel.fetchFilteredEvents(query);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EventFilterActivity.this, FragHome.class);
        startActivity(intent);
    }

    private void updateFilteredEventsUI(List<EventDataModel> eventDataList, View rootView){
        if(!eventDataList.isEmpty()){
            Log.d("FilterActivity", "Event data is not empty");
            no_event_layout.setVisibility(View.GONE);
            fetchEventsUI(eventDataList, rootView);
            Log.d("FilterActivity", "Show events!");
        }
        else{
            Log.d("FilterActivity", "Event data is empty");

        }
    }

    private void checkFilter(Intent intent) {
        if (intent != null) {
            isFreeEvent = intent.getBooleanExtra("isFreeEvent", false);
            selectedLanguageChips = intent.getStringArrayListExtra("selectedLanguageChips");
            selectedEventTypeChips = intent.getStringArrayListExtra("selectedEventTypeChips");
            selectedDateChips = intent.getStringArrayListExtra("selectedDateChips");
            selectedTimeChips = intent.getStringArrayListExtra("selectedTimeChips");

            // Save search information
            preferences = getSharedPreferences("SearchPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFreeEvent", isFreeEvent);
            editor.putString("selectedLanguageChips", selectedLanguageChips.toString());
            editor.apply();
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
            EventCardHelper.createEventCardList(this, eventDataList, eventCardContainer, userId, "filter");
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
