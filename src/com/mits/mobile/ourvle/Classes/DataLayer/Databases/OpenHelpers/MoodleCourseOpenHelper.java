/**
/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers;

import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseUtil.QB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.MoodleCourseContract;

/**
 * @author Aston Hamilton
 * 
 */
public class MoodleCourseOpenHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "moddle_courses.db";
    public static final String TABLE_NAME = "moddle_courses";
    public static final int VERSION = 2;

    private static final String DATABASE_TABLE_CREATE =
	    QB.CREATE_TABLE(MoodleCourseOpenHelper.TABLE_NAME,
		    QB.PRIMARY_KEY(
			    QB.INTEGER(MoodleCourseContract.Columns._ID)),
		    QB.TEXT(MoodleCourseContract.Columns.COURSE_ID),
		    QB.TEXT(MoodleCourseContract.Columns.COURSE_NAME),
		    QB.TEXT(MoodleCourseContract.Columns.COURSE_MANAGERS));

    public MoodleCourseOpenHelper(final Context context) {
	super(context, MoodleCourseOpenHelper.DB_FILE_NAME,
		null, MoodleCourseOpenHelper.VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
	db.execSQL(MoodleCourseOpenHelper.DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
	    final int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS "
		+ MoodleCourseOpenHelper.TABLE_NAME);
	onCreate(db);
    }
}
