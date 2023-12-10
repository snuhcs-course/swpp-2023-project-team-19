package com.mugswpp.gathernow.main_ui.event_application_form;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mugswpp.gathernow.api.RetrofitClient;
import com.mugswpp.gathernow.api.ServiceApi;
import com.mugswpp.gathernow.api.models.ApplicationDataModel;
import com.mugswpp.gathernow.api.models.EventDataModel;
import com.mugswpp.gathernow.api.models.UserDataModel;
import com.mugswpp.gathernow.main_ui.CallbackInterface;
import com.mugswpp.gathernow.main_ui.EventDataSource;
import com.mugswpp.gathernow.main_ui.EventRepository;
import com.mugswpp.gathernow.main_ui.UserRemoteDataSource;
import com.mugswpp.gathernow.main_ui.UserRemoteRepository;

public class ApplicationFormViewModel extends ViewModel {
    ServiceApi service;
    EventRepository eventRepository;
    UserRemoteRepository userRemoteRepository;
    private MutableLiveData<EventDataModel> eventData = new MutableLiveData<>();
    private MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final MutableLiveData<UserDataModel> applicantData = new MutableLiveData<>();
    public ApplicationFormViewModel() {
        service = RetrofitClient.getClient().create(ServiceApi.class);
        eventRepository = new EventRepository(new EventDataSource());
        userRemoteRepository = new UserRemoteRepository(new UserRemoteDataSource());
    }

    public MutableLiveData<UserDataModel> getApplicantData() {
        return applicantData;
    }

    public void fetchUserData(int userId) {
        userRemoteRepository.getUserInfo(userId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                applicantData.setValue((UserDataModel) result);
            }

            @Override
            public void onError(String message) {
                alertMessage.setValue(message);
            }
        });
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public void applyEvent(ApplicationDataModel applicationDataModel) {
        if (isInputValid(applicationDataModel)) {
            eventRepository.applyEvent(applicationDataModel, new CallbackInterface() {
                @Override
                public <T> void onSuccess(T result) {
                    alertMessage.setValue((String) result);
                }

                @Override
                public void onError(String message) {
                    alertMessage.setValue(message);
                }
            });
        }
    }

    private boolean isInputValid(ApplicationDataModel applicationDataModel) {
        if (applicationDataModel.getApplicantContact().isEmpty()) {
            alertMessage.setValue("Please enter your contact information");
            return false;
        } else if (applicationDataModel.getMessage().isEmpty()) {
            alertMessage.setValue("Please enter your message");
            return false;
        } else {
            return true;
        }
    }


}




