/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers.CoursePhotosOpenHelper;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers.CourseVideosOpenHelper;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;
import edu.uwi.mona.mobileourvle.app.Classes.Dialogs.CourseListOptionsDialogFragment;
import edu.uwi.mona.mobileourvle.app.Classes.Dialogs.CourseMediaOptionsDialogFragment;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.ForumDiscussionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleUserParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.CourseClasses.CourseClassTimesFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.CoursePhotosFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.CourseVideoesFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.Notes.CourseNotesFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.CourseContentsFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.CourseListFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.CourseParticipantsFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Forum.ForumDiscussionListFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.MoodleUser.ViewProfileFragment;
import edu.uwi.mona.mobileourvle.app.R;

import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseListerner;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseManager;
import org.sourceforge.ah.android.utilities.Widgets.Listeners.SimpleViewPagerTabListener;

import java.io.File;

/**
 * @author Aston Hamilton
 */
public class CourseListActivity extends ActivityBase implements CourseListOptionsDialogFragment.CourseOptionListener,
        CourseContentsFragment.Listener, CourseParticipantsFragment.Listener, CourseMediaOptionsDialogFragment.MediaOptionListener {

    private UserSession mUserSession;

    private ViewPager mPager;
    private DrawerLayout mDrawer;
    private MainPagePagerAdaper mPagerAdapter;
    private ListView navigationList;
    private LinearLayout layout;
    private boolean isLargeScreen = false;
    private MoodleCourse mCourse;

    private CoursePhotosFragment photoFragment;

    private FragmentResponseListerner mOnCourseSeclectedReceiver;
    private FragmentResponseListerner mOnDiscussionSeclectedReceiver;
    private CourseVideoesFragment videoFragment;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        final Bundle extras = getIntent().getExtras();

        mUserSession = ((UserSessionParcel) extras
                .get(ParcelKeys.USER_SESSION)).getWrappedObejct();

        setTitle(mUserSession.getContext().getSiteInfo().getName());

        mPager = (ViewPager) findViewById(R.id.pager);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);

        if(mPager!=null) {
            setupViewPager();
            addTabNavigation();
        }
        else if(mDrawer!=null){
            isLargeScreen = true;
            setupNavigationDrawer();
        }
    }


    @Override
    protected void onResume() {
        if (mOnCourseSeclectedReceiver == null)
            mOnCourseSeclectedReceiver = new OnCourseSelectedReceiver();

        FragmentResponseManager.registerReceiver(getApplicationContext(),
                                                 CourseListFragment.Responses.onCourseSelected,
                                                 mOnCourseSeclectedReceiver);

        if (mOnDiscussionSeclectedReceiver == null)
            mOnDiscussionSeclectedReceiver = new OnDiscussionSelectedReceiver();

        FragmentResponseManager.registerReceiver(getApplicationContext(),
                                                 ForumDiscussionListFragment.Responses.onDiscussionSelected,
                                                 mOnDiscussionSeclectedReceiver);

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mOnCourseSeclectedReceiver != null)
            FragmentResponseManager.unregisterReceiver(getApplicationContext(),
                                                       mOnCourseSeclectedReceiver);

        if (mOnDiscussionSeclectedReceiver != null)
            FragmentResponseManager.unregisterReceiver(getApplicationContext(),
                                                       mOnDiscussionSeclectedReceiver);
        super.onPause();
    }

    private void setupNavigationDrawer()
    {
        navigationList = (ListView) findViewById(R.id.navgation_list);
        layout = (LinearLayout) findViewById(R.id.left_drawer);

        mDrawer.openDrawer(layout);

        navigationList.setAdapter(new ArrayAdapter<String>(CourseListActivity.this, android.R.layout.simple_list_item_1, new String[]{"News", "Profile"}));
        navigationList.setOnItemClickListener(new NavigationListener());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment f;

        if (mUserSession.getContext().getSiteInfo().getNewsForum() == null) {
            f = ViewProfileFragment.newInstance(
                    mUserSession,
                    mUserSession.getContext().getCurretnUser(),
                    null);
        } else {
            f = CourseListFragment.newInstance(mUserSession);
        }

        fragmentTransaction.add(R.id.course_list_container, f);

        if (mUserSession.getContext().getSiteInfo().getNewsForum() == null) {
            f = CourseListFragment.newInstance(mUserSession);
        } else {
            f = ForumDiscussionListFragment
                    .newInstance(mUserSession,
                            mUserSession.getContext().getSiteInfo()
                                    .getNewsForum()
                    );
        }

        fragmentTransaction.replace(R.id.content_container, f);
        fragmentTransaction.commit();
    }

    private void setupViewPager() {
        mPagerAdapter = new MainPagePagerAdaper(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(final int arg0) {

            }

            @Override
            public void onPageScrolled(final int arg0, final float arg1,
                                       final int arg2) {

            }

            @Override
            public void onPageSelected(final int position) {
                getActionBar().getTabAt(position).select();
            }

        });
    }

    private void addTabNavigation() {
        // setup action bar for tabs
        final ActionBar actionBar = getActionBar();
        // actionBar.setDisplayShowTitleEnabled(false);
        Tab tab;

        tab = actionBar.newTab()
                       .setText("News")
                       .setIcon(R.drawable.chat_icon)
                       .setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                       .setText("Courses")
                       .setIcon(R.drawable.content_icon)
                       .setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab, true);

        tab = actionBar.newTab()
                       .setText("Profile")
                       .setIcon(R.drawable.profile_icon)
                       .setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    @Override
    public void OnPhotoItemSelected(int loc, Long id, Uri uri) {
        switch (loc)
        {
            case MediaOptions.DELETE:
                CoursePhotosOpenHelper helper = new CoursePhotosOpenHelper(this);
                helper.deletePhoto(id);
                photoFragment.refresh();
                break;
            case MediaOptions.VIEW:
                // Launch default viewer for the file
                final Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        uri, "image/*");

                try{
                    startActivity(intent);
                }
                catch(NullPointerException e)
                {

                }
                break;
            default:
                Toast.makeText(this,"Error opening dialog",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onVideoItemSelected(int loc, Long id, Uri uri) {
        switch (loc)
        {
            case MediaOptions.DELETE:
                CourseVideosOpenHelper helper = new CourseVideosOpenHelper(this);
                helper.deleteVideo(id);
                videoFragment.refresh();
                break;
            case MediaOptions.VIEW:
                // Launch default viewer for the file
                final Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        uri, "video/*");

                startActivity(intent);
                break;
            default:
                Toast.makeText(this,"Error opening dialog",Toast.LENGTH_LONG).show();
        }
    }

    private class OnCourseSelectedReceiver extends
            FragmentResponseListerner {

        @Override
        public void onResponseReceived(final Context context, final Bundle data) {
                final MoodleCourse course = ((MoodleCourseParcel) data
                        .getParcelable(
                                CourseListFragment.ResponseArgs.Course))
                        .getWrappedObejct();
            if(!isLargeScreen) {
                final Intent intent = new Intent(CourseListActivity.this,
                        CourseContentsActivity.class);

                intent.putExtra(ParcelKeys.USER_SESSION,
                        new UserSessionParcel(mUserSession));

                intent.putExtra(ParcelKeys.MOODLE_COURSE,
                        new MoodleCourseParcel(course));

                startActivity(intent);
            }
            else
            {

                mCourse = course;

                CourseListOptionsDialogFragment dialog = new CourseListOptionsDialogFragment();

                FragmentManager fragmentManager = getSupportFragmentManager();

                dialog.show(fragmentManager, "dialog");

                mDrawer.closeDrawer(layout);
            }
        }
    }

    @Override
    public void OnOptionItemSelected(int loc)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment f = null;
        switch(loc)
        {
            case 0:
                f = CourseContentsFragment.newInstance(mUserSession, mCourse);
                break;
            case 1:
                f = CourseClassTimesFragment.newInstance(
                        mCourse);
                break;
            case 2:
                f = CourseParticipantsFragment.newInstance(mUserSession, mCourse);
                break;
            case 3:
                f = CoursePhotosFragment.newInstance(mCourse);
                photoFragment=(CoursePhotosFragment) f;
                break;
            case 4:
                f = CourseVideoesFragment.newInstance(mCourse);
                videoFragment = (CourseVideoesFragment) f;
                break;
            case 5:
                f = CourseNotesFragment.newInstance(mCourse);
                break;
        }

        fragmentTransaction.replace(R.id.content_container, f);
        fragmentTransaction.commit();
    }

    private class OnDiscussionSelectedReceiver extends
            FragmentResponseListerner {

        @Override
        public void onResponseReceived(final Context context, final Bundle data) {

            final ForumDiscussion discussion = ((ForumDiscussionParcel) data
                    .getParcelable(ForumDiscussionListFragment.ResponseArgs.Discussion))
                    .getWrappedObejct();

            final Intent intent = new Intent(CourseListActivity.this,
                                             ForumDiscussionPostListActivity.class);

            intent.putExtra(ParcelKeys.USER_SESSION,
                            new UserSessionParcel(mUserSession));

            intent.putExtra(ParcelKeys.FORUM_DISCUSSION,
                            new ForumDiscussionParcel(discussion));

            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit the app?")
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CourseListActivity.this.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    static File courseFile;
    @Override
    public void onCourseModuleSelected(final CourseModule module) {
        if ("resource".equalsIgnoreCase(module.getName())) {

            String folderLocation = "/OurVLE/courses/"+mCourse.getName().trim()+"-"+mCourse.getId()+"/files/"; // sub-folder definition

            File location = new File(Environment.getExternalStorageDirectory(), folderLocation);

            if (!location.exists()) {
                location.mkdirs();  // makes the subfolder
            }

            String fileLocation = location+"/"+module.getFileName();
            courseFile = new File(fileLocation);

            if(!courseFile.exists()) // checks if the file is not already present
            {
                Toast.makeText(getApplicationContext(), "Downloading File: " + module.getLabel(),
                        Toast.LENGTH_LONG).show();
                final String url = module.getFileUrl() + "&token=" + mUserSession.getSessionKey();
                final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setDescription("Course file download");
                request.setTitle(module.getLabel());
                // in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(folderLocation,
                        module.getFileName());

                // get download service and enqueue file
                final DownloadManager manager = (DownloadManager) getSystemService(
                        Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);

                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
            else // open the file that is already present
            {
                openFile(courseFile);
            }
        } else if (module.getFileUrl() != null) {

            final String url = module.getFileUrl();
            final Intent webviewIntent = new Intent(this,CourseContentsResourceActivity.class);
            webviewIntent.putExtra("URL",url);

            startActivity(webviewIntent);
        } else //noinspection StatementWithEmptyBody
            if ("label".equalsIgnoreCase(module.getName())) {
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
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(getApplicationContext(), "no application available to view this file",
                    Toast.LENGTH_LONG).show();
        }
    }

    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
                if(courseFile.exists())
                    openFile(courseFile);
                else
                    Toast.makeText(CourseListActivity.this,"Error downloading file",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onParticipantSelected(final MoodleUser user) {
        final Intent i = new Intent(CourseListActivity.this, ViewUserProfileActivity.class);
        i.putExtra(ParcelKeys.USER_SESSION, new UserSessionParcel(mUserSession));
        i.putExtra(ParcelKeys.MOODLE_USER, new MoodleUserParcel(user));

        startActivity(i);
    }


    /* ===================== Private Classes =============== */
    private class NavigationListener implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment f;

            switch(i)
            {
                case 0:
                    if (mUserSession.getContext().getSiteInfo().getNewsForum() == null) {
                        f = CourseListFragment.newInstance(mUserSession);
                    } else {
                        f = ForumDiscussionListFragment
                                .newInstance(mUserSession,
                                        mUserSession.getContext().getSiteInfo()
                                                .getNewsForum()
                                );
                    }
                    break;
                case 1:
                    f = ViewProfileFragment.newInstance(
                            mUserSession,
                            mUserSession.getContext().getCurretnUser(),
                            null);
                    break;
                default:
                    f=null;
                    break;
            }
            fragmentTransaction.replace(R.id.content_container, f);
            fragmentTransaction.commit();

            mDrawer.closeDrawer(layout);
        }
    }
    private class MainPagePagerAdaper extends FragmentStatePagerAdapter {

        public MainPagePagerAdaper(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            if (mUserSession.getContext().getSiteInfo().getNewsForum() == null) {
                return 2;
            }
            return 3;
        }

        @Override
        public Fragment getItem(final int position) {
            final Fragment f;
            switch (position) {
                case 0:
                    if (mUserSession.getContext().getSiteInfo().getNewsForum() == null) {
                        f = CourseListFragment.newInstance(mUserSession);
                    } else {
                        f = ForumDiscussionListFragment
                                .newInstance(mUserSession,
                                             mUserSession.getContext().getSiteInfo()
                                                         .getNewsForum()
                                            );
                    }
                    break;
                case 1:
                    if (mUserSession.getContext().getSiteInfo().getNewsForum() == null) {
                        f = ViewProfileFragment.newInstance(
                                mUserSession,
                                mUserSession.getContext().getCurretnUser(),
                                null);
                    } else {
                        f = CourseListFragment.newInstance(mUserSession);
                    }
                    break;
                case 2:
                    f = ViewProfileFragment.newInstance(
                            mUserSession,
                            mUserSession.getContext().getCurretnUser(),
                            null);
                    break;
                default:
                    return null;
            }

            return f;
        }
    }
    /*==================================== Interfaces =============================================*/
    private interface MediaOptions {
        static final int VIEW = 0;
        static final int DELETE =1;
    }
}
