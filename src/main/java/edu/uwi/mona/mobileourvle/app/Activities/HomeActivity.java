package edu.uwi.mona.mobileourvle.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants;
import edu.uwi.mona.mobileourvle.app.Fragments.OptionListFragment;
import edu.uwi.mona.mobileourvle.app.R;

/**
 * Created by javon_000 on 09/06/2015.
 */
public class HomeActivity extends ActionBarActivity implements OptionListFragment.OnOptionSelectedListener {

    private UserSession mUserSession;
    private Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);

        final Bundle extras = getIntent().getExtras();

        mUserSession = ((UserSessionParcel) extras
                .get(SharedConstants.ParcelKeys.USER_SESSION)).getWrappedObejct();

        homeToolbar = (Toolbar) findViewById(R.id.homeToolbar);
        setSupportActionBar(homeToolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.homeContainer, OptionListFragment.newInstance())
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_out_menu, menu);
        return true;
    }

    @Override
    public void onOptionSelected(String id) {
        Toast.makeText(this,"click",Toast.LENGTH_LONG).show();
        if(id.equals("1"))
        {
            final Intent intent = new Intent(HomeActivity.this, CourseListActivity.class);

            intent.putExtra(SharedConstants.ParcelKeys.USER_SESSION, new UserSessionParcel(mUserSession));

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
        else
            Toast.makeText(this,"You clicked option "+id,Toast.LENGTH_SHORT).show();
    }
}
