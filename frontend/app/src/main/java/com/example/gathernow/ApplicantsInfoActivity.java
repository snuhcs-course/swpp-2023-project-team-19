package com.example.gathernow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Time;

public class ApplicantsInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants_info);

        LinearLayout applicantCardContainer = findViewById(R.id.container);
        RelativeLayout noApplicant = findViewById(R.id.no_applicant);
        TextView event_name = (TextView) findViewById(R.id.event_name);


        // TODO: get event name here
        String event_n = "AHAHAHAHA";
        event_name.setText(event_n);

        // TODO: check if there is any applicant to this event
        Integer i = 1;
        if(i == 1){
            noApplicant.setVisibility(View.VISIBLE);
        } else {
            noApplicant.setVisibility(View.GONE);
            // TODO: add info to applicant card
            ApplicantCardView newApplicantCard = new ApplicantCardView(this, null);

            String applicant_name = "Taylor Swift";
            String applicant_contact = "@swifties";
            String applicant_message = "Hello! I am Taylor Swift, please approve my application!";
            //newApplicantCard.setApplicantImg();

            newApplicantCard.setApplicantName(applicant_name);
            newApplicantCard.setApplicantContact(applicant_contact);
            newApplicantCard.setApplicantMessage(applicant_message);

            // Add vertical padding to the newEventCard
            int verticalPadding = (int) (10 * getResources().getDisplayMetrics().density); // 16dp converted to pixels
            newApplicantCard.setPadding(newApplicantCard.getPaddingLeft(), verticalPadding, newApplicantCard.getPaddingRight(), verticalPadding);
            applicantCardContainer.addView(newApplicantCard);
        }
    }
}