package com.mugswpp.gathernow.main_ui;

public interface CallbackInterface {

    <T> void onSuccess(T result);

    void onError(String message);
}
