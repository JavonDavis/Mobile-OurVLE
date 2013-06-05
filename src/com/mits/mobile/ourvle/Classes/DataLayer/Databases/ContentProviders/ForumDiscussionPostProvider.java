/**
 *
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviders;

import java.util.List;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONEncoder;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Widgets.ContentProviders.SimpleContentProvider;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;

import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.ForumDiscussionPostContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers.ForumDiscussionPostOpenHelper;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Users.MoodleUserDescriptor;
import com.mits.mobile.ourvle.Fragments.Components.ForumDiscussionPostListFragment.EntityWrappers.ExtendedDiscussionPostWrapper;

/**
 * @author Aston Hamilton
 */
public class ForumDiscussionPostProvider extends SimpleContentProvider {

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        ForumDiscussionPostProvider.sURIMatcher.addURI(ForumDiscussionPostContract.AUTHORITY, ForumDiscussionPostContract.BASE_PATH, SimpleContentProvider.URI_ALL_ENTITIES);
        ForumDiscussionPostProvider.sURIMatcher.addURI(ForumDiscussionPostContract.AUTHORITY, ForumDiscussionPostContract.BASE_PATH + "/#", SimpleContentProvider.URI_SPECIFIC_ENTITY);
    }

    @Override
    public boolean onCreate() {
        registerContentType(ForumDiscussionPostContract.CONTENT_ITEM_TYPE, ForumDiscussionPostContract.CONTENT_TYPE);
        registerSQLiteOpenHelper(new ForumDiscussionPostOpenHelper(getContext()));
        registerTable(ForumDiscussionPostOpenHelper.TABLE_NAME, ForumDiscussionPostContract.Columns._ID);
        registerURIMatcher(ForumDiscussionPostProvider.sURIMatcher);
        return super.onCreate();
    }

    public static int removeAllPosts(final Context context, final ForumDiscussion discussion) {
        String selection;
        String[] selectionArgs;

        selection = ForumDiscussionPostContract.Columns.DISCUSSION_ID + " = ?";
        selectionArgs = new String[]{discussion.getId().toString()};

        return context.getContentResolver().delete(ForumDiscussionPostContract.CONTENT_URI, selection, selectionArgs);

    }

    public static int insertExtendedDiscussionPosts(final Context context, final List<ExtendedDiscussionPostWrapper> posts) {

        final ContentValues[] values = new ContentValues[posts.size()];

        for (int i = 0; i < posts.size(); i++) {
            final ExtendedDiscussionPostWrapper ePost = posts.get(i);
            final DiscussionPost post = ePost.getPost();
            final ContentValues newValues = new ContentValues();
            newValues.put(ForumDiscussionPostContract.Columns.POST_ID, post.getId());
            newValues.put(ForumDiscussionPostContract.Columns.DISCUSSION_ID, post.getParentId());
            newValues.put(ForumDiscussionPostContract.Columns.CREATED, DateFormatter.getISODateString(
                    post.getDateCreaded()));
            newValues.put(ForumDiscussionPostContract.Columns.POST_TITLE, post.getSubject());
            newValues.put(ForumDiscussionPostContract.Columns.INDENTATION, ePost.getIndentationFactor());
            newValues.put(ForumDiscussionPostContract.Columns.MODIFIED, DateFormatter.getISODateString(post.getDateLastModified()));
            newValues.put(ForumDiscussionPostContract.Columns.POSTER, ForumDiscussionPostProvider.getSerializedPoster(post.getPoster()));

            values[i] = newValues;
        }

        return context.getContentResolver().bulkInsert(ForumDiscussionPostContract.CONTENT_URI, values);
    }

    /* ================= Serializers ================ */
    public static MoodleUser getDeserializePoster(final String serializedForumCreator) {
        return JSONDecoder.getObject(new MoodleUserDescriptor(), serializedForumCreator);
    }

    public static String getSerializedPoster(final MoodleUser creator) {
        return JSONEncoder.getEncodedObject(new MoodleUserDescriptor(), creator);
    }

}
