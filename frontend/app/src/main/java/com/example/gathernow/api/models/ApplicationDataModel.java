package com.example.gathernow.api.models;

import com.google.gson.annotations.SerializedName;

public class ApplicationDataModel {
    @SerializedName("applicant_contact")
    String applicant_contact;

    @SerializedName("message")
    String message;

    @SerializedName("applicant_id")
    Integer applicant_id;

    @SerializedName("applicant_name")
    String applicant_name;

    @SerializedName("event_id")
    Integer event_id;

    @SerializedName("host_id")
    Integer host_id;

    @SerializedName("applicant_avatar")
    String applicant_avatar;

    @SerializedName("request_status")
    Integer request_status;

    @SerializedName("application_id")
    Integer application_id;

    public ApplicationDataModel(String applicant_contact, String message, Integer applicant_id,
                                Integer event_id, Integer host_id, String applicant_name, String applicant_avatar){
        this.applicant_contact = applicant_contact;
        this.message = message;
        this.applicant_id = applicant_id;
        this.applicant_name = applicant_name;
        this.event_id = event_id;
        this.host_id = host_id;
        this.applicant_avatar = applicant_avatar;
    }
    public ApplicationDataModel() {

    }

    public int getRequestStatus() {
        return request_status;
    }

    public int getApplicationId() {
        return application_id;
    }

    // get applicant_name, applicant_avatar, applicant_contact
    public String getApplicantName() {
        return applicant_name;
    }

    public String getApplicantAvatar() {
        return applicant_avatar;
    }

    public String getApplicantContact() {
        return applicant_contact;
    }

    public String getMessage() {
        return message;
    }

    public Integer getEventId() {
        return event_id;
    }

    // set variables
    public void setApplicantName(String applicant_name) {
        this.applicant_name = applicant_name;
    }

    public void setApplicantAvatar(String applicant_avatar) {
        this.applicant_avatar = applicant_avatar;
    }

    public void setApplicantContact(String applicant_contact) {
        this.applicant_contact = applicant_contact;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEventId(Integer event_id) {
        this.event_id = event_id;
    }

    public void setHostId(Integer host_id) {
        this.host_id = host_id;
    }

    public void setApplicantId(Integer applicant_id) {
        this.applicant_id = applicant_id;
    }

    public void setRequestStatus(Integer request_status) {
        this.request_status = request_status;
    }

    public void setApplicationId(Integer application_id) {
        this.application_id = application_id;
    }

}
