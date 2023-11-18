package com.example.gathernow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gathernow.api.CodeMessageResponse;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMapSdk;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventInfo extends AppCompatActivity {
    private ServiceApi service;
    public int userId;
    public int eventId;

    public String eventName;

    public String userAvatar;

    public String hostname;

    public int hostId;

    private Integer applicationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        // Receiving the user id from the previous activity
        Intent intent = getIntent();
        userId = Integer.parseInt(intent.getStringExtra("userId"));
        eventId = intent.getIntExtra("eventId", -1);
        if (eventId == -1) {
            Toast.makeText(EventInfo.this, "Event ID not found", Toast.LENGTH_SHORT).show();
        }

        // query event info from database
        getEventInfo();

    }

    private void getEventInfo() {
        // Get elements on the screen
        TextView eventTitle = findViewById(R.id.event_title);
        TextView eventDescription = findViewById(R.id.event_description);
        TextView eventLanguage = findViewById(R.id.languages);
        TextView numParticipants = findViewById(R.id.num_reg_participants);
        TextView numMaxParticipants = findViewById(R.id.num_max_participants);
        TextView eventDate = findViewById(R.id.event_date);
        TextView eventTime = findViewById(R.id.event_time);
        TextView eventDuration = findViewById(R.id.event_duration);
        TextView eventLocation = findViewById(R.id.event_location);
        TextView eventPrice = findViewById(R.id.event_price);

        service.getEventByEventId(eventId).enqueue(new Callback<List<EventData>>() {
            @Override
            public void onResponse(Call<List<EventData>> call, Response<List<EventData>> response) {
                if (response.isSuccessful()) {
                    List<EventData> events_list = response.body();
                    EventData eventData = events_list.get(0); // Get the first event as the list only contains one event

                    // Update the UI with the event info
                    setEventPhoto(eventData.event_type, eventData.event_images);
                    eventTitle.setText(eventData.event_title);
                    setHostName(eventData.host_id);
                    eventDescription.setText(eventData.event_description);
                    eventLanguage.setText(eventData.event_language);
                    numParticipants.setText(eventData.event_num_joined.toString());
                    numMaxParticipants.setText(eventData.event_num_participants.toString());
                    eventDate.setText(eventData.event_date);
                    eventTime.setText(eventData.event_time);
                    eventDuration.setText(eventData.event_duration);
                    eventLocation.setText(eventData.event_location);
                    eventPrice.setText(eventData.event_price.toString());
                    setButtonVisibility(eventData.host_id);

                    eventName = eventData.event_title;
                    hostId = eventData.host_id;

                    // TODO: Update host's profile name
                    service.getUserInfo(eventData.host_id).enqueue(new Callback<UserData>() {
                        @Override
                        public void onResponse(Call<UserData> call, Response<UserData> response) {
                            if (response.isSuccessful()) {
                                UserData userData = response.body();
                                TextView profileName = findViewById(R.id.profile_name);
                                profileName.setText(userData.name);

                                UserData currentHostAvatar = response.body();
                                String host_avatar = currentHostAvatar.avatar;
                                host_avatar = "http://20.2.88.70:8000" + host_avatar;
                                ImageView profile_img = findViewById(R.id.profile_img);
                                Picasso.get().load(host_avatar).into(profile_img);

                                userAvatar = host_avatar;
                                hostname = userData.name;

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
        });
    }

    private void setHostName(int hostId) {
        service.getUserInfo(hostId).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    UserData userData = response.body();
                    TextView profileName = findViewById(R.id.profile_name);
                    profileName.setText(userData.name);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.d("EventInfo Testing", "Failed to get host name");

            }
        });
    }

    private void setEventPhoto(String event_type, String event_images) {
        Log.d("EventInfo Testing", "Event thumbnail: " + event_images);
        ImageView eventImage = findViewById(R.id.event_img);
        // if user uploaded image
        if (event_images != null) {
            String eventThumbnail = "http://20.2.88.70:8000" + event_images;
            Picasso.get().load(eventThumbnail).into(eventImage);
            return;
        }
        switch (event_type) {
            case "Leisure":
                eventImage.setImageResource(R.mipmap.ic_image1_leisure);
                break;
            case "Sports":
                eventImage.setImageResource(R.mipmap.ic_image2_sports_foreground);
                break;
            case "Workshops":
                eventImage.setImageResource(R.mipmap.ic_image3_workshops_foreground);
                break;
            case "Parties":
                eventImage.setImageResource(R.mipmap.ic_image4_parties_foreground);
                break;
            case "Cultural activities":
                eventImage.setImageResource(R.mipmap.ic_image5_cultural_activities_foreground);
                break;
            case "Others":
                eventImage.setImageResource(R.mipmap.ic_image6_others_foreground);
                break;
        }
    }

    private void setButtonVisibility(int hostId) {
        // Get the buttons from the layout
        Button registerButton = findViewById(R.id.register_button);
        Button waitingResultButton = findViewById(R.id.result_awaiting_button);
        Button acceptedResultButton = findViewById(R.id.result_accepted_button);
        Button cancelRegButton = findViewById(R.id.cancel_button);
        Button viewApplicantsButton = findViewById(R.id.view_applicants_button);
        Button deleteEventButton = findViewById(R.id.delete_button);

        // if host id == current user id, show delete button
        if (hostId == userId) {
            viewApplicantsButton.setVisibility(View.VISIBLE);
            deleteEventButton.setVisibility(View.VISIBLE);
        }
        else{
            service.check_if_applied(userId, eventId).enqueue(new Callback<ApplicationData>(){
                @Override
                public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response){
                    if (response.isSuccessful()) {
                        // User has applied for this event, check user application status (pending or accepted)
                        ApplicationData current_application = response.body();
                        //int status = -1;
                        int status = current_application.request_status;

                        applicationId = current_application.application_id;


                        if(status == 0){
                            //Application is pending, show waiting button
                            waitingResultButton.setVisibility(View.VISIBLE);
                            cancelRegButton.setVisibility(View.VISIBLE);
                        }
                        else if (status == 1){
                            acceptedResultButton.setVisibility(View.VISIBLE);
                            cancelRegButton.setVisibility(View.VISIBLE);
                        }
                        else{
                            Log.e("EventInfoDisplay", "Application status not found occurred");
                        }

                    } else if (response.code() == 404) {
                        // User has not applied for this event, show register button
                        registerButton.setVisibility(View.VISIBLE);
                    }



                    else{


                        Log.e("EventInfoDisplay", "Check application error occurred"+ Integer.toString(response.code()));
                    }
                }
                @Override
                public void onFailure(Call<ApplicationData>  call, Throwable t) {
                    Toast.makeText(EventInfo.this, "Check Event Status Error", Toast.LENGTH_SHORT).show();
                    Log.e("EventInfoDisplay", "Error occurred", t);
                }

            });


        }

        // TODO: else if user attendance has not applied, show register button
        // TODO: else if user attendance has not approved, show waiting button
        // TODO: else if user attendance has approved, show cancel button
    }

    public void onDeleteEvent(View view) {
        Log.d("EventInfo Testing", "Delete button clicked");
        // Set up an alert builder to ask users whether they want to delete the event
        AlertDialog.Builder deleteAlertBuilder = new AlertDialog.Builder(this);
        deleteAlertBuilder
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // TODO: Delete the event
                    service.deleteEventByEventId(eventId).enqueue(new Callback<CodeMessageResponse>() {
                        @Override
                        public void onResponse(Call<CodeMessageResponse> call, Response<CodeMessageResponse> response) {
                            Log.d("EventInfo Testing", "Event deleted");
                            if (response.isSuccessful()) {
                                // !!! Commented lines here will cause crash!
                                //Log.d("EventInfo Testing", response.body().toString());
                                CodeMessageResponse codeMessageResponse = response.body();
                                //Toast.makeText(EventInfo.this, codeMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CodeMessageResponse> call, Throwable t) {
                            Log.d("EventInfo Testing", "Failed to delete event");
                        }
                    });

                    // Direct to Delete Successful class
                    //Intent intent = new Intent(view.getContext(), FragHome.class);
                    //startActivity(intent);
                    Intent intent = new Intent(view.getContext(), DeleteSuccessful.class);
                    startActivity(intent);
                    finish();

                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Do nothing
                    dialog.dismiss();
                });
        deleteAlertBuilder.show();

    }

    public void onRegisterEvent(View v){

        Intent intent = new Intent(v.getContext(), ApplicationForm.class);

        // Send the user id to the EventInfo activity
        intent.putExtra("userId", userId);
        intent.putExtra("eventId", eventId);
        intent.putExtra("hostId", hostId);
        intent.putExtra("eventName", eventName);
        intent.putExtra("hostName", hostname);
        intent.putExtra("hostAvatar", userAvatar);

        startActivity(intent);
        finish();

    }

    public void onDeleteApplication(View view){
        service.delete_application(applicationId).enqueue(new Callback<ApplicationData>() {
            @Override
            public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response) {
                Log.d("EventInfo Testing", "Event deleted");
                if (response.isSuccessful()) {
                    // !!! Commented lines here will cause crash!
                    //Log.d("EventInfo Testing", response.body().toString());
                    ApplicationData application_to_delete = response.body();
                    //Toast.makeText(EventInfo.this, codeMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApplicationData> call, Throwable t) {
                Log.d("ApplicationDelete Testing", "Failed to delete application");
            }
        });

        // Direct to Application Delete Successful class
        //Intent intent = new Intent(view.getContext(), FragHome.class);
        //startActivity(intent);
        Intent intent = new Intent(view.getContext(), DeleteSuccessful.class);
        startActivity(intent);
        finish();

    }

    public void onViewApplicants(View v) {
        Intent intent = new Intent(v.getContext(), ApplicantsInfoActivity.class);

        // Send the user id to the EventInfo activity
        intent.putExtra("userId", userId);
        intent.putExtra("eventId", eventId);
        intent.putExtra("hostId", hostId);
        intent.putExtra("eventName", eventName);
        intent.putExtra("hostName", hostname);
        intent.putExtra("hostAvatar", userAvatar);
        startActivity(intent);
    }


}