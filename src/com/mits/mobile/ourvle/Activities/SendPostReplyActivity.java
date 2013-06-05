/**
 * 
 */
package com.mits.mobile.ourvle.Activities;

import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;

import android.os.Bundle;
import android.widget.Toast;

import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.DiscussionPostParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.UserSessionParcel;
import com.mits.mobile.ourvle.Fragments.Forum.SendPostReplyFragment;

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
