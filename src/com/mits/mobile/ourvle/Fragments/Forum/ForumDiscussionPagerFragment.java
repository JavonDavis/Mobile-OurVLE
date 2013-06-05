/**
 *
 */
package com.mits.mobile.ourvle.Fragments.Forum;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.ForumDiscussionContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviders.ForumDiscussionProvider;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ExtendedForumDiscussion;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.DiscussionParentParcel;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.ForumDiscussionParcel;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Fragments.Components.AuthenticatedFragment;
import com.mits.mobile.ourvle.R;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;

/**
 * @author Aston Hamilton
 */
public class ForumDiscussionPagerFragment extends AuthenticatedFragment implements LoaderCallbacks<Cursor> {

    private DiscussionParent mParent;
    private long mCurrentDiscussionId = -1L;

    private DiscussionPager mDiscussionPagerAdapter;
    private ViewPager mPager;

    private boolean sFirstLoad = true;

    public static ForumDiscussionPagerFragment newInstance(final UserSession session, final long currentDiscussionId, final DiscussionParent parent) {
        final ForumDiscussionPagerFragment f = new ForumDiscussionPagerFragment();

        f.setUserSession(session);
        f.setCurrentDiscussionId(currentDiscussionId);
        f.setDIscussionParent(parent);
        return f;
    }

    public void setCurrentDiscussionId(final long currentDiscussion) {
        getFragmentArguments().putLong(ParcelKeys.FORUM_DISCUSSION_ID, currentDiscussion);

        mCurrentDiscussionId = currentDiscussion;
        if (mDiscussionPagerAdapter != null && mPager != null) {
            final int position = mDiscussionPagerAdapter.getDiscussionPositionById(currentDiscussion);
            mPager.setCurrentItem(position, true);

            final ForumDiscussion discussion = mDiscussionPagerAdapter.getDiscussion(position);
            final Bundle data = new Bundle();
            data.putParcelable(ResponseArgs.Discussion, new ForumDiscussionParcel(discussion));
            sendResponse(Responses.onDiscussionSelected, data);
        }
    }

    public void setDIscussionParent(final DiscussionParent parent) {
        getFragmentArguments().putParcelable(ParcelKeys.PARENT, new DiscussionParentParcel(parent));

        mParent = parent;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        // needs to be called before 'getUserSession'
        super.onCreate(savedInstanceState);
        mCurrentDiscussionId = getFragmentArguments().getLong(ParcelKeys.FORUM_DISCUSSION_ID);

        mParent = ((DiscussionParentParcel) getFragmentArguments().getParcelable(ParcelKeys.PARENT)).getWrappedObejct();

        mDiscussionPagerAdapter = new DiscussionPager(getFragmentManager(), getUserSession(), mParent, null);

        getLoaderManager().initLoader(Loaders.Discussions, null, this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_discussion_pager, container, false);

        mPager = (ViewPager) v.findViewById(R.id.pager);
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(final int position) {
                final ForumDiscussion discussion = mDiscussionPagerAdapter.getDiscussion(position);
                final Bundle data = new Bundle();
                data.putParcelable(ResponseArgs.Discussion, new ForumDiscussionParcel(discussion));
                sendResponse(Responses.onDiscussionSelected, data);

            }

            @Override
            public void onPageScrolled(final int arg0, final float arg1, final int arg2) {
                // Do nothing

            }

            @Override
            public void onPageScrollStateChanged(final int arg0) {
                // Do nothing

            }
        });

        mPager.setAdapter(mDiscussionPagerAdapter);
        setCurrentDiscussionId(mCurrentDiscussionId);
        return v;
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

            return new CursorLoader(getParentActivity(), ForumDiscussionContract.CONTENT_URI, new String[]{ForumDiscussionContract.Columns._ID, ForumDiscussionContract.Columns.DISCUSSION_ID, ForumDiscussionContract.Columns.DISCUSSION_NAME, ForumDiscussionContract.Columns.CREATOR, ForumDiscussionContract.Columns.MODIFIED, ForumDiscussionContract.Columns.LAST_POST_ID, ForumDiscussionContract.Columns.LAST_POST_TEXT}, selection, selectionArgs, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> arg0, final Cursor arg1) {
        if (arg0.getId() == Loaders.Discussions) {
            mDiscussionPagerAdapter.swapCursor(arg1);

            if (sFirstLoad && mCurrentDiscussionId >= 0)
                setCurrentDiscussionId(mCurrentDiscussionId);
            sFirstLoad = false;
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> arg0) {
        if (arg0.getId() == Loaders.Discussions)
            mDiscussionPagerAdapter.swapCursor(null);
    }

    /* ================ Private Classes ======================= */
    private static class DiscussionPager extends FragmentStatePagerAdapter {

        private final UserSession mUserSession;

        private final DiscussionParent mParent;
        private Cursor mCursor;

        private final SparseArray<ForumDiscussion> mDiscussionCache = new SparseArray<ForumDiscussion>();

        public DiscussionPager(final FragmentManager fm, final UserSession userSession, final DiscussionParent parent, final Cursor cursor) {
            super(fm);
            mParent = parent;
            mUserSession = userSession;
        }

        @Override
        public int getCount() {
            if (mCursor == null)
                return 0;
            return mCursor.getCount();
        }

        @Override
        public Fragment getItem(final int position) {
            final ForumDiscussion discussion = getDiscussion(position);
            final Fragment f = ForumDiscussionPostListFragment.newInstance(mUserSession, discussion);

            return f;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            final ForumDiscussion discussion = getDiscussion(position);
            return discussion.getName();
        }

        public int getDiscussionPositionById(final long discussionId) {

            if (mCursor != null && mCursor.moveToFirst()) {
                int position = 0;
                do {
                    if (ForumDiscussionCursorWrapper.getDiscussionId(mCursor) == discussionId)
                        return position;
                    position++;
                } while (mCursor.moveToNext());
            }
            return -1;
        }

        public void swapCursor(final Cursor cursor) {
            mCursor = cursor;
            notifyDataSetChanged();
        }

        public ForumDiscussion getDiscussion(final int position) {

            if (mCursor == null)
                return null;

            ForumDiscussion discussion = mDiscussionCache.get(position);

            if (discussion == null) {
                mCursor.moveToPosition(position);
                discussion = ForumDiscussionCursorWrapper.getDiscussion(mCursor, mParent);
                mDiscussionCache.append(position, discussion);
            }

            return discussion;
        }
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

        public static ExtendedForumDiscussion getDiscussion(final Cursor c, final DiscussionParent parent) {
            return new ExtendedForumDiscussion(ForumDiscussionCursorWrapper.getDiscussionId(c), ForumDiscussionCursorWrapper.getDiscussionName(c), ForumDiscussionCursorWrapper.getDiscussionCreator(c), ForumDiscussionCursorWrapper.getDiscussionModified(c), ForumDiscussionCursorWrapper.getLastPostId(c), ForumDiscussionCursorWrapper.getLastPostText(c), parent);
        }
    }

    /* ============================== Interfaces ================== */
    public static interface Responses {
        public String onDiscussionSelected = "com.mits.mobile.ourvle.Fragments.Forum.ForumDiscussionPagerFragment.onDiscussionSelected";
    }

    public static interface ResponseArgs {
        String Discussion = "disscussion";
    }

    private static interface Loaders {
        static final int Discussions = 1;
    }
}
