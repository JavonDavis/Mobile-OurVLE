/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Activities;

import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;

import android.os.Bundle;
import android.widget.Toast;

import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.DiscussionPostParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.Forum.SendPostReplyFragment;

/**
 * @author Aston Hamilton
 * 
 */
public class SendPostReplyActivity extends ActivityBase implements
	SendPostReplyFragment.Listener {

    private UserSession mUserSession;
    private DiscussionPost mDiscussionPost;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_send_post_reply);

	final Bundle extras = getIntent().getExtras();

	mUserSession = ((UserSessionParcel) extras
		.get(ParcelKeys.USER_SESSION)).getWrappedObejct();

	mDiscussionPost = ((DiscussionPostParcel) extras
		.get(ParcelKeys.DISCUSSION_POST)).getWrappedObejct();
	// set the action bar title

	setTitle((mDiscussionPost.getSubject()
		.substring(0, 3).toLowerCase().equals("re:")
		? ""
		: "Re: ")
		+ mDiscussionPost.getSubject());

	final SendPostReplyFragment fragment = SendPostReplyFragment
		.newInstance(mUserSession, mDiscussionPost);

	final android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
		.beginTransaction();

	// Replace whatever is in the fragment_container view with this
	// fragment,
	transaction.replace(R.id.fragment, fragment);

	// Commit the transaction
	transaction.commit();

    }

    @Override
    public void onReplySent(final ResponseObject response) {
	Toast.makeText(this, "Reply sent", Toast.LENGTH_LONG).show();
	finish();
    }
}
