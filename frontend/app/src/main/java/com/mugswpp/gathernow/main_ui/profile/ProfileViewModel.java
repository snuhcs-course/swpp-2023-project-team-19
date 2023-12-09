package com.mugswpp.gathernow.main_ui.profile;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mugswpp.gathernow.api.models.EventDataModel;
import com.mugswpp.gathernow.api.models.UserDataModel;
import com.mugswpp.gathernow.authenticate.UserLocalDataSource;
import com.mugswpp.gathernow.main_ui.CallbackInterface;
import com.mugswpp.gathernow.main_ui.EventDataSource;
import com.mugswpp.gathernow.main_ui.EventRepository;
import com.mugswpp.gathernow.main_ui.UserRemoteDataSource;
import com.mugswpp.gathernow.main_ui.UserRemoteRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileViewModel extends ViewModel {
    private UserLocalDataSource userLocalDataSource;
    private UserRemoteRepository userRemoteRepository;
    private EventRepository eventRepository;
    private Context context;
    private final MutableLiveData<UserDataModel> userData = new MutableLiveData<>();
    private final MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private final MutableLiveData<List<EventDataModel>> userEvents = new MutableLiveData<>();

    public ProfileViewModel(Context context) {
        this.context = context;
        this.userLocalDataSource = new UserLocalDataSource(context);
        this.userRemoteRepository = new UserRemoteRepository(new UserRemoteDataSource());
        this.eventRepository = new EventRepository(new EventDataSource());
    }

    public MutableLiveData<UserDataModel> getUserData() {
        return userData;
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public MutableLiveData<List<EventDataModel>> getUserEvents() {
        return userEvents;
    }

    public void fetchUserProfile(UserLocalDataSource userLocalDataSource) {
        if (userLocalDataSource.getUserId() == null) {
            Log.d("ProfileViewModel", "User not logged in");
            return;
        }
        int userId = Integer.parseInt(userLocalDataSource.getUserId());
        userRemoteRepository.getUserInfo(userId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                Log.d("ProfileViewModel", "Loaded user info");
                userData.postValue((UserDataModel) result);
                fetchUserEvents(userId);
            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
            }
        });
    }

    public void fetchUserEvents(int userId) {
        eventRepository.getUserEvents(userId, new CallbackInterface() {
            @Override
            public <T> void onSuccess(T result) {
                Log.d("ProfileViewModel", "Loaded user events successfully");
                if (result instanceof List<?>) {
                    List<?> resultList = (List<?>) result;
                    if (!resultList.isEmpty() && resultList.get(0) instanceof EventDataModel) {
                        List<EventDataModel> events = (List<EventDataModel>) resultList;
                        Collections.reverse(events);
                        userEvents.postValue(events);
                    } else {
                        userEvents.postValue(new ArrayList<EventDataModel>());
                    }
                }
            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
                userEvents.postValue(new ArrayList<EventDataModel>());
            }
        });
    }

}
