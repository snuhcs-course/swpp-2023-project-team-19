package com.example.gathernow.main_ui.event_search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gathernow.FragHome;
import com.example.gathernow.R;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.authenticate.UserLocalDataSource;
import com.example.gathernow.main_ui.event_filter.EventFilterViewModel;
import com.example.gathernow.utils.EventCardHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class EventSearchActivity extends AppCompatActivity {

    private EventSearchViewModel eventSearchViewModel;
    private TextInputEditText searchBar;
    private String query;
    private RelativeLayout no_event_layout;
    private  LinearLayout eventCardContainer;
    private View rootView;
    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("EventSearchedActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);
        rootView = findViewById(android.R.id.content);
        eventSearchViewModel = new EventSearchViewModel(this);
        no_event_layout = findViewById(R.id.no_event_layout);
        eventCardContainer = findViewById(R.id.eventCardContainer);

        searchBar = findViewById(R.id.search_bar);
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                // User pressed "Done" on the keyboard or clicked "Enter"
                query = searchBar.getText().toString();

                // save search query for press back button case
                preferences = getSharedPreferences("SearchPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("searchQuery", query);
                editor.apply();


                Log.d("EventSearch", "query: " + query);
                // Do something with the search text
                eventCardContainer.removeAllViews();
                eventSearchViewModel.getAlertMessage().observe(EventSearchActivity.this, message -> Toast.makeText(EventSearchActivity.this, message, Toast.LENGTH_SHORT).show());
                eventSearchViewModel.getSearchedEvents().observe(EventSearchActivity.this, eventDataModels -> updateSearchedEventsUI(eventDataModels, rootView));
                eventSearchViewModel.fetchSearchedEvents(query);
                hideKeyboard();
                return true; // Consume the event
            }
            return false; // Continue processing the event
        });


        /*
        // show sad frog
        RelativeLayout no_event_layout = findViewById(R.id.no_event_layout);
        TextView go_home_text = (TextView) no_event_layout.findViewById(R.id.go_home_text);
        go_home_text.setPaintFlags(go_home_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        go_home_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FragHome.class);
                startActivity(intent);
                finish();
            }
        });*/

    }

    @Override
    protected void onResume() {
        Log.d("EventSearchedActivity", "onResume");
        super.onResume();
        // Restore search information
        SharedPreferences preferences = getSharedPreferences("SearchPreferences", Context.MODE_PRIVATE);
        String previousQuery = preferences.getString("searchQuery", "");

        if (!previousQuery.equals("")) {
            // Update the UI with the restored search information
            // Do something with the search text
            searchBar.setText(previousQuery);
            eventCardContainer.removeAllViews();
            eventSearchViewModel.getAlertMessage().observe(EventSearchActivity.this, message -> Toast.makeText(EventSearchActivity.this, message, Toast.LENGTH_SHORT).show());
            eventSearchViewModel.getSearchedEvents().observe(EventSearchActivity.this, eventDataModels -> updateSearchedEventsUI(eventDataModels, rootView));
            eventSearchViewModel.fetchSearchedEvents(previousQuery);
            hideKeyboard();
        }

    }

    @Override
    public void onBackPressed() {
        preferences = getSharedPreferences("SearchPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("searchQuery");
        editor.apply();

        super.onBackPressed();
        Intent intent = new Intent(this, FragHome.class);
        finish();
        startActivity(intent);

    }

    private void updateSearchedEventsUI(List<EventDataModel> eventDataList, View rootView){
        if(!eventDataList.isEmpty()){
            Log.d("SearchedActivity", "Show events!");
            no_event_layout.setVisibility(View.GONE);
            fetchEventsUI(eventDataList, rootView);
            Log.d("SearchedActivity", "fetch event UI done");
        } else {
            Log.d("SearchedActivity", "No events!");
            no_event_layout.setVisibility(View.VISIBLE);
            eventCardContainer.removeAllViews();
        }
    }

    // show event cards
    private void fetchEventsUI(List<EventDataModel> eventDataList, View rootView) {
        //get current login user id
        UserLocalDataSource userLocalDataSource = new UserLocalDataSource(this);
        int userId = Integer.parseInt(userLocalDataSource.getUserId());

        if (eventDataList == null || eventDataList.isEmpty()){
            Log.d("EventSearchedActivity", "Event data is null or empty");
        } else {
            Log.d("EventSearchedActivity", "Loaded user events");
            //LinearLayout eventCardContainer = rootView.findViewById(R.id.eventCardContainer);
            eventCardContainer.removeAllViews();
            EventCardHelper.createEventCardList(this, eventDataList, eventCardContainer, userId, "search");
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
    }
}