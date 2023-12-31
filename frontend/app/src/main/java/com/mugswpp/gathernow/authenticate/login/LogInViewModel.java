package com.mugswpp.gathernow.authenticate.login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mugswpp.gathernow.authenticate.AuthCallback;

public class LogInViewModel extends ViewModel {
    private MutableLiveData<String> alertMessage = new MutableLiveData<>();
    private LoginRepository logInRepository;

    public LogInViewModel(LoginRepository logInRepository) {
        this.logInRepository = logInRepository;
    }

    public MutableLiveData<String> getAlertMessage() {
        return alertMessage;
    }

    public void logIn(String email, String password) {
        if (!isValidInput(email, password)) {
            return;
        }
        logInRepository.login(email, password, new AuthCallback() {
            @Override
            public void onSuccess() {
                alertMessage.postValue("Login successful");
                Log.d("Hello from LogInViewModel", "Login successful");
            }

            @Override
            public void onError(String message) {
                alertMessage.postValue(message);
                Log.d("Hello from LogInViewModel", message);
            }
        });
    }

    private boolean isValidInput(String email, String password) {
        if (email == null || email.isEmpty()) {
            alertMessage.postValue("Email cannot be empty");
            return false;
        }
        // check email input format
        if (!email.contains("@") || !email.contains(".")) {
            alertMessage.postValue("Input should be a valid email address");
            return false;
        }
        if (password == null || password.isEmpty()) {
            alertMessage.postValue("Password cannot be empty");
            return false;
        }
        return true;
    }

}
