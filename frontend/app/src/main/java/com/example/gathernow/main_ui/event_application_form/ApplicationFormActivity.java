package com.example.gathernow.main_ui.event_application_form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gathernow.ApplySuccessful;
import com.example.gathernow.R;
import com.example.gathernow.api.CodeMessageResponse;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;
import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.api.models.ApplicationDataModelBuilder;
import com.example.gathernow.api.models.UserDataModel;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        // query event info from database
        getEventInfo();

        // View model
        applicationFormViewModel = new ApplicationFormViewModel();
        applicationFormViewModel.fetchUserData(userId);
        applicationFormViewModel.getAlertMessage().observe(this, message -> {
            if (message.equals("Application sent successfully")) {
                Intent intent1 = new Intent(this, ApplySuccessful.class);
                startActivity(intent1);
                finish();
            } else {
                Toast.makeText(ApplicationFormActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        applicationFormViewModel.getApplicantData().observe(this, applicantData -> {
            username = applicantData.getName();
            userAvatar = applicantData.getAvatar();
        });
    }

    private void getEventInfo() {
        TextView eventTitle = findViewById(R.id.subtitle_text);
        eventTitle.setText(eventName);

        TextView profileName = findViewById(R.id.user_name);
        String displayUserText = "Hosted by " + hostName;
        profileName.setText(displayUserText);

        ImageView profileImage = findViewById(R.id.profile_image);
        Picasso.get().load(hostAvatar).into(profileImage);

    }

    public void onSendApplicationEvent(View v) {
        Integer applicant_id = userId;
        Integer event_id = eventId;
        Integer host_id = hostId;
        String applicant_name = username;

        TextView applicant_contact_input = findViewById(R.id.applicant_contact);
        String applicant_contact = applicant_contact_input.getText().toString();

        TextView applicant_message_input = findViewById(R.id.applicant_message);
        String applicant_message = applicant_message_input.getText().toString();

        ApplicationDataModelBuilder applicationBuilder = new ApplicationDataModelBuilder();
        applicationBuilder.setApplicantContact(applicant_contact)
                .setApplicantId(applicant_id)
                .setMessage(applicant_message)
                .setEventId(event_id)
                .setHostId(host_id)
                .setApplicantName(applicant_name)
                .setApplicantAvatar(userAvatar);

        ApplicationDataModel newApplication = applicationBuilder.build();
//        ApplicationDataModel newApplication = new ApplicationDataModel(applicant_contact, applicant_message, applicant_id, event_id, host_id, applicant_name, userAvatar);
        applicationFormViewModel.applyEvent(newApplication);

    }


}