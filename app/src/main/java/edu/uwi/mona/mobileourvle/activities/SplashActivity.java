package edu.uwi.mona.mobileourvle.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Boolean isEmpty = SiteInfo.listAll(SiteInfo.class).isEmpty();

        //If user still logged it skip login
        if(!isEmpty)
        {
            SiteInfo info = SiteInfo.listAll(SiteInfo.class).get(0);
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity(i);
            return;
        }

        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(i);
    }

}
