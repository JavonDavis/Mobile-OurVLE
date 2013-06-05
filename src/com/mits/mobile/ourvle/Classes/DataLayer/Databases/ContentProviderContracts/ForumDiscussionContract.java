/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Aston Hamilton
 * 
 */
public interface ForumDiscussionContract {
    public static final String AUTHORITY = "com.mits.mobile.ourvle.data.ForumDiscussionProvider";
    public static final String BASE_PATH = "forumdiscussions";

    public static final Uri CONTENT_URI = Uri.parse("content://"
	    + ForumDiscussionContract.AUTHORITY
	    + "/" + ForumDiscussionContract.BASE_PATH);
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	    + "/mm-forumdiscussion";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	    + "/mm-forumdiscussion";

    public static interface Columns {
	public static final String _ID = BaseColumns._ID;
	static final String DISCUSSION_ID = "discussion_id";
	static final String FORUM_ID = "forum_id";
	static final String COURSE_MODULE_ID = "course_module_id";
	static final String DISCUSSION_NAME = "discussion_name";
	static final String CREATOR = "creator";
	static final String MODIFIED = "last_modified";
	static final String LAST_POST_ID = "last_post_id";
	static final String LAST_POST_TEXT = "last_post_text";
    }
}
