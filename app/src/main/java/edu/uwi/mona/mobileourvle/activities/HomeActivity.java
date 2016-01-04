package edu.uwi.mona.mobileourvle.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import edu.uwi.mona.mobileourvle.classes.models.MoodleFunction;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.fragments.OptionListFragment;

/*
 * @author Javon Davis
 */
public class HomeActivity extends AppCompatActivity implements OptionListFragment.OnOptionSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar homeToolbar = (Toolbar) findViewById(R.id.homeToolbar);
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

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onOptionSelected(int id) {
        Intent intent;
        switch(id+1)
        {
            case 1:
                intent = new Intent(this, CourseListActivity.class);

                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, ForumListActivity.class);

                //intent.putExtra(SharedConstants.ParcelKeys.USER_SESSION, new UserSessionParcel(mUserSession));

                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                break;
            case 3:
//                intent = new Intent(this,MessageListActivity.class);
//                startActivity(intent);

                Toast.makeText(this,"This section is not yet supported in the mobile version of OurVLE",Toast.LENGTH_LONG).show();
                break;
            case 4:
                intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "ourvle.mobile.feedback@gmail.com", null));
                intent.putExtra(Intent.EXTRA_TEXT,"Sent from OurVLE mobile");
                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
            case 5:
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
