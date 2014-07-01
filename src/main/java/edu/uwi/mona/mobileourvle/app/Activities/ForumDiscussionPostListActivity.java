/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseListerner;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseManager;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.DiscussionParentParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.DiscussionPostParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.ForumDiscussionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Fragments.Forum.ForumDiscussionPagerFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Forum.ForumDiscussionPostListFragment;
import edu.uwi.mona.mobileourvle.app.R;

/**
 * @author Aston Hamilton
 */
public class ForumDiscussionPostListActivity extends ActivityBase {
    private UserSession mUserSession;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_discussion_pager);

        final Bundle extras = getIntent().getExtras();

        mUserSession = ((UserSessionParcel) extras.get(ParcelKeys.USER_SESSION)).getWrappedObejct();

        final ForumDiscussion discussion = ((ForumDiscussionParcel) extras
                .getParcelable(ParcelKeys.FORUM_DISCUSSION)).getWrappedObejct();

        final String activityTitle = discussion.getName();

        setTitle(activityTitle);

        final ForumDiscussionPostListFragment fragment = ForumDiscussionPostListFragment
                .newInstance(mUserSession, discussion);

        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        // Replace whatever is in the fragment_container view with this
        // fragment,
        transaction.replace(R.id.fragment, fragment);

        // Commit the transaction
        transaction.commit();

    }

}
