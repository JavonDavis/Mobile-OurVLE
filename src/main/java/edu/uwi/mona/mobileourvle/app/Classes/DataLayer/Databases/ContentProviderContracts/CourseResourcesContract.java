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
public interface CourseResourcesContract {
    public static final String AUTHORITY = "edu.uwi.mona.mobileourvle.app.data.CourcesResourcesProvider";
    public static final String RESOURCES_PATH = "resources";
    public static final String RESOURCE_GROUPS_PATH = "groups";

    public static final Uri COURSE_RESOURCE_URI = Uri.parse("content://"
	    + CourseResourcesContract.AUTHORITY
	    + "/" + CourseResourcesContract.RESOURCES_PATH);
    public static final Uri COURSE_RESOURCE_GROUP_URI = Uri.parse("content://"
	    + CourseResourcesContract.AUTHORITY
	    + "/" + CourseResourcesContract.RESOURCES_PATH + "/"
	    + CourseResourcesContract.RESOURCE_GROUPS_PATH);

    public static final String RESOURCE_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	    + "/mm-resources";

    public static final String RESOURCE_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	    + "/mm-resources";

    public static final String RESOURCE_GROUP_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	    + "/mm-resources/group";

    public static final String RESOURCE_GROUP_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	    + "/mm-resources/group";

    public static final String _ID = BaseColumns._ID;
    public final String COURSE_ID = "course_id";
    public final String RESOURCE_ID = "resource_id";
    public final String TITLE = "title";
    public final String LINK = "link";
    public final String TYPE = "type";
    public final String STATUS = "status";
    public final String GROUP_ID = "group";
    public final String GROUP_NAME = "group_name";
}
