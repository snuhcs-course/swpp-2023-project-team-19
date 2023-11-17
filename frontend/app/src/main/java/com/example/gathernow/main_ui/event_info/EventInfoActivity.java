package com.example.gathernow.main_ui.event_info;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gathernow.ApplicantsInfoActivity;
import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.ApplicationForm;
import com.example.gathernow.DeleteSuccessful;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.R;
import com.example.gathernow.api.models.UserDataModel;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;
import com.example.gathernow.main_ui.EventDataSource;
import com.example.gathernow.main_ui.EventRepository;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventInfoActivity extends AppCompatActivity {
    private ServiceApi service;
    public int userId;
    public int eventId;

    public String eventName;

    public String userAvatar;

    public String hostname;

    public int hostId;
    private EventInfoViewModel eventInfoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        // Receiving the user id from the previous activity
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        eventId = intent.getIntExtra("eventId", -1);
        if (eventId == -1) {
            Toast.makeText(EventInfoActivity.this, "Event ID not found", Toast.LENGTH_SHORT).show();
        }

        // Data Source
        EventDataSource eventDataSource = new EventDataSource();
        // Repository
        EventRepository eventRepository = new EventRepository(eventDataSource);
        // View Model
        eventInfoViewModel = new EventInfoViewModel(eventRepository);
        // Observe event data changes
        eventInfoViewModel.getAlertMessage().observe(this, message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
        eventInfoViewModel.getEventData().observe(this, this::updateEventInfoUI);
        eventInfoViewModel.getHostData().observe(this, this::updateHostInfoUI);
        eventInfoViewModel.getShowRegisterButton().observe(this, showRegisterButton -> {
            Button registerButton = findViewById(R.id.register_button);
            if (showRegisterButton) {
                registerButton.setVisibility(View.VISIBLE);
            } else {
                registerButton.setVisibility(View.GONE);
            }
        });
        eventInfoViewModel.getShowWaitingButton().observe(this, showWaitingButton -> {
            Button waitingButton = findViewById(R.id.result_awaiting_button);
            if (showWaitingButton) {
                waitingButton.setVisibility(View.VISIBLE);
            } else {
                waitingButton.setVisibility(View.GONE);
            }
        });
        eventInfoViewModel.getShowAcceptedButton().observe(this, showAcceptedButton -> {
            Button acceptedButton = findViewById(R.id.result_accepted_button);
            if (showAcceptedButton) {
                acceptedButton.setVisibility(View.VISIBLE);
            } else {
                acceptedButton.setVisibility(View.GONE);
            }
        });
        eventInfoViewModel.getShowCancelRegButton().observe(this, showCancelRegButton -> {
            Button cancelRegButton = findViewById(R.id.cancel_button);
            if (showCancelRegButton) {
                cancelRegButton.setVisibility(View.VISIBLE);
            } else {
                cancelRegButton.setVisibility(View.GONE);
            }
        });
        eventInfoViewModel.getShowViewApplicantsButton().observe(this, showViewApplicantsButton -> {
            Button viewApplicantsButton = findViewById(R.id.view_applicants_button);
            if (showViewApplicantsButton) {
                viewApplicantsButton.setVisibility(View.VISIBLE);
            } else {
                viewApplicantsButton.setVisibility(View.GONE);
            }
        });
        eventInfoViewModel.getShowDeleteEventButton().observe(this, showDeleteEventButton -> {
            Button deleteEventButton = findViewById(R.id.delete_button);
            if (showDeleteEventButton) {
                deleteEventButton.setVisibility(View.VISIBLE);
            } else {
                deleteEventButton.setVisibility(View.GONE);
            }
        });
        eventInfoViewModel.getShowDeleteEventSuccess().observe(this, showDeleteSuccess -> {
            if (showDeleteSuccess) {
                Intent intent1 = new Intent(this, DeleteSuccessful.class);
                startActivity(intent1);
                finish();
            }
        });
        eventInfoViewModel.getShowDeleteApplicationSuccess().observe(this, showDeleteSuccess -> {
            if (showDeleteSuccess) {
                Intent intent1 = new Intent(this, DeleteSuccessful.class);
                startActivity(intent1);
                finish();
            }
        });
        // query event info from database
        eventInfoViewModel.loadEventInfo(eventId, userId);

    }

    private void updateHostInfoUI(UserDataModel userDataModel) {
        if (userDataModel == null) {
            Log.d("EventInfo Testing", "User data model is null");
            return;
        }
        TextView profileName = findViewById(R.id.profile_name);
        profileName.setText(userDataModel.getName());

        String host_avatar = userDataModel.getAvatar();
        host_avatar = "http://20.2.88.70:8000" + host_avatar;
        ImageView profile_img = findViewById(R.id.profile_img);
        Picasso.get().load(host_avatar).into(profile_img);
    }

    private void updateEventInfoUI(EventDataModel eventDataModel) {
        if (eventDataModel == null) {
            Log.d("EventInfo Testing", "Event data model is null");
            return;
        }
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
        TextView eventLastRegisterDate = findViewById(R.id.event_register_date);
        TextView eventLastRegisterTime = findViewById(R.id.event_register_time);

        // Update the UI with the event info
        setEventPhoto(eventDataModel.getEventType(), eventDataModel.getEventImages());
        eventTitle.setText(eventDataModel.getEventTitle());
        eventDescription.setText(eventDataModel.getEventDescription());
        eventLanguage.setText(eventDataModel.getEventLanguage());

        String numJoinedFormat = String.format(Locale.ENGLISH, "%,d", eventDataModel.getEventNumJoined());
        numParticipants.setText(numJoinedFormat);

        String numMaxParticipantsFormat = String.format(Locale.ENGLISH, "%,d", eventDataModel.getEventNumParticipants());
        numMaxParticipants.setText(numMaxParticipantsFormat);

        eventDate.setText(eventDataModel.getEventDate());
        eventTime.setText(eventDataModel.getEventTime());
        eventLastRegisterDate.setText(eventDataModel.getEventRegisterDate());
        eventLastRegisterTime.setText(eventDataModel.getEventRegisterTime());
        eventDuration.setText(eventDataModel.getEventDuration());
        eventLocation.setText(eventDataModel.getEventLocation());

        String priceFormat = String.format(Locale.ENGLISH, "%,d", eventDataModel.getEventPrice());
        eventPrice.setText(priceFormat);
//        setButtonVisibility(eventDataModel.getHostId());
//
        eventName = eventDataModel.getEventTime();
        hostId = eventDataModel.getHostId();
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

    public void onDeleteEvent(View view) {
        Log.d("EventInfo Testing", "Delete button clicked");
        // Set up an alert builder to ask users whether they want to delete the event
        AlertDialog.Builder deleteAlertBuilder = new AlertDialog.Builder(this);
        deleteAlertBuilder
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // TODO: Delete the event
                    eventInfoViewModel.deleteEvent(eventId);

                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Do nothing
                    dialog.dismiss();
                });
        deleteAlertBuilder.show();

    }

    public void onRegisterEvent(View v) {

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

    public void onDeleteApplication(View view) {
        eventInfoViewModel.deleteApplication();
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