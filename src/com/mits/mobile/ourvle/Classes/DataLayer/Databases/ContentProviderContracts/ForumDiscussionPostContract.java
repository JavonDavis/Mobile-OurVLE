/**
 *
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Aston Hamilton
 */
public interface ForumDiscussionPostContract {
    public static final String AUTHORITY = "com.mits.mobile.ourvle.data.ForumDiscussionPostProvider";
    public static final String BASE_PATH = "forumdiscussionspost";

    public static final Uri CONTENT_URI = Uri.parse("content://" + ForumDiscussionPostContract.AUTHORITY + "/" + ForumDiscussionPostContract.BASE_PATH);
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/mm-forumdiscussionpost";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/mm-forumdiscussionpost";

    public static interface Columns {
        public static final String _ID = BaseColumns._ID;
        static final String POST_ID = "post_id";
        static final String DISCUSSION_ID = "discussion_id";
        static final String POSTER = "poster";
        static final String PARENT = "parent";
        static final String POST_TEXT = "post_text";
        static final String CREATED = "created";
        static final String MODIFIED = "last_modified";
        static final String POST_TITLE = "title";
        static final String INDENTATION = "indentation";
    }
}
