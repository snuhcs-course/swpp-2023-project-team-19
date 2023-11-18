package com.example.gathernow.main_ui.event_applicant_info;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventDataSource;
import com.example.gathernow.main_ui.EventRepository;

import java.util.ArrayList;
import java.util.List;

public class ApplicantsInfoViewModel extends ViewModel {
    private final EventRepository eventRepository;
    private final MutableLiveData<List<ApplicationDataModel>> applicationList = new MutableLiveData<>();
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();

    public MutableLiveData<List<ApplicationDataModel>> getApplicationList() {
        return applicationList;
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public ApplicantsInfoViewModel() {
        this.eventRepository = new EventRepository(new EventDataSource());
    }

    public void fetchEventApplication(int eventId) {
        eventRepository.getEventApplication(eventId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                Log.d("ApplicantsInfoViewModel", "Loaded event application successfully");
                if (result instanceof List<?>) {
                    List<?> resultList = (List<?>) result;
                    Log.d("ApplicantsInfoViewModel", resultList.toString());
                    if (!resultList.isEmpty() && resultList.get(0) instanceof ApplicationDataModel) {
                        applicationList.setValue((List<ApplicationDataModel>) result);
                        Log.d("ApplicantsInfoViewModel", "Event application is not empty");
                    } else {
                        applicationList.setValue(new ArrayList<ApplicationDataModel>());
                    }
                }
            }

            @Override
            public void onError(String message) {
                alertMessage.setValue(message);
                applicationList.setValue(new ArrayList<ApplicationDataModel>());
            }
        });
    }



}
