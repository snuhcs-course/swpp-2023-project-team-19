package com.example.gathernow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationForm extends AppCompatActivity {

    private ServiceApi service;
    private int userId;
    private int eventId;
    private int hostId;

    private String username;
    private String eventName;
    private String hostName;
    private String hostAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_form);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        // Receiving the user id from the previous activity
        Intent intent = getIntent();

        hostId = intent.getIntExtra("hostId", 0);
        userId = intent.getIntExtra("userId", 0);
        eventId = intent.getIntExtra("eventId", -1);

        if (eventId == -1) {
            Toast.makeText(ApplicationForm.this, "Event ID not found", Toast.LENGTH_SHORT).show();
            Log.e("ApplicationForm Testing", "Failed to get event");
        }

        eventName = intent.getStringExtra("eventName");
        hostName = intent.getStringExtra("hostName");
        hostAvatar = intent.getStringExtra("hostAvatar");

        // query event info from database
        getEventInfo();

        //find applicant name
        findUserName(userId);

        // Save user application into database


    }

    private void getEventInfo(){

        TextView eventTitle = findViewById(R.id.subtitle_text);
        eventTitle.setText(eventName);


        TextView profileName = findViewById(R.id.user_name);
        String n = hostName;
        String display_user_text = "Hosted by " + n;
        profileName.setText(display_user_text);

        ImageView profile_image = findViewById(R.id.profile_image);
        Picasso.get().load(hostAvatar).into(profile_image);


        /*
        service.getEventByEventId(eventId).enqueue(new Callback<List<EventData>>() {
            @Override
            public void onResponse(Call<List<EventData>> call, Response<List<EventData>> response) {
                if (response.isSuccessful()) {
                    List<EventData> events_list = response.body();
                    EventData eventData = events_list.get(0); // Get the first event as the list only contains one event

                    // Update the UI with the event info
                    eventTitle.setText(eventData.event_title);

                    hostId = eventData.host_id;

                    // TODO: Update host's profile name
                    service.getUserInfo(eventData.host_id).enqueue(new Callback<UserData>() {
                        @Override
                        public void onResponse(Call<UserData> call, Response<UserData> response) {
                            if (response.isSuccessful()) {
                                UserData userData = response.body();
                                TextView profileName = findViewById(R.id.user_name);
                                profileName.setText("Hosted by" + userData.name);
                                hostname = userData.name;

                                UserData currentHostAvatar = response.body();
                                String host_avatar = currentHostAvatar.avatar;
                                host_avatar = "http://20.2.88.70:8000" + host_avatar;
                                ImageView profile_img = findViewById(R.id.profile_img);
                                Picasso.get().load(host_avatar).into(profile_img);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserData> call, Throwable t) {
                            Log.d("EventInfo Testing", "Failed to get host name");

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<EventData>> call, Throwable t) {
                Log.d("EventInfo Testing", "Failed to get event info");
            }
        });*/

    }

    private void findUserName(int userId) {
        service.getUserInfo(userId).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    UserData userData = response.body();

                    username = userData.name;

                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.d("ApplicationForm Testing", "Failed to get user(applicant) name");

            }
        });
    }

    public void onSendApplicationEvent(View v){

        Integer applicant_id = userId;
        Integer event_id = eventId;
        Integer host_id =  hostId;
        String applicant_name = username;

        TextView applicant_contact_input = (TextView) findViewById(R.id.applicant_contact);
        String applicant_contact = applicant_contact_input.getText().toString();

        TextView applicant_message_input = (TextView) findViewById(R.id.applicant_message);
        String applicant_message = applicant_message_input.getText().toString();

        int request_status= 0;

        ApplicationData new_application = new ApplicationData(applicant_contact, applicant_message, applicant_id, event_id, host_id, applicant_name, hostAvatar, request_status);

        service.apply_event(new_application).enqueue(new Callback<CodeMessageResponse>() {
            @Override
            public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
                if (response.isSuccessful()) {
                    CodeMessageResponse result = response.body();
                    if (result != null) {
                        if (response.code() == 201) {

                            //Toast.makeText(EventCreate.this, "Event created successfully.", Toast.LENGTH_SHORT).show();
                            // Link to the createSuccessful page
                            Intent intent = new Intent(v.getContext(), ApplySuccessful.class);
                            startActivity(intent);
                        }
                    } else {
                        // Handle the case where the response body is null or empty
                        Toast.makeText(ApplicationForm.this, "Bad Request.", Toast.LENGTH_SHORT).show();


                    }
                } else {
                    // Handle the case where the response is not successful (e.g., non-2xx HTTP status)
                    Toast.makeText(ApplicationForm.this, "Application failed.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CodeMessageResponse> call, Throwable t) {
                Toast.makeText(ApplicationForm.this, "Application Error", Toast.LENGTH_SHORT).show();
            }
        });

    }



}