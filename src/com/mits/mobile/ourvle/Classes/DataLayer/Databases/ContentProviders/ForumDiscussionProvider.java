/**
 *
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviders;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;

import com.google.gson.JsonElement;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.ForumDiscussionContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers.ForumDiscussionOpenHelper;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ExtendedForumDiscussion;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum.DiscussionParentDescriptor;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum.FullForumDiscussionDescriptior;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Users.MoodleUserDescriptor;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONEncoder;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Widgets.ContentProviders.SimpleContentProvider;

import java.util.List;

/**
 * @author Aston Hamilton
 */
public class ForumDiscussionProvider extends SimpleContentProvider {

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        ForumDiscussionProvider.sURIMatcher.addURI(
                ForumDiscussionContract.AUTHORITY,
                ForumDiscussionContract.BASE_PATH,
                SimpleContentProvider.URI_ALL_ENTITIES);
        ForumDiscussionProvider.sURIMatcher.addURI(
                ForumDiscussionContract.AUTHORITY,
                ForumDiscussionContract.BASE_PATH + "/#",
                SimpleContentProvider.URI_SPECIFIC_ENTITY);
    }

    @Override
    public boolean onCreate() {
        registerContentType(ForumDiscussionContract.CONTENT_ITEM_TYPE,
                            ForumDiscussionContract.CONTENT_TYPE);
        registerSQLiteOpenHelper(new ForumDiscussionOpenHelper(getContext()));
        registerTable(ForumDiscussionOpenHelper.TABLE_NAME,
                      ForumDiscussionContract.Columns._ID);
        registerURIMatcher(ForumDiscussionProvider.sURIMatcher);
        return super.onCreate();
    }

    public static int removeAllDiscussions(final Context context,
                                           final DiscussionParent parent) {
        String selection;
        String[] selectionArgs;

        if (parent.isForum()) {
            selection = ForumDiscussionContract.Columns.FORUM_ID + " = ?";
            selectionArgs = new String[]{
                    parent.getForum().getForumid()
                          .toString()
            };
        } else {
            selection = ForumDiscussionContract.Columns.COURSE_MODULE_ID
                        + " = ?";
            selectionArgs = new String[]{
                    parent.getModule().getId()
                          .toString()
            };
        }

        return context.getContentResolver().delete(
                ForumDiscussionContract.CONTENT_URI,
                selection, selectionArgs);

    }

    public static int insertForumDiscussions(final Context context,
                                             final List<ForumDiscussion> discussions) {

        final ContentValues[] values = new ContentValues[discussions
                .size()];

        for (int i = 0; i < discussions.size(); i++) {
            final ForumDiscussion discussion = discussions.get(i);
            final ContentValues newValues = new ContentValues();
            newValues.put(ForumDiscussionContract.Columns.DISCUSSION_ID,
                          discussion.getId());
            newValues.put(ForumDiscussionContract.Columns.DISCUSSION_NAME,
                          discussion.getName());
            newValues
                    .put(ForumDiscussionContract.Columns.MODIFIED,
                         DateFormatter.getISODateString(discussion
                                                                .getLastModified()));
            newValues
                    .put(ForumDiscussionContract.Columns.CREATOR,
                         ForumDiscussionProvider
                                 .getSerializedForumCreator(discussion
                                                                    .getCreator()));

            if (discussion.getParent().isForum())
                newValues.put(ForumDiscussionContract.Columns.FORUM_ID,
                              discussion.getParent().getForum().getForumid());
            else if (discussion.getParent().isModule())
                newValues.put(ForumDiscussionContract.Columns.COURSE_MODULE_ID,
                              discussion.getParent().getModule().getId());

            if (discussion instanceof ExtendedForumDiscussion) {
                newValues
                        .put(ForumDiscussionContract.Columns.LAST_POST_ID,
                             ((ExtendedForumDiscussion) discussion)
                                     .getLastPostId());
                newValues
                        .put(ForumDiscussionContract.Columns.LAST_POST_TEXT,
                             ((ExtendedForumDiscussion) discussion)
                                     .getLastPortMessage());
            }
            values[i] = newValues;
        }

        return context.getContentResolver().bulkInsert(
                ForumDiscussionContract.CONTENT_URI, values);
    }

    /* ================= Serializers ================ */
    public static MoodleUser getDeserializeForumCreator(
            final String serializedForumCreator) {
        return JSONDecoder.getObject(new MoodleUserDescriptor(),
                                     serializedForumCreator);
    }

    public static String getSerializedForumCreator(final MoodleUser creator) {
        return JSONEncoder
                .getEncodedObject(new MoodleUserDescriptor(), creator);
    }

    public static String getSerializedParent(final DiscussionParent parent) {
        String courseId = "";

        if (parent.getModule() != null) {
            courseId = parent.getModule().getCourseId();
        }

        final JsonElement serializedParent = JSONEncoder
                .getEncodedObjectElement(new DiscussionParentDescriptor(courseId),
                                         parent);

        return serializedParent.toString();
    }

    public static DiscussionParent getDeserializedParent(
            final String serializedParent) {
        return JSONDecoder.getObject(new DiscussionParentDescriptor(null),
                                     serializedParent);
    }

    public static ForumDiscussion getDeserializedDiscussion(
            final String serializedDiscussion) {

        return JSONDecoder.getObject(new FullForumDiscussionDescriptior(null),
                                     serializedDiscussion);
    }

    public static String getSerializedDiscussion(
            final ForumDiscussion discussion) {
        return JSONEncoder
                .getEncodedObject(new FullForumDiscussionDescriptior(null),
                                  discussion);
    }
}
