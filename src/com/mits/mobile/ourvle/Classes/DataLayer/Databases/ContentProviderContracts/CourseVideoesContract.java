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
public interface CourseVideoesContract {
    public static final String AUTHORITY = "com.mits.mobile.ourvle.data.CourseVideoesProvider";
    public static final String BASE_PATH = "coursevideoes";

    public static final Uri CONTENT_URI = Uri.parse("content://"
	    + CourseVideoesContract.AUTHORITY
	    + "/" + CourseVideoesContract.BASE_PATH);
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	    + "/mm-video";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	    + "/mm-video";

    public static final String _ID = BaseColumns._ID;
    public static final String COURSE_ID = "course_id";
    public static final String VIDEO_FILE_PATH = "video_uri";
    public static final String TIMESTAMP = "timestamp";
    public static final String NOTES = "notes";
}
