/**
 *
 */
package edu.uwi.mona.mobileourvle.activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.adapters.CoursePagerAdapter;
import edu.uwi.mona.mobileourvle.classes.models.CourseModule;
import edu.uwi.mona.mobileourvle.classes.models.CourseParticipant;
import edu.uwi.mona.mobileourvle.classes.models.Message;
import edu.uwi.mona.mobileourvle.classes.models.ModuleContent;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.classes.moodle.MoodleFunctions;
import edu.uwi.mona.mobileourvle.classes.moodle.MoodleRestMessage;
import edu.uwi.mona.mobileourvle.fragments.CourseContentsFragment;
import edu.uwi.mona.mobileourvle.fragments.ForumListFragment;
import edu.uwi.mona.mobileourvle.fragments.ParticipantsListFragment;

/**
 * @author Javon
 */
public class CourseContentsActivity extends AppCompatActivity
        implements CourseContentsFragment.Listener ,ForumListFragment.OnForumSelectedListener,ParticipantsListFragment.OnParticipantSelectedListener{

    private MoodleCourse mCourse;
    private SiteInfo siteInfo;


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

        if(getSupportActionBar()!= null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        siteInfo = SiteInfo.listAll(SiteInfo.class).get(0);

        title.setText(mCourse.getShortname());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Get the ViewPager and set it's PagerAdapter so that it can display course option tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CoursePagerAdapter(getSupportFragmentManager(),courseid));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    static File courseFile;
    @Override
    public void onCourseModuleSelected(final CourseModule module) {
        List<ModuleContent> contents = module.getContents();
        ModuleContent content = new ModuleContent();

        if(!contents.isEmpty()) {
            content = contents.get(0);
        }

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
                if(!location.mkdirs())
                    Toast.makeText(this,"Error making directory to store file",Toast.LENGTH_LONG).show();  // makes the subfolder
            }


            String fileLocation = location+"/"+content.getFilename();
            courseFile = new File(fileLocation);

            if(!courseFile.exists()) // checks if the file is not already present
            {
                Toast.makeText(getApplicationContext(), "Downloading File: " + module.getName(),
                        Toast.LENGTH_LONG).show();
                String token = SiteInfo.listAll(SiteInfo.class).get(0).getToken();
                final String url = content.getFileurl() + "&token=" + token;
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
        } else if (module.getUrl() != null) {

            String url = module.getUrl();

            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;

            url+=("?wstoken=" + siteInfo.getToken());

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } else //noinspection StatementWithEmptyBody
            if ("label".equalsIgnoreCase(module.getModname())) {
                // do nothing
            } else {
                Log.d("resource","invalid");
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

    @Override
    public void onForumSelected(int id) {
        Intent intent = new Intent(this,ForumActivity.class);
        intent.putExtra("forumId",id);
        startActivity(intent);
    }

    @Override
    public void onParticipantSelected(final CourseParticipant participant) {

        new AlertDialog.Builder(this)
                .setTitle(participant.getFullname())
                .setMessage("Would you like to email or send a message through OurVLE to "+participant.getFullname())
                .setCancelable(true)
                .setPositiveButton("Send Message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final EditText input = new EditText(CourseContentsActivity.this);

                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        input.setPadding(50,50,50,50);

                        new AlertDialog.Builder(CourseContentsActivity.this)
                                .setTitle("Message to "+participant.getFullname())
                                .setView(input)
                                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new MessageSender(siteInfo.getToken(),
                                                participant.getUserid(), input.getText().toString())
                                                .execute("");
                                    }
                                })
                                .setNegativeButton("Cancel",null).show();
                    }
                })
                .setNegativeButton("Send Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Toast.makeText(CourseContentsActivity.this,"Send email",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "ourvle.mobile.feedback@gmail.com", null));
                        intent.putExtra(Intent.EXTRA_TEXT,"Sent from OurVLE mobile");
                        startActivity(Intent.createChooser(intent, "Send Email"));
                    }
                })
                .setNeutralButton("Cancel",null).show();
    }

    private class MessageSender extends
            AsyncTask<String, Integer, Boolean> {
        String mUrl;
        String token;
        int userid;
        String message;
        MoodleRestMessage mrm;

        public MessageSender(String token, int userid,
                                  String message) {
            this.mUrl = MoodleFunctions.API_HOST;
            this.token = token;
            this.userid = userid;
            this.message = message;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            mrm = new MoodleRestMessage(mUrl, token);
            Message mMessage = new Message(userid, message + "\n"
                    + "Sent from Mobile OurVLE");
            return mrm.sendMessage(mMessage);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result)
                Toast.makeText(CourseContentsActivity.this,
                        "Message sending failed. Error: " + mrm.getError(),
                        Toast.LENGTH_LONG).show();
            else
                Toast.makeText(CourseContentsActivity.this, "Message sent!", Toast.LENGTH_SHORT)
                        .show();
        }

    }
}