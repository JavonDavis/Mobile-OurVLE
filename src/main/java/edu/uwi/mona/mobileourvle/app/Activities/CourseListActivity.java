/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.CourseForumParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.DiscussionParentParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.ForumDiscussionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.CourseListFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Forum.ForumDiscussionListFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.MoodleUser.ViewProfileFragment;
import edu.uwi.mona.mobileourvle.app.R;

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
        if (mOnCourseSeclectedReceiver != null)
            FragmentResponseManager.unregisterReceiver(getApplicationContext(),
                                                       mOnCourseSeclectedReceiver);

        if (mOnDiscussionSeclectedReceiver != null)
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
        Log.e("alre", "here");
        new AlertDialog.Builder(this)
                .setTitle("Logging out")
                .setMessage(
                        "Are you sure you want to log out of OurVLE")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CourseListActivity.this.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

}
