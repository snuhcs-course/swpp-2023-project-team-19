package com.example.gathernow.main_ui.cards;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;

import com.example.gathernow.R;
import com.squareup.picasso.Picasso;

public class ApplicantCardView extends LinearLayout {
    private final ImageView applicantImgView;
    private final TextView applicantNameView, applicantContactView, applicantMessageView;
    private Integer applicationId;
    private final Button approveButton, rejectButton, acceptedButton;
    private final ApplicantCardViewModel applicantCardViewModel;

    public ApplicantCardView(Context context, AttributeSet attrs, int status, int eventId) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.applicant_card_layout, this, true);

        // Initialize UI elements
        applicantImgView = findViewById(R.id.applicant_image);
        applicantNameView = findViewById(R.id.applicant_name);
        applicantContactView = findViewById(R.id.applicant_contact);
        applicantMessageView = findViewById(R.id.applicant_message);

        approveButton = findViewById(R.id.approve_button);
        rejectButton = findViewById(R.id.reject_button);
        acceptedButton = findViewById(R.id.accepted);

        // View model
        applicantCardViewModel = new ApplicantCardViewModel();
        applicantCardViewModel.getApplicationStatus().observe((LifecycleOwner) context, applicationStatus -> {
            if (applicationStatus.equals("Accepted") || applicationStatus.equals("Rejected")) {
                // Applicants already been accepted
                approveButton.setVisibility(INVISIBLE);
                rejectButton.setVisibility(INVISIBLE);
                acceptedButton.setVisibility(VISIBLE);
                acceptedButton.setText(applicationStatus);
                if (applicationStatus.equals("Rejected")) {
                    acceptedButton.setEnabled(false);
                }
            } else {
                approveButton.setVisibility(VISIBLE);
                rejectButton.setVisibility(VISIBLE);
                acceptedButton.setVisibility(INVISIBLE);
            }
        });

        initializeButtons(status, eventId);

    }

    private void initializeButtons(int status, int eventId) {
        if (status == 1) {
            // Applicants already been accepted
            approveButton.setVisibility(INVISIBLE);
            rejectButton.setVisibility(INVISIBLE);
            acceptedButton.setVisibility(VISIBLE);
            acceptedButton.setText("Accepted");
        } else {
            approveButton.setVisibility(VISIBLE);
            rejectButton.setVisibility(VISIBLE);
            acceptedButton.setVisibility(INVISIBLE);

            approveButton.setOnClickListener(view -> applicantCardViewModel.acceptApplication(applicationId, eventId));
            rejectButton.setOnClickListener(view -> applicantCardViewModel.rejectApplication(applicationId));
        }
    }


    // Setter methods to update the UI elements
    // TODO: get and set applicant image
    public void setApplicantImg(String imageUrl) {
        //applicant_img.setImageResource();
        Picasso.get().load(imageUrl).into(applicantImgView);
    }

    public void setApplicantName(String name) {
        applicantNameView.setText(name);
    }

    @SuppressLint("SetTextI18n")
    public void setApplicantContact(String contact) {
        applicantContactView.setText("Contact: " + contact);
    }

    public void setApplicantMessage(String message) {
        applicantMessageView.setText(message);
    }

    public void setApplicationId(Integer application_id) {
        this.applicationId = application_id;
    }




}

