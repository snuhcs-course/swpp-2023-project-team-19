package com.example.gathernow.main_ui;

public interface CallbackInterface {

    <T> void onSuccess(T result);

    void onError(String message);
}
