package com.mugswpp.gathernow.authenticate;

public interface AuthCallback {
    void onSuccess();
    void onError(String message);
}
