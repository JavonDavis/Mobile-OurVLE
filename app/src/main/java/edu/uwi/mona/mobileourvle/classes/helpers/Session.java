package edu.uwi.mona.mobileourvle.classes.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/27/15.
 */
public class Session {

    private SharedPreferences sharedPrefs;
    private static final String APP_SHARED_PREFS = "Preferences";
    private SharedPreferences.Editor prefsEditor;
    private String token;

    public Session (Context context)
    {
        this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
                Activity.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();

        setCurrentData();
    }

    private void setCurrentData() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
