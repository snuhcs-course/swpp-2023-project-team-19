package com.mugswpp.gathernow.main_ui.event_info;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mugswpp.gathernow.api.models.ApplicationDataModel;
import com.mugswpp.gathernow.api.models.EventDataModel;
import com.mugswpp.gathernow.api.models.UserDataModel;
import com.mugswpp.gathernow.main_ui.CallbackInterface;
import com.mugswpp.gathernow.main_ui.EventRepository;
import com.mugswpp.gathernow.main_ui.UserRemoteDataSource;
import com.mugswpp.gathernow.main_ui.UserRemoteRepository;

import java.util.Locale;
import java.util.Objects;

public class EventInfoViewModel extends ViewModel {
    private final EventRepository eventInfoRepository;
    private final UserRemoteRepository userRemoteRepository;
    private final MutableLiveData<EventDataModel> eventData = new MutableLiveData<>();
    private final MutableLiveData<ApplicationDataModel> applicationData = new MutableLiveData<>();
    private final MutableLiveData<UserDataModel> hostData = new MutableLiveData<>();
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showRegisterButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showResultButton = new MutableLiveData<>(false);
    private final MutableLiveData<String> applicationStatus = new MutableLiveData<>();
//    private final MutableLiveData<Boolean> showAcceptedButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showCancelRegButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showViewApplicantsButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showDeleteEventButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showDeleteEventSuccess = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showDeleteApplicationSuccess = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> clickableCancelButton = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> clickableRegisterButton = new MutableLiveData<>(true);


    public EventInfoViewModel(EventRepository eventInfoRepository) {
        this.eventInfoRepository = eventInfoRepository;
        this.userRemoteRepository = new UserRemoteRepository(new UserRemoteDataSource());

    }

    public MutableLiveData<Boolean> getClickableCancelButton() {
        return clickableCancelButton;
    }

    public MutableLiveData<Boolean> getClickableRegisterButton() {
        return clickableRegisterButton;
    }
    public MutableLiveData<String> getApplicationStatus() {
//        Log.d("EventInfo Testing", "Application status: " + applicationStatus.getValue());
        return applicationStatus;
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

    public MutableLiveData<Boolean> getShowResultButton() {
        return showResultButton;
    }

//    public MutableLiveData<Boolean> getShowAcceptedButton() {
//        return showAcceptedButton;
//    }

    public MutableLiveData<Boolean> getShowCancelRegButton() {
        return showCancelRegButton;
    }

    public MutableLiveData<Boolean> getShowViewApplicantsButton() {
        return showViewApplicantsButton;
    }

    public MutableLiveData<Boolean> getShowDeleteEventButton() {
        return showDeleteEventButton;
    }
    public MutableLiveData<Boolean> getShowDeleteEventSuccess() {
        return showDeleteEventSuccess;
    }
    public MutableLiveData<Boolean> getShowDeleteApplicationSuccess() {
        return showDeleteApplicationSuccess;
    }

    public MutableLiveData<ApplicationDataModel> getApplicationData() {
        return applicationData;
    }

    public void loadEventInfo(int eventId, int userId) {
        eventInfoRepository.getEventInfo(eventId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
//                Log.d("EventInfoViewModel", "Load event info successfully");
                EventDataModel res = (EventDataModel) result;
                if (res == null) {
                    alertMessage.postValue("Event not found");
                    return;
                }
                eventData.postValue(res);
                loadHostInfo(res.getHostId());
                setButtonVisibility(userId, res.getHostId(), res.getEventId());
//                Log.d("EventInfoViewModel", "Event longitude: " + res.getEventLongitude() + " Event latitude: " + res.getEventLatitude());

            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
            }
        });
    }

    public void setButtonVisibility(int userId, int hostId, int eventId) {
        if (hostId == userId) {
            showViewApplicantsButton.setValue(true);
            showDeleteEventButton.setValue(true);
        } else {
            eventInfoRepository.checkUserAppliedEvent(eventId, userId, new CallbackInterface() {
                @Override
                public <T> void onSuccess(T result) {
//                    Log.d("EventInfoViewModel", "Check user applied event successfully");
                    ApplicationDataModel res = (ApplicationDataModel) result;
                    applicationData.postValue(res);
                    int status = res.getRequestStatus();
                    if (status == 0) {
                        // application is pending
                        showResultButton.setValue(true);
                        applicationStatus.setValue("PENDING");
                        showCancelRegButton.setValue(true);
                        // When the deadline is passed, no cancellation is allowed
                        if (eventData.getValue() != null) {
                            if (deadlinePassed(Objects.requireNonNull(eventData.getValue()).getEventRegisterDate(), eventData.getValue().getEventRegisterTime())) {
                                clickableCancelButton.setValue(false);
                            }
                        }
                    } else if (status == 1) {
                        // application is accepted
                        showResultButton.setValue(true);
                        applicationStatus.setValue("ACCEPTED");
                        showCancelRegButton.setValue(true);
                        // When the deadline is passed, no cancellation is allowed
                        if (eventData.getValue() != null) {
                            if (deadlinePassed(Objects.requireNonNull(eventData.getValue()).getEventRegisterDate(), eventData.getValue().getEventRegisterTime())) {
                                clickableCancelButton.setValue(false);
                            }
                        }
                    } else if(status == 2){
                        // application is rejected
                        showRegisterButton.setValue(true);
                        // When the deadline passed or the event is full, no registration is allowed
                        Log.d("EventInfo Testing", Objects.requireNonNull(eventData.getValue()).getEventRegisterDate());
                        if (deadlinePassed(Objects.requireNonNull(eventData.getValue()).getEventRegisterDate(), eventData.getValue().getEventRegisterTime()) ||
                                reachedMaxParticipants(eventData.getValue().getEventNumJoined(), eventData.getValue().getEventNumParticipants())){
                            clickableRegisterButton.setValue(false);
                        }
                    }
                    else {
                        alertMessage.postValue("Application status not found");
                    }
                }

                @Override
                public void onError(String message) {
                    if (message.equals("No application found")) {
                        showRegisterButton.setValue(true);
                        // When the deadline passed or the event is full, no registration is allowed
//                        Log.d("EventInfo Testing", Objects.requireNonNull(eventData.getValue()).getEventRegisterDate());
                        if (eventData.getValue() != null) {
                            if (deadlinePassed(Objects.requireNonNull(eventData.getValue()).getEventRegisterDate(), eventData.getValue().getEventRegisterTime()) ||
                                    reachedMaxParticipants(eventData.getValue().getEventNumJoined(), eventData.getValue().getEventNumParticipants())){
                                clickableRegisterButton.setValue(false);
                            }
                        }

                    } else {
                        alertMessage.postValue(message);
                    }
                }
            });
        }

    }

    private boolean deadlinePassed(String event_register_date, String event_register_time) {
        // Get current date and time
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        java.util.Date currentDate = calendar.getTime();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US);
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss", Locale.US);
        String currentDateString = dateFormat.format(currentDate);
        String currentTimeString = timeFormat.format(currentDate);
//        Log.d("EventInfo Testing", "Current date: " + currentDateString);
//        Log.d("EventInfo Testing", "Current time: " + currentTimeString);
//        Log.d("EventInfo Testing", "Event register date: " + event_register_date);
//        Log.d("EventInfo Testing", "Event register time: " + event_register_time);

        // Compare current date and time with event registration deadline
        if (currentDateString.compareTo(event_register_date) > 0) {
            return true;
        }
        else if (currentDateString.compareTo(event_register_date) == 0) {
            return currentTimeString.compareTo(event_register_time) > 0;
        }
        return false;

    }

    // Check if the event has reached the maximum number of participants
    private boolean reachedMaxParticipants(int eventNumJoined, int eventNumParticipants) {
        return eventNumJoined >= eventNumParticipants;
    }

    public void loadHostInfo(int hostId) {
        userRemoteRepository.getUserInfo(hostId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
//                Log.d("EventInfoViewModel", "Load host info successfully");
                hostData.postValue((UserDataModel) result);
            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
            }
        });
    }

    public void deleteEvent(int eventId) {
        eventInfoRepository.deleteEvent(eventId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
//                Log.d("EventInfoViewModel", (String) result);
                showDeleteEventSuccess.postValue(true);
            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
            }
        });
    }

    public void deleteApplication() {
        if (applicationData == null) {
            alertMessage.postValue("Application data not found");
            return;
        }
        int applicationId = applicationData.getValue().getApplicationId();
        int eventId = applicationData.getValue().getEventId();
        int status = applicationData.getValue().getRequestStatus();

        eventInfoRepository.deleteApplication(applicationId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
//                Log.d("EventInfoViewModel", (String) result);
                showDeleteApplicationSuccess.postValue(true);
//                alertMessage.postValue((String) result);
            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
            }
        });

        if (status == 1){
            // If a confirmed application is deleted, decrease number of user joined for the event by 1
            eventInfoRepository.decreaseNumJoined(eventId, new CallbackInterface() {
                @Override
                public <T> void onSuccess(T result) {
                    Log.d("EventInfoViewModel", (String) result);
                }

                @Override
                public void onError(String message) {
                    alertMessage.postValue(message);
                }
            });
        }

    }

}
