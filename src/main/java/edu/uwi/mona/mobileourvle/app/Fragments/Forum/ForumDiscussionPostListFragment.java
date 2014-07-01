/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Forum;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.uwi.mona.mobileourvle.app.Activities.SendPostReplyActivity;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.ForumDiscussionPostContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviders.ForumDiscussionPostProvider;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviders.ForumDiscussionProvider;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionContext;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.SyncEntities.ForumDiscussionPostSyncronizationManager;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.DiscussionPostParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.ForumDiscussionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.AuthenticatedListFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.ForumDiscussionPostListFragment.EntityWrappers.ExtendedDiscussionPostWrapper;
import edu.uwi.mona.mobileourvle.app.R;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.ContentProviders.EntityManagerContract;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.EntitySyncronizer;
import org.sourceforge.ah.android.utilities.Converters.PixelConverter;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Plugins.EntitySyncronizerPlugin;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.DynamicViewAdapter.SimpleViewHolder;

/**
 * @author Aston Hamilton
 */
public class ForumDiscussionPostListFragment extends AuthenticatedListFragment
        implements LoaderCallbacks<Cursor> {

    private DiscussionContext mDiscussionContext;
    private ForumDiscussion mDiscussion;

    private DiscussionPostListAdapter mListAdapter;

    public static ForumDiscussionPostListFragment newInstance(final UserSession session,
                                                              final ForumDiscussion discussion) {
        final ForumDiscussionPostListFragment f = new ForumDiscussionPostListFragment();

        f.setUserSession(session);
        f.setForumDiscussion(discussion);
        return f;
    }

    public void setForumDiscussion(final ForumDiscussion discussion) {
        getFragmentArguments().putParcelable(ParcelKeys.FORUM_DISCUSSION,
                                             new ForumDiscussionParcel(discussion));

        mDiscussion = discussion;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        mDiscussion = ((ForumDiscussionParcel) getFragmentArguments().getParcelable(
                ParcelKeys.FORUM_DISCUSSION)).getWrappedObejct();


        mDiscussionContext = new DiscussionContext(false);
        /*
        // TODO - Remove Stub (Change status to inprogress)
        EntitySyncronizer.updateEntityManagerSyncronizationState(
                getApplicationContext(),
                "edu.uwi.mona.mobileourvle.app.Classes.DataLayer.SyncEntities.ForumDiscussionPostSyncronizationManager",
                EntityManagerContract.SyncDirection.PULL,
                EntityManagerContract.Status.SYNCRONIZED,
                new DateTime());
*/
        final EntitySyncronizerPlugin plugin = new EntitySyncronizerPlugin(
                new ForumDiscussionPostSyncronizationManager(),
                ForumDiscussionProvider.getSerializedDiscussion(mDiscussion));

        registerPlugin(plugin);

        mListAdapter = new DiscussionPostListAdapter(getParentActivity(), null,
                                                     CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


        getLoaderManager().initLoader(Loaders.DiscussionPosts, null, this);

        setListAdapter(mListAdapter);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //MenuItem item = menu.add(R.string.reply_icon_title);

        //item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        //item.setIcon(getActivity().getResources().getDrawable(R.drawable.reply_icon));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String title= getActivity().getResources().getString(R.string.reply_icon_title);

        if(item.getTitle()==null)
            return super.onOptionsItemSelected(item);

        if (item.getTitle().toString().equals(title)) {
            Intent intent = new Intent(getActivity(), SendPostReplyActivity.class);

            intent.putExtra(ParcelKeys.USER_SESSION,new UserSessionParcel(getUserSession()));

            final ExtendedDiscussionPostWrapper extendedPost = ExtendedDiscssionPostCursorWrapper
                    .getExtendedPost(mListAdapter.getCursor());

            intent.putExtra(ParcelKeys.DISCUSSION_POST, new DiscussionPostParcel(extendedPost.getPost()));


            startActivity(intent);

        }
        return true;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discussion_post_list, container, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int arg0, final Bundle arg1) {
        if (arg0 == Loaders.DiscussionPosts) {
            String selection = ForumDiscussionPostContract.Columns.DISCUSSION_ID + " = ? ";
            String[] selectionArgs = {mDiscussion.getId().toString()};

            return new CursorLoader(getParentActivity(), ForumDiscussionPostContract.CONTENT_URI,
                                    new String[]{
                                            ForumDiscussionPostContract.Columns._ID,
                                            ForumDiscussionPostContract.Columns.POST_TITLE,
                                            ForumDiscussionPostContract.Columns.DISCUSSION_ID,
                                            ForumDiscussionPostContract.Columns.POST_TEXT,
                                            ForumDiscussionPostContract.Columns.CREATED,
                                            ForumDiscussionPostContract.Columns.INDENTATION,
                                            ForumDiscussionPostContract.Columns.POSTER,
                                            ForumDiscussionPostContract.Columns.MODIFIED,
                                            ForumDiscussionPostContract.Columns.PARENT
                                    }, selection, selectionArgs, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> arg0, final Cursor arg1) {
        if (arg0.getId() == Loaders.DiscussionPosts) {
            mListAdapter.swapCursor(arg1);
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        if (loader.getId() == Loaders.DiscussionPosts) {
            mListAdapter.swapCursor(null);
        }

    }

    /* ============================= Helper Methods ======================= */
    public static class ExtendedDiscssionPostCursorWrapper {
        public static ExtendedDiscussionPostWrapper getExtendedPost(final Cursor c) {
            long postId = c.getLong(0);
            String title = c.getString(1);
            String discussionId = c.getString(2);
            String postText = c.getString(3);
            DateTime created = DateFormatter.getDateTimeFromISOString(c.getString(4));
            int indentationFactor = c.getInt(5);
            MoodleUser poster = ForumDiscussionPostProvider.getDeserializePoster(c.getString(6));
            DateTime modified = DateFormatter.getDateTimeFromISOString(c.getString(7));
            long parent = c.getLong(8);

            ExtendedDiscussionPostWrapper p = new ExtendedDiscussionPostWrapper(
                    new DiscussionPost(
                            postId, title, postText, discussionId,
                            parent, created, modified, false, poster));

            p.setIndentationFactor(indentationFactor);

            return p;
        }
    }

    public class DiscussionPostListAdapter extends CursorAdapter {

        class ViewHolder implements SimpleViewHolder {
            TextView title;
            TextView message;
            TextView date;
            TextView author;
            TextView lastModified;

            ImageView replyBtn;
        }

        public DiscussionPostListAdapter(final Context context, final Cursor c, final int flags) {
            super(context, c, flags);
        }

        @Override
        public void bindView(final View view, final Context arg1, final Cursor cursor) {
            final ViewHolder viewHolder = (ViewHolder) view.getTag();

            final ExtendedDiscussionPostWrapper extendedPost = ExtendedDiscssionPostCursorWrapper
                    .getExtendedPost(cursor);

            view.setPadding(PixelConverter.dpToPixels(extendedPost.getIndentationFactor() * 10,
                                                      getParentActivity().getResources()),
                            view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());

            viewHolder.title.setText(extendedPost.getPost().getSubject());
            viewHolder.date.setText(DateFormatter.getShortDateTime(
                    extendedPost.getPost().getDateCreaded()));
            viewHolder.message.setText(extendedPost.getPost().getFormattedMessage());
            viewHolder.author.setText(String.format("by: %s", extendedPost.getPost().getPoster()
                                                                          .getFullName()));
            if (extendedPost.getPost().getDateCreaded().isEqual(
                    extendedPost.getPost().getDateLastModified()))
                viewHolder.lastModified.setVisibility(View.GONE);
            else {
                viewHolder.lastModified.setVisibility(View.VISIBLE);
                viewHolder.lastModified.setText(String.format("last modified %s",
                                                              DateFormatter.getShortDateTime(
                                                                      extendedPost.getPost()
                                                                                  .getDateLastModified())));
            }
            if (mDiscussionContext.isUserCanReply()) {
                viewHolder.replyBtn.setVisibility(View.VISIBLE);
                viewHolder.replyBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        Log.i(getClass().toString(), "Post Reply clicked");
                    }
                });
            } else
                viewHolder.replyBtn.setVisibility(View.INVISIBLE);

        }

        @Override
        public View newView(final Context arg0, final Cursor arg1, final ViewGroup arg2) {
            final View view = ((LayoutInflater) arg0.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.list_item_discussion_post_list,
                    arg2, false);
            assert view != null;

            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.title = (TextView) view.findViewById(R.id.textview_first_line);

            viewHolder.message = (TextView) view.findViewById(R.id.textview_second_line);

            viewHolder.date = (TextView) view.findViewById(R.id.textview_date);

            viewHolder.author = (TextView) view.findViewById(R.id.textview_author);

            viewHolder.lastModified = (TextView) view.findViewById(R.id.textview_last_modified);

            viewHolder.replyBtn = (ImageView) view.findViewById(R.id.imageview_reply);

            view.setTag(viewHolder);
            return view;
        }

    }

    /* ======================== Interfaces =========================== */
    public static interface Responses {
        String onPostReplyButtonClicked = "edu.uwi.mona.mobileourvle.app.Fragments.Forum.ForumDiscussionPostListFragment.onPostReplyButtonClicked";
    }

    public static interface ResponseArgs {
        String Discussion = "discussion";
    }

    public static interface Loaders {
        static final int DiscussionPosts = 0;
    }
}
