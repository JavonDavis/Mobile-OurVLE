package edu.uwi.mona.mobileourvle.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.models.Message;
import edu.uwi.mona.mobileourvle.fragments.MessageListFragment;

public class MessageListActivity extends AppCompatActivity implements MessageListFragment.OnMessageSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        MessageListFragment fragment = MessageListFragment.newInstance();
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
        }
    }

    @Override
    public void onMessageSelected(Message message) {

    }
}
