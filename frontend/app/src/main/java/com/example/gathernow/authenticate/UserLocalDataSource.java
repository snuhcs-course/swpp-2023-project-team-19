package com.example.gathernow.authenticate;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalDataSource {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "user_id";

    public UserLocalDataSource(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void storeUserId(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

}
