package com.example.gathernow.main_ui.event_applicant_info;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gathernow.R;
import com.example.gathernow.api.RetrofitClient;
import com.example.gathernow.api.ServiceApi;
import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.main_ui.cards.ApplicantCardView;
import com.example.gathernow.utils.EventCardHelper;

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
    private ApplicantsInfoViewModel applicantsInfoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants_info);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        Intent intent = getIntent();

//        hostId = intent.getIntExtra("hostId", 0);
//        userId = intent.getIntExtra("userId", 0);
        eventId = intent.getIntExtra("eventId", -1);

        if (eventId == -1) {
            Toast.makeText(ApplicantsInfoActivity.this, "Event ID not found", Toast.LENGTH_SHORT).show();
            Log.e("ApplicationForm Testing", "Failed to get event");
        }


        // set event name
        eventName = intent.getStringExtra("eventName");
        TextView eventNameView = findViewById(R.id.event_name);
        eventNameView.setText(eventName);

        // View model
        applicantsInfoViewModel = new ApplicantsInfoViewModel();

        // Observe value changes
        applicantsInfoViewModel.getAlertMessage().observe(this, message -> Toast.makeText(ApplicantsInfoActivity.this, message, Toast.LENGTH_SHORT).show());
        applicantsInfoViewModel.getApplicationList().observe(this, this::updateApplicantList);

        // display applicant to this event
        applicantsInfoViewModel.fetchEventApplication(eventId);

    }

    private void updateApplicantList(List<ApplicationDataModel> applicationDataModels) {
        LinearLayout applicantCardContainer = findViewById(R.id.container);
        RelativeLayout noApplicant = findViewById(R.id.no_applicant);
        Log.d("ApplicantsInfoActivity", applicationDataModels.toString());
        if (applicationDataModels == null || applicationDataModels.isEmpty()) {
            Log.d("ApplicantsInfoActivity", "No application found");
            noApplicant.setVisibility(View.VISIBLE);
        } else {
            Log.d("ApplicantsInfoActivity", "Loaded event application successfully");
            EventCardHelper.createApplicantList(applicationDataModels, applicantCardContainer);
        }
    }

//    public void getApplicantList(){
//
//        LinearLayout applicantCardContainer = findViewById(R.id.container);
//        RelativeLayout noApplicant = findViewById(R.id.no_applicant);
//        service.getEventApplications(eventId).enqueue(new Callback<List<ApplicationDataModel>>(){
//            @Override
//            public void onResponse(Call<List<ApplicationDataModel>> call, Response<List<ApplicationDataModel>> response) {
//                if (response.isSuccessful()) {
//                    List<ApplicationDataModel> application_list = response.body();
//                    if (response.body() != null  && !Objects.requireNonNull(application_list).isEmpty()){
//                        noApplicant.setVisibility(View.GONE);
//
//                        for (int i = 0; i < application_list.size(); i++){
//                            ApplicationDataModel current_application = application_list.get(i);
//
//
//
//                            // TODO: add info to applicant card
//                            ApplicantCardView newApplicantCard = new ApplicantCardView(ApplicantsInfoActivity.this, null, current_application.getRequestStatus());
//                            newApplicantCard.setApplicationId(current_application.getApplicationId() );
//                            newApplicantCard.setApplicantName(current_application.getApplicantName());
//                            newApplicantCard.setApplicantContact(current_application.getApplicantContact());
//                            newApplicantCard.setApplicantMessage(current_application.getMessage());
//                            newApplicantCard.setApplicantImg(current_application.getApplicantAvatar());
//
//                            //newApplicantCard.setApplicantContact(Integer.toString(current_application.request_status));
//
//
//                            // Add vertical padding to the newEventCard
//                            int verticalPadding = (int) (10 * getResources().getDisplayMetrics().density); // 16dp converted to pixels
//                            newApplicantCard.setPadding(newApplicantCard.getPaddingLeft(), verticalPadding, newApplicantCard.getPaddingRight(), verticalPadding);
//                            applicantCardContainer.addView(newApplicantCard);
//
//
//                        }
//
//
//                    }
//                    else{
//                        // empty application list, return frog
//                        noApplicant.setVisibility(View.VISIBLE);
//                    }
//
//                }
//                else if (response.code() == 404) {
//                    // No applications for this event, show sad frog face
//                    noApplicant.setVisibility(View.VISIBLE);
//                }
//                else{
//
//                    Log.e("ApplicationInfoDisplay", "Check application error occurred"+ Integer.toString(response.code()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ApplicationDataModel>>  call, Throwable t) {
//
//                //Toast.makeText(ApplicantsInfoActivity.this, "Get Applications Error", Toast.LENGTH_SHORT).show();
//                Log.e("ApplicationInfoDisplay", "Error occurred", t);
//            }
//        });
//
//    }
}