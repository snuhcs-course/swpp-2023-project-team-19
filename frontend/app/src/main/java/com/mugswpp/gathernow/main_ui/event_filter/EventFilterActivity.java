package com.mugswpp.gathernow.main_ui.event_filter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mugswpp.gathernow.FilterFragment;
import com.mugswpp.gathernow.R;
import com.mugswpp.gathernow.api.models.EventDataModel;
import com.mugswpp.gathernow.authenticate.UserLocalDataSource;
import com.mugswpp.gathernow.utils.EventCardHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class EventFilterActivity extends AppCompatActivity {

    EventFilterViewModel eventFilterViewModel;
    List<String> selectedLanguageChips = new ArrayList<>();
    List<String> selectedEventTypeChips = new ArrayList<>();
    List<String> selectedDateChips = new ArrayList<>();
    List<String> selectedTimeChips = new ArrayList<>();
    List<String> selectedEventLocation = new ArrayList<>();
    private String eventLocation;
    boolean isFreeEvent;
    private String query;

    RelativeLayout no_event_layout, loadingLayout;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_filter);
        View rootView = findViewById(android.R.id.content);
        eventFilterViewModel = new EventFilterViewModel(this);

        no_event_layout = findViewById(R.id.no_event_layout);
        loadingLayout = findViewById(R.id.loading_layout);
        scrollView = findViewById(R.id.scroll);
        no_event_layout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        setupFilterListener(rootView);

        // Retrieve filter data from the Intent
        Intent intent = getIntent();
        checkFilter(intent);
        query = getQueryString();

        eventFilterViewModel.getAlertMessage().observe(this, message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
        eventFilterViewModel.getFilteredEvents().observe(this, eventDataModels -> updateFilteredEventsUI(eventDataModels, rootView));
        eventFilterViewModel.fetchFilteredEvents(query);
    }

    void updateFilteredEventsUI(List<EventDataModel> eventDataList, View rootView){
        Log.d("FilterActivity", "Im here");
        if(!eventDataList.isEmpty()){
            no_event_layout.setVisibility(View.GONE);
            fetchEventsUI(eventDataList, rootView);
            Log.d("FilterActivity", "Show events!");
        }
        else{
            Log.d("FilterActivity", "Event data is null or empty");
            loadingLayout.setVisibility(View.GONE);
            no_event_layout.setVisibility(View.VISIBLE);
        }
    }

    void checkFilter(Intent intent) {
        if (intent != null) {
            isFreeEvent = intent.getBooleanExtra("isFreeEvent", false);
            selectedLanguageChips = intent.getStringArrayListExtra("selectedLanguageChips");
            selectedEventTypeChips = intent.getStringArrayListExtra("selectedEventTypeChips");
            selectedDateChips = intent.getStringArrayListExtra("selectedDateChips");
            selectedTimeChips = intent.getStringArrayListExtra("selectedTimeChips");
            selectedEventLocation = intent.getStringArrayListExtra("selectedEventLocation");
        }
    }

    // show event cards
    void fetchEventsUI(List<EventDataModel> eventDataList, View rootView) {
        //get current login user id
        UserLocalDataSource userLocalDataSource = new UserLocalDataSource(this);
        int userId = Integer.parseInt(userLocalDataSource.getUserId());
        Log.d("FilterActivity", "eventDataList " + eventDataList);

        if (eventDataList == null || eventDataList.isEmpty()){
            Log.d("EventFilterActivity", "Event data is null or empty");
            loadingLayout.setVisibility(View.GONE);
            no_event_layout.setVisibility(View.VISIBLE);
        } else {
            Log.d("EventFilterActivity", "Loaded user events");
            LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);
            EventCardHelper.createEventCardList(this, eventDataList, eventCardContainer, userId, "filter");
            loadingLayout.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }

    }

    void setupFilterListener(View rootView) {
        ImageButton filterButton = rootView.findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new FilterFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }

    String getQueryString(){
        StringBuilder query = new StringBuilder();

        // Add is_free parameter
        if(isFreeEvent){ // if is_free_only is true, add is_free=true to query
            query.append("?is_free=true");
        } else { // if is_free_only is false, by default it shows all events, so no need to add is_free to query
            query.append("?");
        }
        //query.append("?is_free=").append(isFreeEvent);

        // "Others" in languages should include: Uzbek, Khmer, Filipino, Nepali, Kazakh, Mongolian, Burmese, Portuguese, Hindi, Arabic, Bengali, Urdu, Turkish
        for (int i = 0; i < selectedLanguageChips.size(); i++) {
            if (selectedLanguageChips.get(i).equals("Others")) {
                selectedLanguageChips.set(i, "Other");
                selectedLanguageChips.add("Uzbek");
                selectedLanguageChips.add("Khmer");
                selectedLanguageChips.add("Filipino");
                selectedLanguageChips.add("Nepali");
                selectedLanguageChips.add("Kazakh");
                selectedLanguageChips.add("Mongolian");
                selectedLanguageChips.add("Burmese");
                selectedLanguageChips.add("Portuguese");
                selectedLanguageChips.add("Hindi");
                selectedLanguageChips.add("Arabic");
                selectedLanguageChips.add("Bengali");
                selectedLanguageChips.add("Urdu");
                selectedLanguageChips.add("Turkish");
            }
        }

        // Add "&" if there are additional parameters
        if ((selectedLanguageChips != null && !selectedLanguageChips.isEmpty()) || (selectedEventTypeChips != null && !selectedEventTypeChips.isEmpty()) ||
                (selectedDateChips != null && !selectedDateChips.isEmpty()) || (selectedTimeChips != null && !selectedTimeChips.isEmpty())
                || (selectedEventLocation != null && !selectedEventLocation.isEmpty())) {
            query.append("&");
        }

        // Helper method to append formatted strings
        appendParameterList(query, "language", selectedLanguageChips);
        appendParameterList(query, "event_type", selectedEventTypeChips);
        appendParameterList(query, "date", selectedDateChips);
        appendParameterList(query, "time", selectedTimeChips);
        appendParameterList(query, "location_address", selectedEventLocation);
        return query.toString();
    }

    private static void appendParameterList(StringBuilder query, String parameterName, List<String> values) {
        if (values != null) {
            for (String value : values) {
                query.append(parameterName).append("=").append(value).append("&");
            }
        } else {
            query.append(parameterName).append("=").append("&");
        }
    }

}
