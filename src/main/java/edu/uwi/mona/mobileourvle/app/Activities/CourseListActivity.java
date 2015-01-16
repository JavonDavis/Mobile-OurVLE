/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.ForumDiscussionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Fragments.Course.CourseListFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Forum.ForumDiscussionListFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.MoodleUser.ViewProfileFragment;
import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.view.SlidingTabLayout;

import org.sourceforge.ah.android.utilities.AndroidUtil.ManagedAsyncTask;
import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseListerner;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseManager;

import java.io.IOException;
import java.net.URL;

/**
 * @author Aston Hamilton
 */
public class CourseListActivity extends ActivityBase {

    private UserSession mUserSession;

    private ViewPager mPager;
    //private DrawerLayout mDrawer;
    private ListView navigationList;
    private LinearLayout layout;
    private boolean isLargeScreen = false;
    private SlidingTabLayout mSlidingTabs;

    private FragmentResponseListerner mOnCourseSeclectedReceiver;
    private FragmentResponseListerner mOnDiscussionSeclectedReceiver;
    private Menu menu;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        //setSupportActionBar(toolbar);



        final Bundle extras = getIntent().getExtras();

        mUserSession = ((UserSessionParcel) extras
                .get(ParcelKeys.USER_SESSION)).getWrappedObejct();

        toolbar.setTitle(getResources().getString(R.string.course));

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextAppearance(this,Typeface.BOLD);



        toolbar.inflateMenu(R.menu.menu_course_list);

        final MenuItem courseItem = toolbar.getMenu().getItem(ToolbarOptions.COURSE_ITEM);
        final MenuItem profileItem = toolbar.getMenu().getItem(ToolbarOptions.PROFILE_ITEM);
        final MenuItem messageItem = toolbar.getMenu().getItem(ToolbarOptions.MESSAGE_ITEM);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.courses_option:
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.container, CourseListFragment.newInstance(mUserSession)
                                        , "course").addToBackStack("course").commit();

                        item.setVisible(false);
                        toolbar.setTitle(getResources().getString(R.string.course_menu_option));
                        profileItem.setVisible(true);
                        messageItem.setVisible(true);
                        return true;
                    case R.id.messages:
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.container, ForumDiscussionListFragment
                                        .newInstance(mUserSession,
                                                mUserSession.getContext().getSiteInfo()
                                                        .getNewsForum()
                                        ), "message").addToBackStack("message").commit();
                        item.setVisible(false);
                        toolbar.setTitle(getResources().getString(R.string.message_menu_option));
                        courseItem.setVisible(true);
                        profileItem.setVisible(true);
                        return true;
                    case R.id.view_profile:
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.container, ViewProfileFragment.newInstance(
                                        mUserSession,
                                        mUserSession.getContext().getCurrentUser(),
                                        null), "profile").addToBackStack("profile").commit();
                        item.setVisible(false);
                        toolbar.setTitle(getResources().getString(R.string.profile_menu_option));
                        courseItem.setVisible(true);
                        messageItem.setVisible(true);
                        return true;
                    case R.id.log_out:
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

                }
                return false;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, CourseListFragment.newInstance(mUserSession))
                    .commit();
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

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        final MenuItem courseItem = menu.findItem(R.id.courses_option);
        final MenuItem profileItem = menu.findItem(R.id.view_profile);
        final MenuItem messageItem = menu.findItem(R.id.messages);

        switch (id) {
            case R.id.courses_option:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, CourseListFragment.newInstance(mUserSession)
                                , "course").addToBackStack("course").commit();

                item.setVisible(false);
                setTitle(getResources().getString(R.string.course_menu_option));
                profileItem.setVisible(true);
                messageItem.setVisible(true);
                return true;
            case R.id.messages:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, ForumDiscussionListFragment
                                .newInstance(mUserSession,
                                        mUserSession.getContext().getSiteInfo()
                                                .getNewsForum()
                                ), "message").addToBackStack("message").commit();
                item.setVisible(false);
                setTitle(getResources().getString(R.string.message_menu_option));
                courseItem.setVisible(true);
                profileItem.setVisible(true);
                return true;
            case R.id.view_profile:
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, ViewProfileFragment.newInstance(
                                mUserSession,
                                mUserSession.getContext().getCurrentUser(),
                                null), "profile").addToBackStack("profile").commit();
                item.setVisible(false);
                setTitle(getResources().getString(R.string.profile_menu_option));
                courseItem.setVisible(true);
                messageItem.setVisible(true);
                return true;
            case R.id.log_out:
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

        }

        return true;
    }*/

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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_course_list, menu);
        this.menu = menu;

        final MenuItem courseItem = menu.findItem(R.id.courses_option);
        final MenuItem profileItem = menu.findItem(R.id.view_profile);
        final MenuItem messageItem = menu.findItem(R.id.messages);

        if (!(getSupportFragmentManager().getBackStackEntryCount() == 0)) {
            String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName();

            switch (tag) {
                case "course":
                    courseItem.setVisible(false);
                    profileItem.setVisible(true);
                    messageItem.setVisible(true);
                    break;
                case "profile":
                    courseItem.setVisible(true);
                    profileItem.setVisible(false);
                    messageItem.setVisible(true);
                    break;
                case "message":
                    courseItem.setVisible(true);
                    profileItem.setVisible(true);
                    messageItem.setVisible(false);
                    break;
            }
        }
        else{
            setTitle(getResources().getString(R.string.course_menu_option));
            courseItem.setVisible(false);
            profileItem.setVisible(true);
            messageItem.setVisible(true);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                if (!(getSupportFragmentManager().getBackStackEntryCount() == 0)) {
                    String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName();

                    switch (tag) {
                        case "course":
                            setTitle(getResources().getString(R.string.course_menu_option));
                            courseItem.setVisible(false);
                            profileItem.setVisible(true);
                            messageItem.setVisible(true);
                            break;
                        case "profile":
                            setTitle(getResources().getString(R.string.profile_menu_option));
                            courseItem.setVisible(true);
                            profileItem.setVisible(false);
                            messageItem.setVisible(true);
                            break;
                        case "message":
                            setTitle(getResources().getString(R.string.message_menu_option));
                            courseItem.setVisible(true);
                            profileItem.setVisible(true);
                            messageItem.setVisible(false);
                            break;
                    }
                }
                else{
                    setTitle(getResources().getString(R.string.course_menu_option));
                    courseItem.setVisible(false);
                    profileItem.setVisible(true);
                    messageItem.setVisible(true);
                }

            }
        });
        return true;
    }
*/
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

    /*==================================== Interfaces =============================================*/
    private interface MediaOptions {
        static final int VIEW = 0;
        static final int DELETE =1;
    }

    private interface ToolbarOptions
    {
        final static int COURSE_ITEM = 0;
        final static int PROFILE_ITEM = 1;
        final static int MESSAGE_ITEM = 2;
    }
}
