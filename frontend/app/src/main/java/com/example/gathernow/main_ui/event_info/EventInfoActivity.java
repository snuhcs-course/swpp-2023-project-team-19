package com.example.gathernow.main_ui.event_info;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gathernow.FragHome;
import com.example.gathernow.main_ui.cards.EventCardView;
import com.example.gathernow.main_ui.event_applicant_info.ApplicantsInfoActivity;
import com.example.gathernow.main_ui.event_application_form.ApplicationFormActivity;
import com.example.gathernow.DeleteSuccessful;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.R;
import com.example.gathernow.api.models.UserDataModel;
import com.example.gathernow.main_ui.EventDataSource;
import com.example.gathernow.main_ui.EventRepository;
import com.example.gathernow.main_ui.event_filter.EventFilterActivity;
import com.example.gathernow.main_ui.event_search.EventSearchActivity;
import com.example.gathernow.main_ui.home.HomeActivity;
import com.example.gathernow.main_ui.profile.ProfileActivity;
import com.example.gathernow.utils.ImageLoader.ImageLoader;
import com.example.gathernow.utils.ImageLoader.ProxyImageLoader;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.squareup.picasso.Picasso;

import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.OnMapReadyCallback;

import java.util.List;
import java.util.Locale;

import com.example.gathernow.main_ui.profile.ProfileActivity;

public class EventInfoActivity extends AppCompatActivity {
    public int userId;
    public int eventId;

    public String eventName, sourceFrag;

    public String hostAvatar;

    public String hostName;

    public int hostId;
    private EventInfoViewModel eventInfoViewModel;

    private NaverMap naverMap;

    private double eventLongitude;
    private double eventLatitude;
    private MapFragment mapFragment;

    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        scrollView = findViewById(R.id.content);
        // Receiving the user id from the previous activity
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        eventId = intent.getIntExtra("eventId", -1);
        if(intent.getStringExtra("sourceFrag") != null){
            sourceFrag = intent.getStringExtra("sourceFrag");
        }
        else{
            sourceFrag = null;
        }

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
        eventInfoViewModel.getAlertMessage().observe(this, message -> {
            if (message != null && (message.equals("Event not found") || message.equals("Application data not found"))) {
                // open a dialog to notify the user that the event is not found, then go back to home page
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder
                        .setTitle("Event not found")
                        .setCancelable(false)
                        .setMessage("The event you are looking for is deleted. Do you want to go back?")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Go back to home page
                            Intent intent1 = new Intent(this, FragHome.class);
                            startActivity(intent1);
                            finish();
                        });
                alertBuilder.show();
            }
            else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
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
        Button resultButton = findViewById(R.id.result_button);
        eventInfoViewModel.getShowResultButton().observe(this, showWaitingButton -> {
            if (showWaitingButton) {
                resultButton.setVisibility(View.VISIBLE);
            } else {
                resultButton.setVisibility(View.GONE);
            }
        });
        eventInfoViewModel.getApplicationStatus().observe(this, resultButton::setText);
//        eventInfoViewModel.getShowAcceptedButton().observe(this, showAcceptedButton -> {
//            Button acceptedButton = findViewById(R.id.result_accepted_button);
//            if (showAcceptedButton) {
//                acceptedButton.setVisibility(View.VISIBLE);
//            } else {
//                acceptedButton.setVisibility(View.GONE);
//            }
//        });
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
        eventInfoViewModel.getClickableCancelButton().observe(this, clickableCancelButton -> {
            Button cancelButton = findViewById(R.id.cancel_button);
            Log.d("EventInfo Testing", "Cancel button clickable: " + clickableCancelButton);
            cancelButton.setClickable(clickableCancelButton);
            cancelButton.setEnabled(clickableCancelButton);
        });
        eventInfoViewModel.getClickableRegisterButton().observe(this, clickableRegisterButton -> {
            Button registerButton = findViewById(R.id.register_button);
            registerButton.setClickable(clickableRegisterButton);
            registerButton.setEnabled(clickableRegisterButton);
        });
        // query event info from database
        eventInfoViewModel.loadEventInfo(eventId, userId);
        // NaverMap

    }

    @Override
    public void onBackPressed() {
        // Handle what happens when the back button is pressed
        // -> go back to refreshed home or profile page
        if("home".equals(sourceFrag)){
            Log.e("BackButton", "Going back to Home in else case");
            Intent intent = new Intent(this, FragHome.class);
            startActivity(intent);
            finish();
        }
        else if ("profile".equals(sourceFrag) || "applicantInfo".equals(sourceFrag)){
            Log.e("BackButton", "Going back to Profile in else case");
            Intent intent = new Intent(this, FragHome.class);
            intent.putExtra("targetFragment", "profile");
            startActivity(intent);
            finish();
        }
        else if ("events".equals(sourceFrag)){
            Log.e("BackButton", "Going back to Event in else case");
            Intent intent = new Intent(this, FragHome.class);
            intent.putExtra("targetFragment", "event");
            startActivity(intent);
            finish();
        }

        else if("search".equals(sourceFrag)){
            Log.e("BackButton", "Going back to Search in else case");
            Intent intent = new Intent(this, EventSearchActivity.class);
            intent.putExtra("targetFragment", "search");
            startActivity(intent);
            finish();
        }
        else{
            Log.e("BackButton", "Going back to Home in else case");
            super.onBackPressed();
        }
    }

    private void updateHostInfoUI(UserDataModel userDataModel) {
        if (userDataModel == null) {
            Log.d("EventInfo Testing", "User data model is null");
            return;
        }

        hostName = userDataModel.getName();
        TextView profileName = findViewById(R.id.profile_name);
        profileName.setText(hostName);

        hostAvatar = userDataModel.getAvatar();
        ImageView profile_img = findViewById(R.id.profile_img);
        //Picasso.get().load(hostAvatar).into(profile_img);

        int resourceId = R.drawable.ic_user_no_profile;
        ImageLoader imageLoader = new ProxyImageLoader(hostAvatar, resourceId);
        imageLoader.displayImage(profile_img);
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
        String fixed_time = eventDataModel.getEventTime().substring(0, 5);
        eventTime.setText(fixed_time);
        eventLastRegisterDate.setText(eventDataModel.getEventRegisterDate());
        String fixed_regTime = eventDataModel.getEventRegisterTime().substring(0, 5);
        eventLastRegisterTime.setText(fixed_regTime);
        eventDuration.setText(eventDataModel.getEventDuration());
        eventLocation.setText(eventDataModel.getEventLocation());

        String priceFormat = String.format(Locale.ENGLISH, "%,d", eventDataModel.getEventPrice());
        priceFormat = "₩ " + priceFormat;
        eventPrice.setText(priceFormat);
//      setButtonVisibility(eventDataModel.getHostId());
//
        eventName = eventDataModel.getEventTitle();
        hostId = eventDataModel.getHostId();
        // Display NaverMap UI based on eventLongitude and eventLatitude
        eventLongitude = eventDataModel.getEventLongitude();
        eventLatitude = eventDataModel.getEventLatitude();
        FragmentManager fm = getSupportFragmentManager();
        mapFragment = (MapFragment)fm.findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

        mapFragment.getMapAsync(naverMap -> {
            // For example, set the camera position
            // Testing with specifed location. Will generalize it latter.
            CameraPosition cameraPosition = new CameraPosition(
                    new LatLng(eventLatitude, eventLongitude),
                    15.0 // Zoom level (adjust as needed)
            );
            Log.d("EventInfo Testing", "Event location: " + eventLatitude + ", " + eventLongitude);
            naverMap.setCameraPosition(cameraPosition);
            Marker marker = new Marker();
            marker.setPosition(new LatLng(eventLatitude, eventLongitude));
            marker.setMap(naverMap);
            marker.setCaptionText("Event location");


        // location text is clickable and will open NaverMap
        eventLocation.setPaintFlags(eventLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        eventLocation.setOnClickListener(v -> {
            openNaverMap(eventLongitude, eventLatitude);
        });

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull NaverMap naverMap) {
                // For example, set the camera position
                // Testing with specifed location. Will generalize it latter.
                CameraPosition cameraPosition = new CameraPosition(
                        new LatLng(eventLatitude, eventLongitude),
                        15.0 // Zoom level (adjust as needed)
                );
                Log.d("EventInfo Testing", "Event location: " + eventLatitude + ", " + eventLongitude);
                naverMap.setCameraPosition(cameraPosition);
                Marker marker = new Marker();
                marker.setPosition(new LatLng(eventLatitude, eventLongitude));
                marker.setMap(naverMap);
                marker.setCaptionText("Event location");

                UiSettings uiSettings = naverMap.getUiSettings();
                uiSettings.setLocationButtonEnabled(false); // This hides the GPS button
                uiSettings.setZoomGesturesEnabled(true);
                uiSettings.setScrollGesturesEnabled(true);
                uiSettings.setZoomControlEnabled(true);

                View mapView = mapFragment.getView();
                if (mapView != null) {
                    mapView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            // When the map is touched, request the parent ScrollView to not intercept touch events
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    scrollView.requestDisallowInterceptTouchEvent(true);
                                    break;
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    scrollView.requestDisallowInterceptTouchEvent(false);
                                    break;
                            }
                            // Let the map handle the touch event
                            return false;
                        }
                    });
                }
            }
        });
    }
    );
    }

    private void openNaverMap(double eventLongitude, double eventLatitude) {
        // Create a Uri with the specified latitude and longitude
        //Uri gmmIntentUri = Uri.parse("geo:" + eventLongitude + "," + eventLatitude);
        Uri gmmIntentUri = Uri.parse("geo:" + eventLatitude + "," + eventLongitude + "?q=" + eventLatitude + "," + eventLongitude);


        // Create an Intent to launch the Naver app
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.nhn.android.nmap");

        // Check if Naver app is installed
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            // If Naver app is not installed, you can open the map in a browser or
            // suggest the user to install the Naver app from the Play Store
            Uri playStoreUri = Uri.parse("market://details?id=com.nhn.android.nmap");
            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, playStoreUri);
            startActivity(playStoreIntent);
        }
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
        eventImage.getLayoutParams().width = 1000;
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
        // check whether the current event is staled or not...?

        Intent intent = new Intent(v.getContext(), ApplicationFormActivity.class);

        // Send the user id to the EventInfo activity
        intent.putExtra("userId", userId);
        intent.putExtra("eventId", eventId);
        intent.putExtra("hostId", hostId);
        intent.putExtra("eventName", eventName);
        intent.putExtra("hostName", hostName);
        intent.putExtra("hostAvatar", hostAvatar);

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
        intent.putExtra("hostName", hostName);
        intent.putExtra("hostAvatar", hostAvatar);
        intent.putExtra("sourceFrag", sourceFrag);
        startActivity(intent);
    }
}