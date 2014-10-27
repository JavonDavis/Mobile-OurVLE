/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Activities;

import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseListerner;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.DiscussionParentParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.ForumDiscussionParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.Forum.ForumDiscussionPostListFragment;
import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ExtendedForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.CourseForumParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.CourseModuleParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.Forum.ForumDiscussionListFragment;

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
                    .newInstance(mUserSession, forum,false);
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

                    final ForumDiscussion discussion = ((ForumDiscussionParcel) data
                            .getParcelable(ForumDiscussionListFragment.ResponseArgs.Discussion))
                            .getWrappedObejct();

                    final Intent intent = new Intent(ForumDiscussionListActivity.this,
                                                     ForumDiscussionPostListActivity.class);

                    intent.putExtra(ParcelKeys.USER_SESSION,
                                    new UserSessionParcel(mUserSession));

                    intent.putExtra(ParcelKeys.FORUM_DISCUSSION,
                                    new ForumDiscussionParcel(discussion));

                    startActivity(intent);
                    /*
                    final Intent intent = new Intent(ForumDiscussionListActivity.this,
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
                                    new DiscussionParentParcel(discussion.getParent()));

                    intent.putExtra(ParcelKeys.FORUM_DISCUSSION_ID,
                                    discussion.getId());

                    startActivity(intent);
                    */
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
}
