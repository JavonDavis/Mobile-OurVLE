/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers;

import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseUtil.QB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.CourseClassTimeContract;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseEventsOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "course_events";
    public static final int VERSION = 1;

    private static final String DATABASE_TABLE_CREATE =
	    QB.CREATE_TABLE(CourseEventsOpenHelper.TABLE_NAME,
		    QB.PRIMARY_KEY(
			    QB.INTEGER(CourseClassTimeContract.ID)),
		    QB.INTEGER(CourseClassTimeContract.COURSE_ID),
		    QB.INTEGER(CourseClassTimeContract.EVENT_ID));

    public CourseEventsOpenHelper(final Context context) {
	super(context, MoodleCourseOpenHelper.DB_FILE_NAME + ".db",
		null, CourseEventsOpenHelper.VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
	db.execSQL(CourseEventsOpenHelper.DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
	    final int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS "
		+ CourseEventsOpenHelper.TABLE_NAME);
	onCreate(db);
    }
}
