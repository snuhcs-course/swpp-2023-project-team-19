package com.example.gathernow;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantCardView extends LinearLayout {

    private ImageView applicant_img;
    private ServiceApi service;
    private TextView applicant_name, applicant_contact, applicant_message;
    private Integer applicationId;
    private int requestStatus;

    public ApplicantCardView(Context context, AttributeSet attrs, int status, int eventId) {


        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.applicant_card_layout, this, true);

        this.requestStatus = status;

        // Initialize UI elements
        applicant_img = findViewById(R.id.applicant_image);
        applicant_name = findViewById(R.id.applicant_name);
        applicant_contact = findViewById(R.id.applicant_contact);
        applicant_message = findViewById(R.id.applicant_message);

        Button approveButton = findViewById(R.id.approve_button);
        Button rejectButton = findViewById(R.id.reject_button);
        Button acceptedButton = findViewById(R.id.accepted);

        approveButton.setVisibility(INVISIBLE);
        rejectButton.setVisibility(INVISIBLE);
        acceptedButton.setVisibility(INVISIBLE);

        if(requestStatus == 1){
            // Applicants already been accepted
            approveButton.setVisibility(INVISIBLE);
            rejectButton.setVisibility(INVISIBLE);
            acceptedButton.setVisibility(VISIBLE);

        }
        else{
            approveButton.setVisibility(VISIBLE);
            rejectButton.setVisibility(VISIBLE);
            acceptedButton.setVisibility(INVISIBLE);

            Log.e("ApplicationFeature", "Application accepted, status: "+ Integer.toString(requestStatus));

            service = RetrofitClient.getClient().create(ServiceApi.class);

            // Handle the click event for the "approvedButton"
            approveButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View view) {
                    // TODO: backend implementation for approving applicant

                    service.acceptStatus(applicationId).enqueue(new Callback<ApplicationData>(){
                        @Override
                        public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response){
                            if (response.isSuccessful()) {
                                Log.e("ApplicationFeature", "Application accepted"+ Integer.toString(response.code()));
                            }
                            else{
                                Log.e("ApplicationFeature", "Found no application to accept error occurred"+ Integer.toString(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApplicationData> call, Throwable t) {
                            Log.e("ApplicationFeature", "Application table retrieve error occurred", t);
                        }
                    });


                    service.increaseNumJoined(eventId).enqueue(new Callback<ApplicationData>(){
                        @Override
                        public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response){
                            if (response.isSuccessful()) {
                                Log.e("ApplicationFeature", "Increase num of people joined"+ Integer.toString(response.code()));
                            }
                            else{
                                Log.e("ApplicationFeature", "Found no application to accept error occurred"+ Integer.toString(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<ApplicationData> call, Throwable t) {
                            Log.e("ApplicationFeature", "Error in increasing participants number", t);
                        }
                    });

                    approveButton.setVisibility(INVISIBLE);
                    rejectButton.setVisibility(INVISIBLE);
                    acceptedButton.setVisibility(VISIBLE);
                }
            });

            // Handle the click event for the "rejectButton"
            rejectButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View view) {
                    // backend implementation for rejecting applicant
                    // the applicant card will disappear once deleted

                    // Remove the user card from its parent layout
                    ViewGroup parent = (ViewGroup) getParent();
                    parent.removeView(ApplicantCardView.this);

                    // Optionally, perform any additional cleanup if needed

                    service.delete_application(applicationId).enqueue(new Callback<ApplicationData>(){
                        @Override
                        public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response){
                            if (response.isSuccessful()) {
                                Log.e("ApplicationFeature", "Application deleted"+ Integer.toString(response.code()));
                            }
                            else{
                                Log.e("ApplicationFeature", "Found no application to delete error occurred"+ Integer.toString(response.code()));
                            }

                        }

                        @Override
                        public void onFailure(Call<ApplicationData> call, Throwable t) {
                            Log.e("ApplicationFeature", "Application table retrieve error occurred", t);
                        }
                    });




                    // Invalidate the layout to reflect the removal of the card
                    parent.invalidate();
                }
            });
        }




    }

    //public void setOnApplicantCardClickListener(OnClickListener listener) {
      //  setOnClickListener(listener);
    //}

    // Setter methods to update the UI elements
    // TODO: get and set applicant image
    public void setApplicantImg(String imageUrl) {
        //applicant_img.setImageResource();
        Picasso.get().load(imageUrl).into(applicant_img);
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

    public void setApplicationId(Integer application_id) {
        this.applicationId = application_id;


    }


}

