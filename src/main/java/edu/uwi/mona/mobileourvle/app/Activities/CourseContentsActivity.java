/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Activities;

import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseListerner;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseManager;
import org.sourceforge.ah.android.utilities.Widgets.Listeners.SimpleViewPagerTabListener;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;
import android.widget.Toast;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import java.io.File;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers.CoursePhotosOpenHelper;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers.CourseVideosOpenHelper;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.Dialogs.CourseMediaOptionsDialogFragment;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.CourseForumParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.DiscussionParentParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.ForumDiscussionParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.Forum.ForumDiscussionListFragment;
import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleUserParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.CourseContentsFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.CourseOverviewFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.CourseParticipantsFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.CoursePhotosFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.CourseVideoesFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.CourseClasses.CourseClassTimesFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion.Notes.CourseNotesFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Shared.UnderDevelopementFragment;

/**
 * @author Aston Hamilton
 */
public class CourseContentsActivity extends ActivityBase
        implements CourseContentsFragment.Listener, CourseParticipantsFragment.Listener, CourseMediaOptionsDialogFragment.MediaOptionListener {
    private UserSession mUserSession;
    private MoodleCourse mCourse;

    private ViewPager mPager;
    private CourseContentsPagerAdapter mPagerAdapter;

    private CourseClassTimesFragment mCourseClassTimesProxiedFragment;
    private FragmentResponseListerner mOnDiscussionSeclectedReceiver;
    private CoursePhotosFragment photoFragment;
    private CourseVideoesFragment videoFragment;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_main);

        final Bundle extras = getIntent().getExtras();

        mUserSession = ((UserSessionParcel) extras.get(ParcelKeys.USER_SESSION)).getWrappedObejct();

        mCourse = ((MoodleCourseParcel) extras.get(ParcelKeys.MOODLE_COURSE)).getWrappedObejct();

        setTitle(mCourse.getName());

        setupViewPager();
        addTabNavigation();

    }

    @Override
    protected void onResume() {
        if (mOnDiscussionSeclectedReceiver == null)
            mOnDiscussionSeclectedReceiver = new FragmentResponseListerner() {
                @Override
                public void onResponseReceived(final Context context, final Bundle data) {

                    final ForumDiscussion discussion = ((ForumDiscussionParcel) data
                            .getParcelable(ForumDiscussionListFragment.ResponseArgs.Discussion))
                            .getWrappedObejct();

                    final Intent intent = new Intent(CourseContentsActivity.this,
                            ForumDiscussionPagerActivity.class);

                    intent.putExtra(ParcelKeys.USER_SESSION,
                            new UserSessionParcel(mUserSession));

                    intent.putExtra(ParcelKeys.FORUM_DISCUSSION_ID,
                            discussion.getId());

                    intent.putExtra(ParcelKeys.PARENT,
                            new CourseForumParcel(mUserSession.getContext()
                                    .getSiteInfo()
                                    .getNewsForum())
                    );

                    intent.putExtra(ParcelKeys.PARENT,
                            new DiscussionParentParcel(
                                    new DiscussionParent(
                                            mUserSession.getContext()
                                                    .getSiteInfo()
                                                    .getNewsForum()
                                    )
                            )
                    );

                    intent.putExtra(ParcelKeys.FORUM_DISCUSSION_ID,
                            discussion.getId());

                    startActivity(intent);
                }
            };

        FragmentResponseManager.registerReceiver(getApplicationContext(),
                ForumDiscussionListFragment.Responses.onDiscussionSelected,
                mOnDiscussionSeclectedReceiver);

        super.onResume();
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

    @Override
    protected void onPause() {
        if (mOnDiscussionSeclectedReceiver != null)
            FragmentResponseManager.unregisterReceiver(getApplicationContext(),
                    mOnDiscussionSeclectedReceiver);
        super.onPause();
    }

    private void setupViewPager() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new CourseContentsPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(final int arg0) {

            }

            @Override
            public void onPageScrolled(final int arg0, final float arg1, final int arg2) {

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

        /*
        tab = actionBar.newTab().setText("Events").setIcon(R.drawable.event_icon).setTabListener(
                new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);
*/
        tab = actionBar.newTab().setText("Classes").setIcon(R.drawable.ic_time).setTabListener(
                new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Users").setIcon(R.drawable.ic_group).setTabListener(
                new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Resource").setIcon(R.drawable.collection_icon)
                .setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Overview").setIcon(R.drawable.overview_icon)
                .setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab, true);

        tab = actionBar.newTab().setText("Notes").setIcon(R.drawable.notes_icon).setTabListener(
                new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Pictures").setIcon(R.drawable.picture_icon)
                       .setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Video").setIcon(R.drawable.video_icon).setTabListener(
                new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
                    Toast.makeText(CourseContentsActivity.this,"Error downloading file",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onParticipantSelected(final MoodleUser user) {
        final Intent i = new Intent(CourseContentsActivity.this, ViewUserProfileActivity.class);
        i.putExtra(ParcelKeys.USER_SESSION, new UserSessionParcel(mUserSession));
        i.putExtra(ParcelKeys.MOODLE_USER, new MoodleUserParcel(user));

        startActivity(i);
    }

    /* ===================== Private Helper Dependent Classes =============== */
    private class CourseContentsPagerAdapter extends FragmentStatePagerAdapter {
        private Object mPrimaryItem;

        public CourseContentsPagerAdapter(final FragmentManager fm) {
            super(fm);
        }

        public Object getPrimaryItem() {
            return mPrimaryItem;
        }

        @Override
        public void setPrimaryItem(final ViewGroup container, final int position,
                                   final Object object) {
            mPrimaryItem = object;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getCount() {
            return 7;

        }

        @Override
        public Fragment getItem(final int position) {
            Fragment f;
            switch (position) {
                case 0:
                    mCourseClassTimesProxiedFragment = CourseClassTimesFragment.newInstance(
                            mCourse);
                    f = mCourseClassTimesProxiedFragment;
                    break;
                case 1:
                    f = CourseParticipantsFragment.newInstance(mUserSession, mCourse);
                    break;
                case 2:
                    f = CourseContentsFragment.newInstance(mUserSession, mCourse);
                    break;
                case 3:
                    f = CourseOverviewFragment.newInstance(mUserSession, mCourse);
                    break;
                case 4:
                    f = CourseNotesFragment.newInstance(mCourse);
                    break;
                case 5:
                    f = CoursePhotosFragment.newInstance(mCourse);
                    photoFragment= (CoursePhotosFragment) f;
                    break;
                case 6:
                    f = CourseVideoesFragment.newInstance(mCourse);
                    videoFragment = (CourseVideoesFragment) f;
                    break;
                default:
                    f = UnderDevelopementFragment.newInstance();
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
