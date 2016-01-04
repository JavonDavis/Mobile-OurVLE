package edu.uwi.mona.mobileourvle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.fragments.ForumListFragment;

public class ForumListActivity extends AppCompatActivity implements ForumListFragment.OnForumSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.forum_list_toolbar);
        setSupportActionBar(toolbar);

        //Doesn't hurt
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ForumListFragment.newInstance(0)).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onForumSelected(int id) {
        Intent intent = new Intent(this,ForumActivity.class);
        intent.putExtra("forumId",id);
        startActivity(intent);
    }
}
