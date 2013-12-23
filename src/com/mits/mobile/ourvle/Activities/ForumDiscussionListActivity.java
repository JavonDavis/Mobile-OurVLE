/**
 *
 */
package com.mits.mobile.ourvle.Activities;

import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseListerner;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.ForumDiscussionParcel;
import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.CourseModule;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ExtendedForumDiscussion;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.CourseForumParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.CourseModuleParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.UserSessionParcel;
import com.mits.mobile.ourvle.Fragments.Forum.ForumDiscussionListFragment;

/**
 * @author Aston Hamilton
 */
public class ForumDiscussionListActivity extends ActivityBase {
    private UserSession mUserSession;
    private FragmentResponseListerner mOnDiscussionSeclectedReceiver;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_discussion_pager);

        final Bundle extras = getIntent().getExtras();

        mUserSession = ((UserSessionParcel) extras
                .get(ParcelKeys.USER_SESSION)).getWrappedObejct();

        final CourseForum forum;

        if (extras.containsKey(ParcelKeys.PARENT))
            forum = ((CourseForumParcel) extras
                    .get(ParcelKeys.PARENT)).getWrappedObejct();
        else
            forum = null;

        final CourseModule module;

        if (extras.containsKey(ParcelKeys.COURSE_FORUM_MODULE))
            module = ((CourseModuleParcel) extras
                    .get(ParcelKeys.COURSE_FORUM_MODULE)).getWrappedObejct();
        else
            module = null;

        final Fragment fragment;

        if (forum != null) {
            fragment = ForumDiscussionListFragment
                    .newInstance(mUserSession, forum);
            setTitle(forum.getName());
        } else if ("forum".equals(module.getName())) {
            fragment = ForumDiscussionListFragment
                    .newInstance(mUserSession, module);
            setTitle(module.getName());
        } else
            throw new RuntimeException(
                    "ForumDiscussionList Activity must get either a forum or a forum module.");

        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        // Replace whatever is in the fragment_container view with this
        // fragment,
        transaction.replace(R.id.fragment, fragment);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    protected void onResume() {
        if (mOnDiscussionSeclectedReceiver == null)
            mOnDiscussionSeclectedReceiver = new FragmentResponseListerner() {
                @Override
                public void onResponseReceived(final Context context, final Bundle data) {

                }
            };

        FragmentResponseManager.registerReceiver(getApplicationContext(),
                                                 ForumDiscussionListFragment.Responses.onDiscussionSelected,
                                                 mOnDiscussionSeclectedReceiver);

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mOnDiscussionSeclectedReceiver == null)
            FragmentResponseManager.unregisterReceiver(getApplicationContext(),
                                                       mOnDiscussionSeclectedReceiver);
        super.onPause();
    }

    public void onDiscussionSelected(final ExtendedForumDiscussion discussion) {
        /*
        final Intent intent = new Intent(ForumDiscussionListActivity.this,
                                         ForumDiscussionPagerActivity.class);

        intent.putExtra(ParcelKeys.USER_SESSION,
                        new UserSessionParcel(mUserSession));

        final ForumDiscussionListFragment f = (ForumDiscussionListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);

        final ForumDiscussion[] discussionList = f.getDiscussionList();
        final ForumDiscussionParcel[] discussionListParcel = new
                ForumDiscussionParcel[discussionList.length];
        for (int i = 0; i <
                        discussionList.length; i++)
            discussionListParcel[i] = new
                    ForumDiscussionParcel(discussionList[i]);
        intent.putExtra(ParcelKeys.FORUM_DISCUSSION_LIST,
                        discussionListParcel);

        intent.putExtra(ParcelKeys.FORUM_DISCUSSION_ID, discussion.getId());

        startActivity(intent);

        */

    }
}
