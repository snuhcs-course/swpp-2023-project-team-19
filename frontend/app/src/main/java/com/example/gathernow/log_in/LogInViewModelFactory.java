package com.example.gathernow.log_in;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LogInViewModelFactory implements ViewModelProvider.Factory {
    private final AuthorizationRepository authorizationRepository;

    public LogInViewModelFactory(AuthorizationRepository authorizationRepository) {
        this.authorizationRepository = authorizationRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LogInViewModel.class)) {
            return (T) new LogInViewModel(authorizationRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
