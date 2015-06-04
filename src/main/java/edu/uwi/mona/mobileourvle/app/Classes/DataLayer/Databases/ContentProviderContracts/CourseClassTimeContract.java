/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Aston Hamilton
 * 
 */
public interface CourseClassTimeContract {
    public static final String AUTHORITY = "edu.uwi.mona.mobileourvle.app.data.ClassCourseTimeProvider";
    public static final String BASE_PATH = "classtimes";

    public static final Uri CONTENT_URI = Uri.parse("content://"
	    + CourseClassTimeContract.AUTHORITY
	    + "/" + CourseClassTimeContract.BASE_PATH);
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	    + "/mm-classtime";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	    + "/mm-classtime";

    public static final String ID = BaseColumns._ID;
    public final String COURSE_ID = "course_id";
    public final String EVENT_ID = "class_event_id";
}
