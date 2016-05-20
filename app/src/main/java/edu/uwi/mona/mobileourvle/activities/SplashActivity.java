package edu.uwi.mona.mobileourvle.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean noPreviousUser = SiteInfo.listAll(SiteInfo.class).isEmpty();

        new SplashTask().execute(noPreviousUser);
    }

    private class SplashTask extends AsyncTask<Boolean,Void,Boolean>
    {

        @Override
        protected Boolean doInBackground(Boolean... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(Boolean noPreviousUser) {
            if (noPreviousUser) {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SplashActivity.this.startActivity(i);
            } else {
                Intent i = new Intent(SplashActivity.this, OurVLEActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SplashActivity.this.startActivity(i);
            }
        }
    }

}
