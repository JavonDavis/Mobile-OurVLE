/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Activities;

import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseListerner;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseManager;
import org.sourceforge.ah.android.utilities.Widgets.Listeners.SimpleViewPagerTabListener;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
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

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
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
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.CourseModuleParcel;
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
        implements CourseContentsFragment.Listener, CourseParticipantsFragment.Listener {
    private UserSession mUserSession;
    private MoodleCourse mCourse;

    private ViewPager mPager;
    private CourseContentsPagerAdapter mPagerAdapter;

    private CourseClassTimesFragment mCourseClassTimesProxiedFragment;
    private FragmentResponseListerner mOnDiscussionSeclectedReceiver;

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

        /*
        tab = actionBar.newTab().setText("Pictures").setIcon(R.drawable.picture_icon)
                       .setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Video").setIcon(R.drawable.video_icon).setTabListener(
                new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);
*/
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    }

    @Override
    public void onCourseModuleSelected(final CourseModule module) {
        if ("forum".equals(module.getName())) {
            final Intent intent = new Intent(CourseContentsActivity.this,
                                             ForumDiscussionListActivity.class);

            intent.putExtra(ParcelKeys.USER_SESSION, new UserSessionParcel(mUserSession));

            intent.putExtra(ParcelKeys.COURSE_FORUM_MODULE, new CourseModuleParcel(module));

            startActivity(intent);

        } else if ("resource".equalsIgnoreCase(module.getName())) {
            String url = module.getFileUrl();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDescription("Course file download");
            request.setTitle(module.getLabel());
// in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                                      module.getFileName());

// get download service and enqueue file
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        } else if ("page".equals(module.getName())) {
            String url = module.getFileUrl();
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;

            final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);

        } else
            Toast.makeText(this, "Mobile access to this resource is not yet supported",
                           Toast.LENGTH_SHORT)
                 .show();

    }

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
            return 5;

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
                case 6:
                    f = CoursePhotosFragment.newInstance(mCourse);
                    break;
                case 7:
                    f = CourseVideoesFragment.newInstance(mCourse);
                    break;
                default:
                    f = UnderDevelopementFragment.newInstance();
            }

            return f;
        }

    }
}
