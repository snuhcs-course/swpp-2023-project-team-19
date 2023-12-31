package com.mugswpp.gathernow.main_ui.event_applicant_info;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mugswpp.gathernow.R;
import com.mugswpp.gathernow.api.models.ApplicationDataModel;
import com.mugswpp.gathernow.main_ui.event_info.EventInfoActivity;
import com.mugswpp.gathernow.utils.EventCardHelper;

import java.util.List;

public class ApplicantsInfoActivity extends AppCompatActivity {

    private int userId, eventId;
    private String sourceFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants_info);

        Intent intent = getIntent();

//        hostId = intent.getIntExtra("hostId", 0);
//        userId = intent.getIntExtra("userId", 0);
        userId = intent.getIntExtra("userId", -1);
        eventId = intent.getIntExtra("eventId", -1);
        sourceFrag = intent.getStringExtra("sourceFrag");

        if (eventId == -1) {
            Toast.makeText(ApplicantsInfoActivity.this, "Event ID not found", Toast.LENGTH_SHORT).show();
            Log.e("ApplicationForm Testing", "Failed to get event");
        }


        // set event name
        String eventName = intent.getStringExtra("eventName");
        TextView eventNameView = findViewById(R.id.event_name);
        eventNameView.setText(eventName);

        // View model
        ApplicantsInfoViewModel applicantsInfoViewModel = new ApplicantsInfoViewModel();

        // Observe value changes
        applicantsInfoViewModel.getAlertMessage().observe(this, message -> Toast.makeText(ApplicantsInfoActivity.this, message, Toast.LENGTH_SHORT).show());
        applicantsInfoViewModel.getApplicationList().observe(this, this::updateApplicantList);

        // display applicant to this event
        applicantsInfoViewModel.fetchEventApplication(eventId);


    }

    @Override
    public void onBackPressed() {
        // Reload data or perform other necessary actions
        //recreate(); // This recreates the activity

        // Create an Intent to go back to EventInfoActivity
        Intent intent = new Intent(this, EventInfoActivity.class);

        // Add any extra data if needed
        intent.putExtra("userId", userId);
        intent.putExtra("eventId", eventId);
        intent.putExtra("sourceFrag", sourceFrag);

        // Set the flag to clear the back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Start the EventInfoActivity
        startActivity(intent);

        // Finish the current activity to remove it from the back stack
        finish();
    }


    private void updateApplicantList(List<ApplicationDataModel> applicationDataModels) {
        LinearLayout applicantCardContainer = findViewById(R.id.container);
        RelativeLayout noApplicant = findViewById(R.id.no_applicant);
        Log.d("ApplicantsInfoActivity", applicationDataModels.toString());
        if (applicationDataModels == null || applicationDataModels.isEmpty()) {
            Log.d("ApplicantsInfoActivity", "No application found");
            noApplicant.setVisibility(View.VISIBLE);
//            applicantCardContainer.setVisibility(View.GONE);
        } else {
            applicantCardContainer.setVisibility(View.VISIBLE);
            noApplicant.setVisibility(View.GONE);
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