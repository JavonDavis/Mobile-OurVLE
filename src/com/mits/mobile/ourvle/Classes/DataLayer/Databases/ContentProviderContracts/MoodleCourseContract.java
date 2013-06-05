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
public interface MoodleCourseContract {
    public static final String AUTHORITY = "com.mits.mobile.ourvle.data.MoodleCourseProvider";
    public static final String BASE_PATH = "coursenotes";

    public static final Uri CONTENT_URI = Uri.parse("content://"
	    + MoodleCourseContract.AUTHORITY
	    + "/" + MoodleCourseContract.BASE_PATH);
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	    + "/mm-moodlecourse";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	    + "/mm-moodlecourse";

    public static interface Columns {
	public static final String _ID = BaseColumns._ID;
	static final String COURSE_ID = "course_id";
	static final String COURSE_NAME = "course_name";
	static final String COURSE_MANAGERS = "course_managers";
    }
}
