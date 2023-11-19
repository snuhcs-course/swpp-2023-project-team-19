package com.example.gathernow.main_ui.cards;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventDataSource;
import com.example.gathernow.main_ui.EventRepository;

public class ApplicantCardViewModel extends ViewModel {
    private final EventRepository eventRepository;
    private final MutableLiveData<String> applicationStatus = new MutableLiveData<>();
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();

    public ApplicantCardViewModel() {
        this.eventRepository = new EventRepository(new EventDataSource());
    }

    public MutableLiveData<String> getApplicationStatus() {
        return applicationStatus;
    }

    public void acceptApplication(int applicationId) {
        eventRepository.acceptEventApplication(applicationId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                applicationStatus.setValue("Accepted");
                Log.d("ApplicantCardViewModel", "Application accepted");
            }

            @Override
            public void onError(String message) {
                alertMessage.setValue(message);
                Log.d("ApplicantCardViewModel", "Application accept failed");
            }
        });
    }

    public void rejectApplication(int applicationId) {
        eventRepository.rejectEventApplication(applicationId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                applicationStatus.setValue("Rejected");
                Log.d("ApplicantCardViewModel", "Application rejected");
            }

            @Override
            public void onError(String message) {
                alertMessage.setValue(message);
                Log.d("ApplicantCardViewModel", "Application reject failed");
            }
        });
    }
}
