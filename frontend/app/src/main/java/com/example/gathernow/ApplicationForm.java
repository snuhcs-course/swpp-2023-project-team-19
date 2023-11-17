package com.example.gathernow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gathernow.api.CodeMessageResponse;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;
import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.api.models.UserDataModel;
import com.squareup.picasso.Picasso;

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

    private String userAvatar;

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

    }

    private void findUserName(int userId) {
        service.getUserInfo(userId).enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                if (response.isSuccessful()) {
//                    UserDataModel userDataModel = response.body();
//
//                    username = userDataModel.name;
//                    String avatar = userDataModel.avatar;
//                    userAvatar = "http://20.2.88.70:8000" + avatar;

                }
            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
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

        ApplicationDataModel new_application = new ApplicationDataModel(applicant_contact, applicant_message, applicant_id, event_id, host_id, applicant_name, userAvatar);

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
                            finish(); // kill this activity
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