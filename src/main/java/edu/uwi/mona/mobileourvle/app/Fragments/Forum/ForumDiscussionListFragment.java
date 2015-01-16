/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Forum;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.ContentProviders.EntityManagerContract;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.EntitySyncronizer;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Plugins.EntitySyncronizerPlugin;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.DynamicViewAdapter.SimpleViewHolder;

import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.ForumDiscussionContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviders.ForumDiscussionProvider;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ExtendedForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.SyncEntities.ForumDiscussionSyncronizationManager;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.DiscussionParentParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.ForumDiscussionParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.AuthenticatedListFragment;

/**
 * @author Aston Hamilton
 */
public class ForumDiscussionListFragment extends AuthenticatedListFragment
        implements LoaderCallbacks<Cursor> {

    private DiscussionParent mParent;

    private ExtendediscussionListAdapter mListAdapter;

    public static ForumDiscussionListFragment newInstance(final UserSession session,
                                                          final CourseForum forum) {
        final ForumDiscussionListFragment f = ForumDiscussionListFragment.newInstance(session,
                                                                                      forum, null);
        return f;
    }

    public static ForumDiscussionListFragment newInstance(final UserSession session,
                                                          final CourseModule module) {
        final ForumDiscussionListFragment f = ForumDiscussionListFragment.newInstance(session, null,
                                                                                      module);

        return f;
    }

    private static ForumDiscussionListFragment newInstance(final UserSession session,
                                                           final CourseForum forum,
                                                           final CourseModule module) {
        final ForumDiscussionListFragment f = new ForumDiscussionListFragment();

        f.setUserSession(session);

        if (forum != null)
            f.setCourseForum(forum);
        if (module != null)
            f.setCourseModule(module);

        return f;
    }

    public void setCourseForum(final CourseForum forum) {
        mParent = new DiscussionParent(forum);

        getFragmentArguments().putParcelable(ParcelKeys.PARENT, new DiscussionParentParcel(
                mParent));

    }

    public void setCourseModule(final CourseModule module) {
        mParent = new DiscussionParent(module);

        getFragmentArguments().putParcelable(ParcelKeys.PARENT, new DiscussionParentParcel(
                mParent));

    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {

        mParent = ((DiscussionParentParcel) getFragmentArguments().getParcelable(ParcelKeys.PARENT))
                .getWrappedObejct();

        mListAdapter = new ExtendediscussionListAdapter(getParentActivity(), null,
                                                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                                                        mParent);

/*
        // TODO - Remove Stub (Change status ti inprogress)
        EntitySyncronizer.updateEntityManagerSyncronizationState(
                getApplicationContext(),
                "edu.uwi.mona.mobileourvle.app.Classes.DataLayer.SyncEntities.ForumDiscussionSyncronizationManager",
                EntityManagerContract.SyncDirection.PULL,
                EntityManagerContract.Status.SYNCRONIZED,
                new DateTime());
*/
        final EntitySyncronizerPlugin plugin = new EntitySyncronizerPlugin(
                new ForumDiscussionSyncronizationManager(),
                ForumDiscussionProvider.getSerializedParent(mParent));

        registerPlugin(plugin);

        getLoaderManager().initLoader(Loaders.Discussions, null, this);

        setListAdapter(mListAdapter);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_discussion_list, container, false);

        return v;
    }

    @Override
    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final ForumDiscussion discussion = ForumDiscussionCursorWrapper.getDiscussion(
                (Cursor) mListAdapter.getItem(position), mParent);

        final Bundle b = new Bundle();
        b.putParcelable(ResponseArgs.Discussion, new ForumDiscussionParcel(discussion));
        sendResponse(Responses.onDiscussionSelected, b);

        super.onListItemClick(l, v, position, id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int loaderId, final Bundle arg1) {
        if (loaderId == Loaders.Discussions) {
            String selection;
            String[] selectionArgs;

            if (mParent.isForum()) {
                selection = ForumDiscussionContract.Columns.FORUM_ID + " = ?";
                selectionArgs = new String[]{mParent.getForum().getForumid().toString()};

            } else if (mParent.isModule()) {
                selection = ForumDiscussionContract.Columns.COURSE_MODULE_ID + " = ?";
                selectionArgs = new String[]{mParent.getModule().getId().toString()};
            } else
                throw new IllegalArgumentException("Discussion parent not forum or module.");

            return new CursorLoader(getParentActivity(), ForumDiscussionContract.CONTENT_URI,
                                    new String[]{
                                            ForumDiscussionContract.Columns._ID,
                                            ForumDiscussionContract.Columns.DISCUSSION_ID,
                                            ForumDiscussionContract.Columns.DISCUSSION_NAME,
                                            ForumDiscussionContract.Columns.CREATOR,
                                            ForumDiscussionContract.Columns.MODIFIED,
                                            ForumDiscussionContract.Columns.LAST_POST_ID,
                                            ForumDiscussionContract.Columns.LAST_POST_TEXT
                                    }, selection, selectionArgs, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> arg0, final Cursor arg1) {
        if (arg0.getId() == Loaders.Discussions)
            mListAdapter.swapCursor(arg1);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> arg0) {
        if (arg0.getId() == Loaders.Discussions)
            mListAdapter.swapCursor(null);
    }

    /* ========================== Static Wrappers ========================= */
    private static class ForumDiscussionCursorWrapper {
        public static Long getDiscussionId(final Cursor c) {
            return c.getLong(1);
        }

        public static String getDiscussionName(final Cursor c) {
            return c.getString(2);
        }

        public static MoodleUser getDiscussionCreator(final Cursor c) {
            return ForumDiscussionProvider.getDeserializeForumCreator(c.getString(3));
        }

        public static DateTime getDiscussionModified(final Cursor c) {
            return DateFormatter.getDateTimeFromISOString(c.getString(4));
        }

        public static Long getLastPostId(final Cursor c) {
            return c.getLong(5);
        }

        public static String getLastPostText(final Cursor c) {
            return c.getString(6);
        }

        public static ExtendedForumDiscussion getDiscussion(final Cursor c,
                                                            final DiscussionParent parent) {
            return new ExtendedForumDiscussion(ForumDiscussionCursorWrapper.getDiscussionId(c),
                                               ForumDiscussionCursorWrapper.getDiscussionName(c),
                                               ForumDiscussionCursorWrapper.getDiscussionCreator(c),
                                               ForumDiscussionCursorWrapper.getDiscussionModified(
                                                       c),
                                               ForumDiscussionCursorWrapper.getLastPostId(c),
                                               ForumDiscussionCursorWrapper.getLastPostText(c),
                                               parent);
        }
    }

    /* ========================== Private Classes =================== */
    private static class ExtendediscussionListAdapter extends CursorAdapter {

        private final DiscussionParent mParent;

        public ExtendediscussionListAdapter(final Context context, final Cursor c, final int flags,
                                            final DiscussionParent parent) {
            super(context, c, flags);

            mParent = parent;
        }

        class ViewHolder implements SimpleViewHolder {
            TextView poster;
            TextView discussionTitle;
            TextView lastReply;
            TextView discussionDate;
        }

        @Override
        public void bindView(final View arg0, final Context arg1, final Cursor arg2) {
            final ViewHolder viewHolder = (ViewHolder) arg0.getTag();

            final ExtendedForumDiscussion discussion = ForumDiscussionCursorWrapper.getDiscussion(
                    arg2, mParent);
            viewHolder.poster.setText(discussion.getCreator().getFullName());
            viewHolder.discussionTitle.setText(discussion.getName());
            viewHolder.discussionDate.setText(DateFormatter.getShortDateTime(
                    discussion.getLastModified()));
            viewHolder.lastReply.setText(discussion.getFormattedLastPostMessage());
        }

        @Override
        public View newView(final Context arg0, final Cursor arg1, final ViewGroup arg2) {
            final View view = ((LayoutInflater) arg0.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_discussion_list,
                                                              arg2, false);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.poster = (TextView) view.findViewById(R.id.textview_poster);

            viewHolder.discussionTitle = (TextView) view.findViewById(R.id.textview_first_line);

            viewHolder.lastReply = (TextView) view.findViewById(R.id.textview_second_line);

            viewHolder.discussionDate = (TextView) view.findViewById(R.id.textview_date);

            view.setTag(viewHolder);

            return view;
        }
    }

    /* ========================== Interfaces ======================= */
    public static interface Responses {
        public String onDiscussionSelected = "edu.uwi.mona.mobileourvle.app.Fragments.Forum.ForumDiscussionListFragment.onDiscussionSelected";
    }

    public static interface ResponseArgs {
        final String Discussion = "discussion";
    }

    private static interface Loaders {
        static final int Discussions = 1;
    }

}
