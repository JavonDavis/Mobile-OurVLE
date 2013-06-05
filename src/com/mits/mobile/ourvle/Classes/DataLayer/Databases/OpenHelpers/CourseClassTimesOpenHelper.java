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
public class CourseClassTimesOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "course_times";
    public static final int VERSION = 2;

    private static final String DATABASE_TABLE_CREATE =
	    QB.CREATE_TABLE(CourseClassTimesOpenHelper.TABLE_NAME,
		    QB.PRIMARY_KEY(
			    QB.INTEGER(CourseClassTimeContract.ID)),
		    QB.INTEGER(CourseClassTimeContract.COURSE_ID),
		    QB.INTEGER(CourseClassTimeContract.EVENT_ID));

    public CourseClassTimesOpenHelper(final Context context) {
	super(context, CourseClassTimesOpenHelper.TABLE_NAME + ".db",
		null, CourseClassTimesOpenHelper.VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
	db.execSQL(CourseClassTimesOpenHelper.DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
	    final int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS "
		+ CourseClassTimesOpenHelper.TABLE_NAME);
	onCreate(db);
    }
}
