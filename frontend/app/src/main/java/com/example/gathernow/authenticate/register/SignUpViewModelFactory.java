package com.example.gathernow.authenticate.register;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

public class SignUpViewModelFactory implements ViewModelProvider.Factory {
    private final SignUpRepository signUpRepository;
    private final Context context;

    public SignUpViewModelFactory(Context context, SignUpRepository signUpRepository) {
        this.signUpRepository = signUpRepository;
        this.context = context;
    }

    @Override
    public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignUpViewModel.class)) {
            return (T) new SignUpViewModel(context, signUpRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
