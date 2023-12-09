package com.mugswpp.gathernow.api.models;

public class ApplicationDataModelBuilder {
    // builder for ApplicationDataModel
    private ApplicationDataModel applicationDataModel;
    public ApplicationDataModelBuilder() {
        applicationDataModel = new ApplicationDataModel();
    }

    public ApplicationDataModelBuilder setApplicantContact(String applicant_contact) {
        applicationDataModel.setApplicantContact(applicant_contact);
        return this;
    }

    public ApplicationDataModelBuilder setMessage(String message) {
        applicationDataModel.setMessage(message);
        return this;
    }

    public ApplicationDataModelBuilder setApplicantId(Integer applicant_id) {
        applicationDataModel.setApplicantId(applicant_id);
        return this;
    }

    public ApplicationDataModelBuilder setEventId(Integer event_id) {
        applicationDataModel.setEventId(event_id);
        return this;
    }

    public ApplicationDataModelBuilder setHostId(Integer host_id) {
        applicationDataModel.setHostId(host_id);
        return this;
    }

    public ApplicationDataModelBuilder setApplicantName(String applicant_name) {
        applicationDataModel.setApplicantName(applicant_name);
        return this;
    }

    public ApplicationDataModelBuilder setApplicantAvatar(String applicant_avatar) {
        applicationDataModel.setApplicantAvatar(applicant_avatar);
        return this;
    }

    public ApplicationDataModelBuilder setApplicationId(Integer application_id) {
        applicationDataModel.setApplicationId(application_id);
        return this;
    }

    public ApplicationDataModelBuilder setRequestStatus(Integer request_status) {
        applicationDataModel.setRequestStatus(request_status);
        return this;
    }

    public ApplicationDataModel build() {
        return applicationDataModel;
    }

}
