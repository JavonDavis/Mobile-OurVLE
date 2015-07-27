/**
 *
 */
package edu.uwi.mona.mobileourvle.activities;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.models.CourseModule;
import edu.uwi.mona.mobileourvle.classes.models.ModuleContent;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.fragments.CourseContentsFragment;

/**
 * @author Javon
 */
public class CourseContentsActivity extends AppCompatActivity
        implements CourseContentsFragment.Listener {

    private MoodleCourse mCourse;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_main);

        final Bundle extras = getIntent().getExtras();

        int courseid = extras.getInt("courseid");

        List<MoodleCourse> courses = MoodleCourse.find(MoodleCourse.class, "courseid = ?", courseid + "");

        mCourse = courses.get(0);

        TextView title = (TextView) findViewById(R.id.course_title);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.course_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title.setText(mCourse.getShortname()+" Resources");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, CourseContentsFragment.newInstance(courseid))
                    .commit();
        }
    }


//    @Override
//    protected void onResume() {
//        if (mOnDiscussionSeclectedReceiver == null)
//            mOnDiscussionSeclectedReceiver = new FragmentResponseListerner() {
//                @Override
//                public void onResponseReceived(final Context context, final Bundle data) {
//
//                    final ForumDiscussion discussion = ((ForumDiscussionParcel) data
//                            .getParcelable(ForumDiscussionListFragment.ResponseArgs.Discussion))
//                            .getWrappedObejct();
//
//                    final Intent intent = new Intent(CourseContentsActivity.this,
//                            ForumDiscussionPagerActivity.class);
//
//                    intent.putExtra(ParcelKeys.USER_SESSION,
//                            new UserSessionParcel(mUserSession));
//
//                    intent.putExtra(ParcelKeys.FORUM_DISCUSSION_ID,
//                            discussion.getId());
//
//                    intent.putExtra(ParcelKeys.PARENT,
//                            new CourseForumParcel(mUserSession.getContext()
//                                    .getSiteInfo()
//                                    .getNewsForum())
//                    );
//
//                    intent.putExtra(ParcelKeys.PARENT,
//                            new DiscussionParentParcel(
//                                    new DiscussionParent(
//                                            mUserSession.getContext()
//                                                    .getSiteInfo()
//                                                    .getNewsForum()
//                                    )
//                            )
//                    );
//
//                    intent.putExtra(ParcelKeys.FORUM_DISCUSSION_ID,
//                            discussion.getId());
//
//                    startActivity(intent);
//                }
//            };
//
//        FragmentResponseManager.registerReceiver(getApplicationContext(),
//                ForumDiscussionListFragment.Responses.onForumSelected,
//                mOnDiscussionSeclectedReceiver);
//
//        super.onResume();
//    }

    @Override
    protected void onPause() {
//        if (mOnDiscussionSeclectedReceiver != null)
//            FragmentResponseManager.unregisterReceiver(getApplicationContext(),
//                    mOnDiscussionSeclectedReceiver);
        super.onPause();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_course_contents, menu);

        this.menu = menu;
        return true;
    }*/


    static File courseFile;
    @Override
    public void onCourseModuleSelected(final CourseModule module) {
        List<ModuleContent> contents = module.getContents();
        ModuleContent content = contents.get(0);
        if ("resource".equalsIgnoreCase(module.getModname())) {

            /**
             * Files are stored in the folder /Ourvle/courses/Modified Course Name-COurse Id/files/
             * The modified course name removes any uneccesary whitespace from the name along with some course names may contain the colon
             * The colon is a reserved character in the android library and causes an illegal state exception when explicitly creating a file with that
             * in the name
             *
             * The android reserved characters for file names are  {"|", "\\", "?", "*", "<", "\"", ":", ">"};
            */
            String folderLocation = "/OurVLE/courses/"+mCourse.getShortname().trim().replaceAll(":","-")+"-"+mCourse.getId()+"/files/"; // sub-folder definition

            File location = new File(Environment.getExternalStorageDirectory(), folderLocation);

            if (!location.exists()) {
                location.mkdirs();  // makes the subfolder
            }


            String fileLocation = location+"/"+content.getFilename();
            courseFile = new File(fileLocation);

            if(!courseFile.exists()) // checks if the file is not already present
            {
                Toast.makeText(getApplicationContext(), "Downloading File: " + module.getName(),
                        Toast.LENGTH_LONG).show();
                String token = SiteInfo.listAll(SiteInfo.class).get(0).getToken();
                final String url = module.getUrl() + "&token=" + token;
                final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setDescription("Course file download");
                request.setTitle(module.getName());
                // in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(folderLocation,
                        content.getFilename());

                // get download service and enqueue file
                final DownloadManager manager = (DownloadManager) getSystemService(
                        Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);

                //registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
            else // open the file that is already present
            {
                openFile(courseFile);
            }
        } else if (content.getFileurl() != null) {

            final String url = content.getFileurl();
            //final Intent webviewIntent = new Intent(this,CourseContentsResourceActivity.class);
            //webviewIntent.putExtra("URL",url);

            //startActivity(webviewIntent);
            Toast.makeText(this,"not a resource", Toast.LENGTH_LONG).show();
        } else //noinspection StatementWithEmptyBody
            if ("label".equalsIgnoreCase(module.getModname())) {
                // do nothing
            } else {
                Toast.makeText(getApplicationContext(),
                        "This resource is not yet supported by OurVLE Mobile", Toast.LENGTH_SHORT).show();
            }

    }

    private void openFile(final File courseFile)
    {
        Uri path = Uri.fromFile(courseFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(path);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try
        {
            startActivity(intent);
            //TODO - unRegister();
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(getApplicationContext(), "no application available to view this file",
                    Toast.LENGTH_LONG).show();
        }
    }

}