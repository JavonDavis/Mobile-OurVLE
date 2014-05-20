/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers;

import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseUtil.QB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.CourseResourcesContract;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseResourcesOpenHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "course_resources.db";

    public static final String TABLE_NAME = "course_resources";
    public static final int VERSION = 1;

    private static final String DATABASE_TABLE_CREATE =
	    QB.CREATE_TABLE(CourseResourcesOpenHelper.TABLE_NAME,
		    QB.PRIMARY_KEY(
			    QB.INTEGER(CourseResourcesContract._ID)),
		    QB.INTEGER(CourseResourcesContract.COURSE_ID),
		    QB.INTEGER(CourseResourcesContract.RESOURCE_ID),
		    QB.TEXT(CourseResourcesContract.TITLE),
		    QB.TEXT(CourseResourcesContract.LINK),
		    QB.INTEGER(CourseResourcesContract.TYPE),
		    QB.INTEGER(CourseResourcesContract.STATUS),
		    QB.INTEGER(CourseResourcesContract.GROUP_ID));

    public CourseResourcesOpenHelper(final Context context) {
	super(context, CourseResourcesOpenHelper.DB_FILE_NAME,
		null, CourseResourcesOpenHelper.VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
	db.execSQL(CourseResourcesOpenHelper.DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
	    final int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS "
		+ CourseResourcesOpenHelper.TABLE_NAME);
	onCreate(db);
    }
}
