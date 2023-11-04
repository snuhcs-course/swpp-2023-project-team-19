package com.example.gathernow;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ApplicantCardView extends LinearLayout {

    private ImageView applicant_img;
    private TextView applicant_name, applicant_contact, applicant_message;

    public ApplicantCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.applicant_card_layout, this, true);

        // Initialize UI elements
        applicant_img = findViewById(R.id.applicant_image);
        applicant_name = findViewById(R.id.applicant_name);
        applicant_contact = findViewById(R.id.applicant_contact);
        applicant_message = findViewById(R.id.applicant_message);

        Button approveButton = findViewById(R.id.approve_button);
        Button rejectButton = findViewById(R.id.reject_button);
        Button acceptedButton = findViewById(R.id.accepted);

        // initial applicant card state
        approveButton.setVisibility(VISIBLE);
        rejectButton.setVisibility(VISIBLE);
        acceptedButton.setVisibility(INVISIBLE);

        // Handle the click event for the "approvedButton"
        approveButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                // TODO: backend implementation for approving applicant
                approveButton.setVisibility(INVISIBLE);
                rejectButton.setVisibility(INVISIBLE);
                acceptedButton.setVisibility(VISIBLE);
            }
        });

        // Handle the click event for the "rejectButton"
        rejectButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                // TODO: backend implementation for rejecting applicant
                // refresh and the applicant card will disappear
            }
        });

    }

    //public void setOnApplicantCardClickListener(OnClickListener listener) {
      //  setOnClickListener(listener);
    //}

    // Setter methods to update the UI elements
    // TODO: get and set applicant image
    public void setApplicantImg() {
        //applicant_img.setImageResource();
    }

    public void setApplicantName(String name) {
        applicant_name.setText(name);
    }

    public void setApplicantContact(String contact) {
        applicant_contact.setText("Contact: " + contact);
    }
    public void setApplicantMessage(String message) {
        applicant_message.setText(message);
    }

}

