/**
 *
 */
package com.mits.mobile.ourvle.Activities;

import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;
import org.sourceforge.ah.android.utilities.Widgets.Listeners.SimpleViewPagerTabListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;
import android.widget.Toast;

import android.app.ActionBar;
import android.app.ActionBar.Tab;

import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.CourseModule;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.CourseModuleParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleCourseParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleUserParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.UserSessionParcel;
import com.mits.mobile.ourvle.Fragments.Course.CourseContentsFragment;
import com.mits.mobile.ourvle.Fragments.Course.CourseOverviewFragment;
import com.mits.mobile.ourvle.Fragments.Course.CourseParticipantsFragment;
import com.mits.mobile.ourvle.Fragments.Course.Companion.CoursePhotosFragment;
import com.mits.mobile.ourvle.Fragments.Course.Companion.CourseVideoesFragment;
import com.mits.mobile.ourvle.Fragments.Course.Companion.CourseClasses.CourseClassTimesFragment;
import com.mits.mobile.ourvle.Fragments.Course.Companion.Notes.CourseNotesFragment;
import com.mits.mobile.ourvle.Fragments.Shared.UnderDevelopementFragment;

/**
 * @author Aston Hamilton
 */
public class CourseContentsActivity extends ActivityBase implements CourseContentsFragment.Listener, CourseParticipantsFragment.Listener {
    private UserSession mUserSession;
    private MoodleCourse mCourse;

    private ViewPager mPager;
    private CourseContentsPagerAdapter mPagerAdapter;

    private CourseClassTimesFragment mCourseClassTimesProxiedFragment;

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

        tab = actionBar.newTab().setText("Events").setIcon(R.drawable.event_icon).setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Classes").setIcon(R.drawable.ic_time).setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Users").setIcon(R.drawable.ic_group).setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Resource").setIcon(R.drawable.collection_icon).setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Overview").setIcon(R.drawable.overview_icon).setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab, true);

        tab = actionBar.newTab().setText("Notes").setIcon(R.drawable.notes_icon).setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Pictures").setIcon(R.drawable.picture_icon).setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Video").setIcon(R.drawable.video_icon).setTabListener(new SimpleViewPagerTabListener(mPager));
        actionBar.addTab(tab);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    }

    @Override
    public void onCourseModuleSelected(final CourseModule module) {
        if ("forum".equals(module.getName())) {
            final Intent intent = new Intent(CourseContentsActivity.this, ForumDiscussionListActivity.class);

            intent.putExtra(ParcelKeys.USER_SESSION, new UserSessionParcel(mUserSession));

            intent.putExtra(ParcelKeys.COURSE_FORUM_MODULE, new CourseModuleParcel(module));

            startActivity(intent);

        } else
            Toast.makeText(this, "Only the viewing of forums is ready ATM", Toast.LENGTH_SHORT).show();

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
        public void setPrimaryItem(final ViewGroup container, final int position, final Object object) {
            mPrimaryItem = object;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getCount() {
            return 8;

        }

        @Override
        public Fragment getItem(final int position) {
            Fragment f;
            switch (position) {
                case 1:
                    mCourseClassTimesProxiedFragment = CourseClassTimesFragment.newInstance(mCourse);
                    f = mCourseClassTimesProxiedFragment;
                    break;
                case 2:
                    f = CourseParticipantsFragment.newInstance(mUserSession, mCourse);
                    break;
                case 3:
                    f = CourseContentsFragment.newInstance(mUserSession, mCourse);
                    break;
                case 4:
                    f = CourseOverviewFragment.newInstance(mUserSession, mCourse);
                    break;
                case 5:
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

    // @Override
    // public void onClassAdditionCancelled() {
    // mCourseClassTimesProxiedFragment.onClassAdditionCancelled();
    // }
    //
    // @Override
    // public void onClassAdded(final Uri uri) {
    // mCourseClassTimesProxiedFragment.onClassAdded(uri);
    // }
}
