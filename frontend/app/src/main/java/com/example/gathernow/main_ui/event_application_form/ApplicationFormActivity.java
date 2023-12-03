package com.example.gathernow.main_ui.event_application_form;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gathernow.ApplySuccessful;
import com.example.gathernow.FragHome;
import com.example.gathernow.R;
import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.api.models.ApplicationDataModelBuilder;
import com.example.gathernow.utils.ImageLoader.ImageLoader;
import com.example.gathernow.utils.ImageLoader.ProxyImageLoader;

public class ApplicationFormActivity extends AppCompatActivity {
    private int userId;
    private int eventId;
    private int hostId;

    private String username;
    private String eventName;
    private String hostName;
    private String hostAvatar;
    private ApplicationFormViewModel applicationFormViewModel;
    private String userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_form);

        View rootLayout = findViewById(R.id.container);

        // Add a touch listener to hide the keyboard when tapping on a blank space
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKB();
                return false;
            }
        });

        // Receiving the user id from the previous activity
        Intent intent = getIntent();
        hostId = intent.getIntExtra("hostId", 0);
        userId = intent.getIntExtra("userId", 0);
        eventId = intent.getIntExtra("eventId", -1);
        if (eventId == -1) {
            Toast.makeText(ApplicationFormActivity.this, "Event ID not found", Toast.LENGTH_SHORT).show();
            Log.e("ApplicationForm Testing", "Failed to get event");
        }
        eventName = intent.getStringExtra("eventName");
        hostName = intent.getStringExtra("hostName");
        hostAvatar = intent.getStringExtra("hostAvatar");

        // View model
        applicationFormViewModel = new ApplicationFormViewModel();

        // query event info from database
        getEventInfo();

        applicationFormViewModel.fetchUserData(userId);
        applicationFormViewModel.getAlertMessage().observe(this, message -> {
            if (message.equals("Application sent successfully")) {
                Intent intent1 = new Intent(this, ApplySuccessful.class);
                startActivity(intent1);
                finish();
            } else if (message.equals("Event not found")) {
                // open a dialog to notify the user that the event is not found, then go back to home page
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder
                        .setTitle("Error")
                        .setCancelable(false)
                        .setMessage("The event you are applying is deleted. Please try again later.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Go back to home page
                            Intent intent1 = new Intent(this, FragHome.class);
                            startActivity(intent1);
                            finish();
                        });
                alertBuilder.show();
            }
//            else if (message.equals("Network error")) {
//                // open a dialog to notify the user that network error, then reload the page
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//                alertBuilder
//                        .setTitle("Error")
//                        .setCancelable(false)
//                        .setMessage("Network error. Please try again later.")
//                        .setPositiveButton("OK", (dialog, which) -> {
//                            // Reload the page
//                            Intent intent1 = new Intent(this, ApplicationFormActivity.class);
//                            intent1.putExtra("hostId", hostId);
//                            intent1.putExtra("userId", userId);
//                            intent1.putExtra("eventId", eventId);
//                            intent1.putExtra("eventName", eventName);
//                            intent1.putExtra("hostName", hostName);
//                            intent1.putExtra("hostAvatar", hostAvatar);
//                            startActivity(intent1);
//                            finish();
//                        });
//            }
            else {
//                Toast.makeText(ApplicationFormActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        applicationFormViewModel.getApplicantData().observe(this, applicantData -> {
            username = applicantData.getName();
            userAvatar = applicantData.getAvatar();
        });
    }

    private void hideKB() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getEventInfo() {
        TextView eventTitle = findViewById(R.id.subtitle_text);
        eventTitle.setText(eventName);

        TextView profileName = findViewById(R.id.user_name);
        String displayUserText = "Hosted by " + hostName;
        profileName.setText(displayUserText);
        ImageView profileImage = findViewById(R.id.profile_image);
        //Picasso.get().load(hostAvatar).into(profileImage);

        int resourceId = R.drawable.ic_user_no_profile;
        ImageLoader imageLoader = new ProxyImageLoader(hostAvatar, resourceId);
        imageLoader.displayImage(profileImage);
        applicationFormViewModel.fetchEventData(eventId);

    }

    public void onSendApplicationEvent(View v) {
        Toast.makeText(this, "Sending application...", Toast.LENGTH_SHORT).show();
        Integer applicant_id = userId;
        Integer event_id = eventId;
        Integer host_id = hostId;
        String applicant_name = username;

        TextView applicant_contact_input = findViewById(R.id.applicant_contact);
        String applicant_contact = applicant_contact_input.getText().toString().trim();


        TextView applicant_message_input = findViewById(R.id.applicant_message);
        String applicant_message = applicant_message_input.getText().toString().trim();

        ApplicationDataModelBuilder applicationBuilder = new ApplicationDataModelBuilder();
        applicationBuilder.setApplicantContact(applicant_contact)
                .setApplicantId(applicant_id)
                .setMessage(applicant_message)
                .setEventId(event_id)
                .setHostId(host_id)
                .setApplicantName(applicant_name)
                .setApplicantAvatar(userAvatar);

        ApplicationDataModel newApplication = applicationBuilder.build();
        applicationFormViewModel.applyEvent(newApplication);
    }


}