package edu.uwi.mona.mobileourvle.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;

import java.io.File;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.helpers.ImageDecoder;
import edu.uwi.mona.mobileourvle.classes.models.CourseForum;
import edu.uwi.mona.mobileourvle.classes.models.CourseModule;
import edu.uwi.mona.mobileourvle.classes.models.CourseSection;
import edu.uwi.mona.mobileourvle.classes.models.DiscussionPost;
import edu.uwi.mona.mobileourvle.classes.models.ForumDiscussion;
import edu.uwi.mona.mobileourvle.classes.models.ModuleContent;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourseFormatOption;
import edu.uwi.mona.mobileourvle.classes.models.MoodleFunction;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.fragments.OptionListFragment;

/*
 * @author Javon Davis
 */
public class HomeActivity extends AppCompatActivity implements OptionListFragment.OnOptionSelectedListener{

    private Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Bundle extras = getIntent().getExtras();

        homeToolbar = (Toolbar) findViewById(R.id.homeToolbar);
        setSupportActionBar(homeToolbar);

        CircularImageView imageView = (CircularImageView) findViewById(R.id.profile_pic);

        Bitmap userImage = ImageDecoder.decodeImage(new File(
                Environment.getExternalStorageDirectory() + "/OurVLE/profile_pic"));
        if (userImage != null)
            imageView.setImageBitmap(userImage);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.homeContainer, OptionListFragment.newInstance())
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onOptionSelected(int id) {
        //Toast.makeText(this, "click", Toast.LENGTH_LONG).show();
        Intent intent;
        switch(id+1)
        {
            case 1:
                intent = new Intent(HomeActivity.this, CourseListActivity.class);

                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                break;
            case 3:
                intent = new Intent(HomeActivity.this, ForumListActivity.class);

                //intent.putExtra(SharedConstants.ParcelKeys.USER_SESSION, new UserSessionParcel(mUserSession));

                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                break;
            case 4:
                showLogoutDialog();
                break;
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.log_out)
                .setMessage(R.string.log_out_prompt)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        doLogout();

                        final Intent intent = new Intent(HomeActivity.this, LoginActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                        finish();
                        startActivity(intent);
                    }

                    private void doLogout() {
                        SiteInfo.deleteAll(SiteInfo.class);
                        MoodleCourse.deleteAll(MoodleCourse.class);
                        CourseForum.deleteAll(CourseForum.class);
                        MoodleFunction.deleteAll(MoodleFunction.class);
                        ModuleContent.deleteAll(ModuleContent.class);
                        ForumDiscussion.deleteAll(ForumDiscussion.class);
                        CourseModule.deleteAll(CourseModule.class);
                        CourseSection.deleteAll(CourseSection.class);
                        DiscussionPost.deleteAll(DiscussionPost.class);

                        File picture = new File(Environment.getExternalStorageDirectory() + "/OurVLE/"
                                + "profile_pic");

                        if(picture.exists())
                            picture.delete();

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
