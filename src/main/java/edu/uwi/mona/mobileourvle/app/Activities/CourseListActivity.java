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
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class CourseListActivity extends ActivityBase {

    private UserSession mUserSession;

    private ViewPager mPager;
    private DrawerLayout mDrawer;
    private MainPagePagerAdaper mPagerAdapter;
    private ListView navigationList;
    private LinearLayout layout;
    private boolean isLargeScreen = false;

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

    public boolean isDrawerOpen()
    {
        return mDrawer.isDrawerOpen(layout);
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

        mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {

            }

            @Override
            public void onDrawerOpened(View view) {
                //invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View view) {
                //invalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });


            f = CourseListFragment.newInstance(mUserSession,isLargeScreen);


        fragmentTransaction.add(R.id.course_list_container, f);

        fragmentTransaction.commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);

    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(isLargeScreen) {
            MenuItem logOut = menu.add("Log Out");
//        logOut.setIcon(android.R.drawable.ic_lock_power_off);
            logOut.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            logOut.setOnMenuItemClickListener(new LogOutListener());
        }
        return super.onCreateOptionsMenu(menu);
    }*/

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

                intent.putExtra(ParcelKeys.SCREEN_IDENTIFIER,isLargeScreen);

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


    /* ===================== Private Classes =============== */
    /* =========================== Listener ========================= */
    private class LogOutListener implements MenuItem.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            new AlertDialog.Builder(CourseListActivity.this)
                    .setTitle(R.string.log_out)
                    .setMessage(R.string.log_out_prompt)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            final SharedPreferences preferences =
                                    getApplicationContext().getSharedPreferences(
                                            LoginMainActivity.SAVED_LOGIN_PREFERENCES_NAME,
                                            Context.MODE_PRIVATE);

                            preferences.edit()
                                    .putString(LoginMainActivity.ENCRYPTION_KEY, "")
                                    .putString(LoginMainActivity.USERNAME_KEY, "")
                                    .putString(LoginMainActivity.PASSWORD_KEY, "")
                                    .commit();

                            final Intent intent = new Intent(CourseListActivity.this, LoginMainActivity.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                            CourseListActivity.this.finish();
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
    }

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
                        f = CourseListFragment.newInstance(mUserSession,isLargeScreen);
                    } else {
                        f = ForumDiscussionListFragment
                                .newInstance(mUserSession,
                                        mUserSession.getContext().getSiteInfo()
                                                .getNewsForum(),isLargeScreen
                                );
                    }
                    break;
                case 1:
                    f = ViewProfileFragment.newInstance(
                            mUserSession,
                            mUserSession.getContext().getCurretnUser(),
                            null,isLargeScreen);
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
                        f = CourseListFragment.newInstance(mUserSession,isLargeScreen);
                    } else {
                        f = ForumDiscussionListFragment
                                .newInstance(mUserSession,
                                             mUserSession.getContext().getSiteInfo()
                                                         .getNewsForum(),isLargeScreen
                                            );
                    }
                    break;
                case 1:
                    if (mUserSession.getContext().getSiteInfo().getNewsForum() == null) {
                        f = ViewProfileFragment.newInstance(
                                mUserSession,
                                mUserSession.getContext().getCurretnUser(),
                                null,isLargeScreen);
                    } else {
                        f = CourseListFragment.newInstance(mUserSession,isLargeScreen);
                    }
                    break;
                case 2:
                    f = ViewProfileFragment.newInstance(
                            mUserSession,
                            mUserSession.getContext().getCurretnUser(),
                            null,isLargeScreen);
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
