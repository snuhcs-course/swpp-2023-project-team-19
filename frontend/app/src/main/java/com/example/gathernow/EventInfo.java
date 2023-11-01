package com.example.gathernow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventInfo extends AppCompatActivity {
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        // Receiving the user id from the previous activity
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        int eventId = intent.getIntExtra("eventId", -1);
        Log.d("EventInfo Testing", "User id: " + userId);
        Log.d("EventInfo Testing", "Event id: " + eventId);

        // query event info from database
        getEventInfo(eventId);

        // Get the buttons from the layout
        Button registerButton = findViewById(R.id.register_button);
        Button waitingResultButton = findViewById(R.id.result_awaiting_button);
        Button cancelRegButton = findViewById(R.id.cancel_button);
        Button viewApplicantsButton = findViewById(R.id.view_applicants_button);
        Button deleteEventButton = findViewById(R.id.delete_button);

        // Change the button visibility based on the user status

    }

    private void getEventInfo(int eventId) {
        service = RetrofitClient.getClient().create(ServiceApi.class);
        service.getEventByEventId(eventId).enqueue(new Callback<EventData>() {
            @Override
            public void onResponse(Call<EventData> call, Response<EventData> response) {
                if (response.isSuccessful()) {
                    EventData event = response.body();
                    Log.d("EventInfo Testing", "Event title: " + event.event_title);
                }
                Log.d("EventInfo Testing", response.message());
            }

            @Override
            public void onFailure(Call<EventData> call, Throwable t) {
                Log.d("EventInfo Testing", "Failed to get event info");
            }
        });
    }

}