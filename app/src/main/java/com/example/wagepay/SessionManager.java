package com.example.wagepay;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_EXPIRY_TIMESTAMP = "expiryTimestamp";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void startSession(long sessionDurationMillis) {
        long currentTimestamp = System.currentTimeMillis();
        long expiryTimestamp = currentTimestamp + sessionDurationMillis;
        editor.putLong(KEY_EXPIRY_TIMESTAMP, expiryTimestamp);
        editor.apply();
    }

    public boolean isSessionValid() {
        long currentTimestamp = System.currentTimeMillis();
        long expiryTimestamp = sharedPreferences.getLong(KEY_EXPIRY_TIMESTAMP, 0);
        return currentTimestamp < expiryTimestamp;
    }

    public void endSession() {
        editor.remove(KEY_EXPIRY_TIMESTAMP);
        editor.apply();
    }
}


