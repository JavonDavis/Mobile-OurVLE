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
public interface CourseEventsContract {
    public static final String AUTHORITY = "com.mits.mobile.ourvle.data.CourseEventsProvider";
    public static final String BASE_PATH = "events";

    public static final Uri CONTENT_URI = Uri.parse("content://"
	    + CourseEventsContract.AUTHORITY
	    + "/" + CourseEventsContract.BASE_PATH);
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	    + "/mm-events";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	    + "/mm-event";

    public static final String ID = BaseColumns._ID;
    public final String COURSE_ID = "course_id";
    public final String EVENT_ID = "event_id";
}
