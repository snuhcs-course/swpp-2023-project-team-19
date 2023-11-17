package com.example.gathernow.main_ui.event_info;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gathernow.api.models.ApplicationDataModel;
import com.example.gathernow.api.models.EventDataModel;
import com.example.gathernow.api.models.UserDataModel;
import com.example.gathernow.main_ui.CallbackInterface;
import com.example.gathernow.main_ui.EventRepository;
import com.example.gathernow.main_ui.UserRemoteDataSource;
import com.example.gathernow.main_ui.UserRemoteRepository;

public class EventInfoViewModel extends ViewModel {
    private final EventRepository eventInfoRepository;
    private final UserRemoteRepository userRemoteRepository;
    private final MutableLiveData<EventDataModel> eventData = new MutableLiveData<>();
    private final MutableLiveData<UserDataModel> hostData = new MutableLiveData<>();
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showRegisterButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showWaitingButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showAcceptedButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showCancelRegButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showViewApplicantsButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showDeleteEventButton = new MutableLiveData<>(false);


    public EventInfoViewModel(EventRepository eventInfoRepository) {
        this.eventInfoRepository = eventInfoRepository;
        this.userRemoteRepository = new UserRemoteRepository(new UserRemoteDataSource());

    }

    public MutableLiveData<EventDataModel> getEventData() {
        return eventData;
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public MutableLiveData<UserDataModel> getHostData() {
        return hostData;
    }

    public MutableLiveData<Boolean> getShowRegisterButton() {
        return showRegisterButton;
    }

    public MutableLiveData<Boolean> getShowWaitingButton() {
        return showWaitingButton;
    }

    public MutableLiveData<Boolean> getShowAcceptedButton() {
        return showAcceptedButton;
    }

    public MutableLiveData<Boolean> getShowCancelRegButton() {
        return showCancelRegButton;
    }

    public MutableLiveData<Boolean> getShowViewApplicantsButton() {
        return showViewApplicantsButton;
    }

    public MutableLiveData<Boolean> getShowDeleteEventButton() {
        return showDeleteEventButton;
    }

    public void loadEventInfo(int eventId, int userId) {
        eventInfoRepository.getEventInfo(eventId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                Log.d("EventInfoViewModel", "Load event info successfully");
                EventDataModel res = (EventDataModel) result;
                eventData.postValue(res);
                loadHostInfo(res.getHostId());
                setButtonVisibility(userId, res.getHostId(), res.getEventId());
            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
            }
        });
    }

    private void setButtonVisibility(int userId, int hostId, int eventId) {
        if (hostId == userId) {
            showViewApplicantsButton.setValue(true);
            showDeleteEventButton.setValue(true);
        } else {
            eventInfoRepository.checkUserAppliedEvent(userId, eventId, new CallbackInterface() {
                @Override
                public <T> void onSuccess(T result) {
                    Log.d("EventInfoViewModel", "Check user applied event successfully");
                    int status = ((ApplicationDataModel) result).getRequestStatus();
                    if (status == 0) {
                        // application is pending
                        showWaitingButton.setValue(true);
                        showCancelRegButton.setValue(true);
                    } else if (status == 1) {
                        // application is accepted
                        showAcceptedButton.setValue(true);
                        showCancelRegButton.setValue(true);
                    } else {
                        alertMessage.postValue("Application status not found");
                    }
                }

                @Override
                public void onError(String message) {
                    if (message.equals("No application found")) {
                        showRegisterButton.setValue(true);
                    } else {
                        alertMessage.postValue(message);
                    }
                }
            });
        }

    }

    public void loadHostInfo(int hostId) {
        userRemoteRepository.getUserInfo(hostId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                Log.d("EventInfoViewModel", "Load host info successfully");
                hostData.postValue((UserDataModel) result);
            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
            }
        });
    }



}
