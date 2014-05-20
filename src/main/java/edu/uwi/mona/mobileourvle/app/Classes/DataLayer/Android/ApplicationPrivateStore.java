package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Android;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author ahamidev@gmail.com (Aston Hamilton)
 */
public class ApplicationPrivateStore {

    public static void put(final Context pContext, final String key, final String value) {
        final SharedPreferences settings = pContext.getSharedPreferences(
                SavedArguments.STORE_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();
        editor.putString(SavedArguments.PREFIX + key, value);

        // Commit the edits!
        editor.commit();
    }

    public static String get(final Context pContext, final String key) {
        try {
            final SharedPreferences settings =
                    pContext.getSharedPreferences(
                            SavedArguments.STORE_NAME, 0);
            return settings.getString(SavedArguments.PREFIX + key, "");
        } catch (final ClassCastException e) {
            Log.e(ApplicationPrivateStore.class.toString(),
                  "Unexpected Class Cast Exception", e);
            return "";
        }
    }

    private abstract static class SavedArguments {
        public static final String STORE_NAME = "edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Android.ApplicationPrivateStore.SotreName";

        public static final String PREFIX = "edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Android.ApplicationPrivateStore.quick_setting.";

    }
}
