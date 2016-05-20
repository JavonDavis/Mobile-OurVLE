package edu.uwi.mona.mobileourvle.classes.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.activities.OurVLEActivity;
import edu.uwi.mona.mobileourvle.classes.models.CourseForum;
import edu.uwi.mona.mobileourvle.classes.models.ForumDiscussion;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.classes.moodle.Token;
import edu.uwi.mona.mobileourvle.classes.moodle.SiteRestInfo;
import edu.uwi.mona.mobileourvle.classes.moodle.RestToken;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/6/15.
 */
public class LoginTask extends AsyncTask<String,String,Boolean> {

    private String username;
    private String password;
    private String token;
    private Context context;
    private SiteInfo siteInfo = new SiteInfo();
    private ProgressBar progressBar;
    private LinearLayout loginBox;

    public LoginTask(Context context, String username, String password, ProgressBar progressBar, LinearLayout loginBox)
    {
        this.context = context;
        this.username = username;
        this.password = password;
        this.progressBar = progressBar;
        this.loginBox = loginBox;

    }

    @Override
    protected void onPreExecute() {
        progressBar.setIndeterminate(true);
        loginBox.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onProgressUpdate(String... params) {
        if (params == null || params.length == 0)
            return;

        //Log.d("progress", params[0]);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        // Get token if required
        if (token == null)
            if (!getToken())
                return false;

        // Get Site info
        if (!getSiteInfo())
            return false;

        if(!getCourseInfo())
            return  false;

//        if(!getCourseContents())
//            return false;

        if(!getForums())
            return false;

//        if(!getDiscussions())
//            return false;
//
//        if(!getPosts())
//            return false;

        getUserImage(siteInfo.getUserpictureurl());

        return true;
    }

    private boolean getUserImage(String imageUrl)
    {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // Make directories if required
            File f = new File(Environment.getExternalStorageDirectory()
                    + "/OurVLE/");
            f.mkdirs();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(
                    Environment.getExternalStorageDirectory() + "/OurVLE/"
                            + "profile_pic");

            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    private boolean getCourseInfo() {
        CourseTask cTask = new CourseTask(token);

        publishProgress(context.getString(R.string.login_prog_sync_course));
        Boolean usrCourseSyncStatus = cTask.syncUserCourses();
        if (!usrCourseSyncStatus) {
            publishProgress(cTask.getError());
            publishProgress("\n"
                    + context.getString(R.string.login_prog_sync_failed));
        }

        // Success on user's course sync is what matters
        return usrCourseSyncStatus;
    }

    private boolean getCourseContents()
    {
        List<MoodleCourse> courses = MoodleCourse.listAll(MoodleCourse.class);
        CourseContentsTask task = new CourseContentsTask(token);

        for(MoodleCourse course:courses)
        {
            boolean status = task.syncCourseContents(course.getCourseid());

            publishProgress("Getting course contents");
        }

        return true;
    }

    private boolean getForums()
    {
        ForumTask task = new ForumTask(token);

        List<MoodleCourse> mCourses = MoodleCourse.listAll(MoodleCourse.class);
        ArrayList<String> courseIds = new ArrayList<String>();
        for (int i = 0; i < mCourses.size(); i++)
            courseIds.add(mCourses.get(i).getCourseid() + "");

        publishProgress("Getting forums");

        boolean status = task.syncForums(courseIds);
        return true;
    }

    private boolean getDiscussions()
    {
        List<CourseForum> forums = CourseForum.listAll(CourseForum.class);
        ArrayList<String> ids = new ArrayList<>();
        DiscussionTask task = new DiscussionTask(token);

        publishProgress("Getting discussions");
        for(CourseForum forum: forums)
        {
            ids.add(forum.getForumid()+"");
        }
        boolean status = task.syncDiscussions(ids);

        return true;
    }

    private boolean getPosts()
    {
        List<ForumDiscussion> discussions = ForumDiscussion.listAll(ForumDiscussion.class);
        ArrayList<Integer> ids = new ArrayList<>();

        PostTask task = new PostTask(token);

        for(ForumDiscussion discussion:discussions)
        {
            ids.add(discussion.getDiscussionid());
        }

        boolean status = task.syncPosts(ids);

        return true;
    }

    /**
     * Get token from username and password
     * @return true if token is retrieved, false otherwise
     */
    private Boolean getToken() {
        publishProgress("Fetching token");
        RestToken restToken = new RestToken(username, password);
        Token token = restToken.getToken();

        if(token == null)
        {
            return false;
        }

        this.token = token.getToken();
        return true;
    }

    /**
     * Retrieve Ourvle Site Information
     * @return
     */
    private Boolean getSiteInfo() {
        publishProgress("Fetching site info");
        SiteRestInfo siteRestInfo = new SiteRestInfo(token);
        siteInfo = siteRestInfo.getSiteInfo();
        if (siteInfo.getFullname() == null) {
            publishProgress(context
                    .getString(R.string.login_prog_siteinfo_failed));
            publishProgress("\n" + context.getString(R.string.login_errorcode)
                    + ": \n" + siteInfo.getErrorcode());

            return false;
        }
        siteInfo.setToken(token);
        siteInfo.save();

        publishProgress("\n" + "Login Success"
                + " " + siteInfo.getFullname() + "!\n");

        return true;
    }

    @Override
    protected void onPostExecute(Boolean status) {
        if (status) {
            Intent i = new Intent(context, OurVLEActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(i);
            return;
        } else {
            Toast.makeText(context,"login failed",Toast.LENGTH_LONG).show();
            loginBox.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}
