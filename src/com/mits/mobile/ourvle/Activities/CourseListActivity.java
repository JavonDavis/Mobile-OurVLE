/**
 *
 */
package com.mits.mobile.ourvle.Activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.CourseForumParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.DiscussionParentParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.ForumDiscussionParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleCourseParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.UserSessionParcel;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Fragments.Course.CourseListFragment;
import com.mits.mobile.ourvle.Fragments.Forum.ForumDiscussionListFragment;
import com.mits.mobile.ourvle.Fragments.MoodleUser.ViewProfileFragment;
import com.mits.mobile.ourvle.R;

import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseListerner;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseManager;
import org.sourceforge.ah.android.utilities.Widgets.Listeners.SimpleViewPagerTabListener;

/**
 * @author Aston Hamilton
 */
public class CourseListActivity extends ActivityBase {

    private UserSession mUserSession;

    private ViewPager mPager;
    private MainPagePagerAdaper mPagerAdapter;

    private FragmentResponseListerner mOnCourseSeclectedReceiver;
    private FragmentResponseListerner mOnDiscussionSeclectedReceiver;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        final Bundle extras = getIntent().getExtras();

        mUserSession = ((UserSessionParcel) extras
                .get(ParcelKeys.USER_SESSION)).getWrappedObejct();

        setTitle(mUserSession.getContext().getSiteInfo().getName());

        setupViewPager();
        addTabNavigation();

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
        FragmentResponseManager.unregisterReceiver(getApplicationContext(),
                                                   mOnCourseSeclectedReceiver);
        FragmentResponseManager.unregisterReceiver(getApplicationContext(),
                                                   mOnDiscussionSeclectedReceiver);
        super.onPause();
    }

    private void setupViewPager() {
        mPager = (ViewPager) findViewById(R.id.pager);
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

    private class OnCourseSelectedReceiver extends
            FragmentResponseListerner {

        @Override
        public void onResponseReceived(final Context context, final Bundle data) {
            final MoodleCourse course = ((MoodleCourseParcel) data
                    .getParcelable(
                            CourseListFragment.ResponseArgs.Course))
                    .getWrappedObejct();

            final Intent intent = new Intent(CourseListActivity.this,
                                             CourseContentsActivity.class);

            intent.putExtra(ParcelKeys.USER_SESSION,
                            new UserSessionParcel(mUserSession));

            intent.putExtra(ParcelKeys.MOODLE_COURSE,
                            new MoodleCourseParcel(course));

            startActivity(intent);
        }

    }

    private class OnDiscussionSelectedReceiver extends
            FragmentResponseListerner {

        @Override
        public void onResponseReceived(final Context context, final Bundle data) {

            final ForumDiscussion discussion = ((ForumDiscussionParcel) data
                    .getParcelable(ForumDiscussionListFragment.ResponseArgs.Discussion))
                    .getWrappedObejct();

            final Intent intent = new Intent(CourseListActivity.this,
                                             ForumDiscussionPagerActivity.class);

            intent.putExtra(ParcelKeys.USER_SESSION,
                            new UserSessionParcel(mUserSession));

            intent.putExtra(ParcelKeys.FORUM_DISCUSSION_ID,
                            discussion.getId());

            intent.putExtra(ParcelKeys.PARENT,
                            new CourseForumParcel(mUserSession.getContext()
                                                              .getSiteInfo()
                                                              .getNewsForum()));

            intent.putExtra(ParcelKeys.PARENT,
                            new DiscussionParentParcel(
                                    new DiscussionParent(
                                            mUserSession.getContext()
                                                        .getSiteInfo()
                                                        .getNewsForum())));

            intent.putExtra(ParcelKeys.FORUM_DISCUSSION_ID,
                            discussion.getId());

            startActivity(intent);
        }

    }

    /* ===================== Private Classes =============== */
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
                                                         .getNewsForum());
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

}
