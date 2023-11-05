package com.example.gathernow;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantsInfoActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_applicants_info);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        Intent intent = getIntent();

        hostId = intent.getIntExtra("hostId", 0);
        userId = intent.getIntExtra("userId", 0);
        eventId = intent.getIntExtra("eventId", -1);

        if (eventId == -1) {
            Toast.makeText(ApplicantsInfoActivity.this, "Event ID not found", Toast.LENGTH_SHORT).show();
            Log.e("ApplicationForm Testing", "Failed to get event");
        }

        eventName = intent.getStringExtra("eventName");
        hostName = intent.getStringExtra("hostName");
        hostAvatar = intent.getStringExtra("hostAvatar");


        TextView event_name = (TextView) findViewById(R.id.event_name);

        // get event name here
        event_name.setText(eventName);


        // display applicant to this event
        getApplicantList();

    }

    public void getApplicantList(){

        LinearLayout applicantCardContainer = findViewById(R.id.container);
        RelativeLayout noApplicant = findViewById(R.id.no_applicant);
        service.getEventApplications(eventId).enqueue(new Callback<List<ApplicationData>>(){
            @Override
            public void onResponse(Call<List<ApplicationData>> call, Response<List<ApplicationData>> response) {
                if (response.isSuccessful()) {
                    List<ApplicationData> application_list = response.body();
                    if (response.body() != null  && !Objects.requireNonNull(application_list).isEmpty()){
                        noApplicant.setVisibility(View.GONE);

                        for (int i = 0; i < application_list.size(); i++){
                            ApplicationData current_application = application_list.get(i);



                            // TODO: add info to applicant card
                            ApplicantCardView newApplicantCard = new ApplicantCardView(ApplicantsInfoActivity.this, null, current_application.request_status);
                            newApplicantCard.setApplicationId(current_application.application_id );
                            newApplicantCard.setApplicantName(current_application.applicant_name);
                            newApplicantCard.setApplicantContact(current_application.applicant_contact);
                            newApplicantCard.setApplicantMessage(current_application.message);
                            newApplicantCard.setApplicantImg(current_application.applicant_avatar);

                            //newApplicantCard.setApplicantContact(Integer.toString(current_application.request_status));


                            // Add vertical padding to the newEventCard
                            int verticalPadding = (int) (10 * getResources().getDisplayMetrics().density); // 16dp converted to pixels
                            newApplicantCard.setPadding(newApplicantCard.getPaddingLeft(), verticalPadding, newApplicantCard.getPaddingRight(), verticalPadding);
                            applicantCardContainer.addView(newApplicantCard);


                        }


                    }
                    else{
                        // empty application list, return frog
                        noApplicant.setVisibility(View.VISIBLE);
                    }

                }
                else if (response.code() == 404) {
                    // No applications for this event, show sad frog face
                    noApplicant.setVisibility(View.VISIBLE);
                }
                else{

                    Log.e("ApplicationInfoDisplay", "Check application error occurred"+ Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<ApplicationData>>  call, Throwable t) {

                //Toast.makeText(ApplicantsInfoActivity.this, "Get Applications Error", Toast.LENGTH_SHORT).show();
                Log.e("ApplicationInfoDisplay", "Error occurred", t);
            }
        });

    }
}