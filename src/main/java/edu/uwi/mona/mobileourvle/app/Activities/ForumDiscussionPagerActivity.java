/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Activities;

import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseListerner;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.FragmentResponseManager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.DiscussionParentParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.ForumDiscussionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.Forum.ForumDiscussionPagerFragment;

/**
 * @author Aston Hamilton
 */
public class ForumDiscussionPagerActivity extends ActivityBase {
    private UserSession mUserSession;

    private final FragmentResponseListerner mOnDiscussionSelected = new FragmentResponseListerner() {

        @Override
        public void onResponseReceived(final Context context, final Bundle data) {
            final ForumDiscussion discussion = ((ForumDiscussionParcel) data
                    .getParcelable(ForumDiscussionPagerFragment.ResponseArgs.Discussion))
                    .getWrappedObejct();

            setTitle(discussion.getName());

        }
    };

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_discussion_pager);

        final Bundle extras = getIntent().getExtras();

        mUserSession = ((UserSessionParcel) extras
                .get(ParcelKeys.USER_SESSION)).getWrappedObejct();

        final long currentDiscussion = extras
                .getLong(ParcelKeys.FORUM_DISCUSSION_ID);

        final DiscussionParent parent = ((DiscussionParentParcel) extras
                .getParcelable(ParcelKeys.PARENT)).getWrappedObejct();

        final String activityTitle = "Discussion";

        setTitle(activityTitle);

        final ForumDiscussionPagerFragment fragment = ForumDiscussionPagerFragment
                .newInstance(
                        mUserSession,
                        currentDiscussion,
                        parent);

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

        FragmentResponseManager.registerReceiver(this,
                                                 ForumDiscussionPagerFragment.Responses.onDiscussionSelected,
                                                 mOnDiscussionSelected);

        super.onResume();
    }

    @Override
    protected void onPause() {

        FragmentResponseManager.unregisterReceiver(this,
                                                   mOnDiscussionSelected);

        super.onPause();
    }
}
