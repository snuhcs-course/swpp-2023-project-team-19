package com.example.gathernow.authenticate.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LogInViewModelFactory implements ViewModelProvider.Factory {
    private final LoginRepository loginRepository;

    public LogInViewModelFactory(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LogInViewModel.class)) {
            return (T) new LogInViewModel(loginRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
