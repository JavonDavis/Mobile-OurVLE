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
public interface CoursePhotosContract {
    public static final String AUTHORITY = "edu.uwi.mona.mobileourvle.app.data.CoursePhotosProvider";
    public static final String BASE_PATH = "coursephotos";

    public static final Uri CONTENT_URI = Uri.parse("content://"
	    + CoursePhotosContract.AUTHORITY
	    + "/" + CoursePhotosContract.BASE_PATH);
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	    + "/mm-photo";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	    + "/mm-photo";

    public static final String _ID = BaseColumns._ID;
    static final String COURSE_ID = "course_id";
    static final String PHOTO_FILE_PATH = "photo_uri";
    static final String TIMESTAMP = "timestamp";
    static final String NOTES = "notes";
}
