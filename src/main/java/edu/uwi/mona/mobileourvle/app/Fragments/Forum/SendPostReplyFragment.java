/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Forum;

import org.sourceforge.ah.android.utilities.Communication.CommuncationModule;
import org.sourceforge.ah.android.utilities.Communication.Interfaces.OnCommunicationResponseListener;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPostReply;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.DiscussionPostParcel;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.PostDiscussionPostReply;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.AuthenticatedFragment;

/**
 * @author Aston Hamilton
 * 
 */
public class SendPostReplyFragment extends AuthenticatedFragment implements
	OnCommunicationResponseListener {
    private DiscussionPost mDiscussionPost;
    private Listener mListener;

    private TextView mPostTitle;
    private TextView mPostDate;
    private TextView mPostAuthor;
    private TextView mPostText;

    private EditText mReplyMessage;

    private DefaultCommunicationModulePlugin mCommunicationPlugin;
    private Activity mActivity;

    public static SendPostReplyFragment newInstance(final UserSession session,
	    final DiscussionPost post) {
	final SendPostReplyFragment f = new SendPostReplyFragment();

	f.setUserSession(session);
	f.setDiscussionPost(post);

	return f;
    }

    public void setDiscussionPost(final DiscussionPost post) {
	getFragmentArguments().putParcelable(ParcelKeys.DISCUSSION_POST,
		new DiscussionPostParcel(post));

	mDiscussionPost = post;
    }

    @Override
    public void onAttach(final Activity activity) {
	super.onAttach(activity);
	mActivity = activity;
	try {
	    mListener = (Listener) activity;
	} catch (final ClassCastException e) {
	    throw new ClassCastException(
		    activity.toString()
			    + " must implement ForumDiscussionPostListFragment.Listener");
	}
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDiscussionPost = ((DiscussionPostParcel) getFragmentArguments()
            .getParcelable(ParcelKeys.DISCUSSION_POST))
            .getWrappedObejct();

        mCommunicationPlugin = new DefaultCommunicationModulePlugin();
        registerPlugin(mCommunicationPlugin);


    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        MenuItem item = menu.add(R.string.send_icon_title);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(getActivity().getResources().getDrawable(android.R.drawable.ic_menu_send));

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
	    final ViewGroup container,
	    final Bundle savedInstanceState) {
	final View v = inflater.inflate(R.layout.fragment_reply_to_post,
		container, false);

	mPostAuthor = (TextView) v.findViewById(R.id.textview_author);
	mPostDate = (TextView) v.findViewById(R.id.textview_date);
	mPostTitle = (TextView) v.findViewById(R.id.textview_first_line);
	mPostText = (TextView) v.findViewById(R.id.textview_second_line);

	mReplyMessage = (EditText) v.findViewById(R.id.edittext_message);

	mPostAuthor.setText(String.format(getString(R.string.ftext_by_author),
		mDiscussionPost.getPoster().getFullName()));

	mPostDate.setText(DateFormatter
		.getShortDateTime(mDiscussionPost.getDateCreaded()));

	mPostTitle.setText(mDiscussionPost.getSubject());

	mPostText.setText(mDiscussionPost.getFormattedMessage());

	return v;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        String title= getActivity().getResources().getString(R.string.send_icon_title);
        Log.e(item.getTitle().toString(), title);
        if (item.getTitle().toString().equals(title))
            sendReply();

	    //return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void onCommunicationError(final ResponseError response) {
	mCommunicationPlugin.defaultResponseError(response);
    }

    @Override
    public void onCommunicationResponse(final int requestId,
	    final ResponseObject response) {

	if (requestId == Requests.SEND_REPLY) {
	    mCommunicationPlugin.turnOffLoadingIcon();
	    mListener.onReplySent(response);
	}

    }

    @Override
    public void onDestroy() {
	CommuncationModule.cancelAllRunningAsyncRequests(this);
	super.onDestroy();
    }

    private void sendReply() {
	final String message = mReplyMessage.getText().toString().trim();
	if (message.length() == 0) {
	    Toast.makeText(mActivity, "Enter a reply message",
		    Toast.LENGTH_LONG)
		    .show();
	    return;
	}

	final String subject =
		(mDiscussionPost.getSubject()
			.substring(0, 3).toLowerCase().equals("re:")
			? ""
			: "Re: ")
			+ mDiscussionPost.getSubject();
	final DiscussionPostReply reply =
		new DiscussionPostReply(
			getUserSession()
				.getContext().getCurretnUser(),
			mDiscussionPost, subject, message);

	mCommunicationPlugin.turnOnLoadingIcon();
	CommuncationModule.sendAsyncRequest(
		mActivity,
		new PostDiscussionPostReply(getUserSession(), reply),
		Requests.SEND_REPLY,
		this);
    }

    /* ======================== Interfaces ========================= */
    public static interface Listener {
	void onReplySent(ResponseObject response);
    }

    private static interface Requests {
	static final int SEND_REPLY = 0;
    }
}
