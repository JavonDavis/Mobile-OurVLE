package edu.uwi.mona.mobileourvle.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.models.CourseForum;
import edu.uwi.mona.mobileourvle.fragments.ForumFragment;
import edu.uwi.mona.mobileourvle.fragments.ForumListFragment;

public class ForumActivity extends AppCompatActivity implements ForumFragment.OnDiscussionSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Toolbar toolbar = (Toolbar) findViewById(R.id.forum_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int forumId = getIntent().getExtras().getInt("forumId");

        List<CourseForum> forums = CourseForum.find(CourseForum.class,"forumid = ?",forumId+"");
        CourseForum forum = forums.get(0);
        TextView title = (TextView) findViewById(R.id.toolbar_title);

        String courseName = forum.getCoursename();
        String forumName = forum.getName();

        title.setText(courseName+" - "+forumName);
        title.setTextColor(getResources().getColor(R.color.white));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ForumFragment.newInstance(forumId))
                    .commit();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDiscussionSelected(int discussionId) {

        Intent intent = new Intent(this,PostListActivity.class);

        intent.putExtra("discussionid",discussionId);

        startActivity(intent);

    }
}
