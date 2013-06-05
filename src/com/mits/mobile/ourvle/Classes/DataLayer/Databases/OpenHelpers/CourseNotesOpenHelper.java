/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers;

import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseUtil.QB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.CourseNotesContract;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseNotesOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "course_notes";
    public static final int VERSION = 2;

    private static final String DATABASE_TABLE_CREATE =
	    QB.CREATE_TABLE(CourseNotesOpenHelper.TABLE_NAME,
		    QB.PRIMARY_KEY(
			    QB.INTEGER(CourseNotesContract._ID)),
		    QB.INTEGER(CourseNotesContract.COURSE_ID),
		    QB.TEXT(CourseNotesContract.TEXT),
		    QB.INTEGER(CourseNotesContract.TIMESTAMP));

    public CourseNotesOpenHelper(final Context context) {
	super(context, CourseNotesOpenHelper.TABLE_NAME + ".db",
		null, CourseNotesOpenHelper.VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
	db.execSQL(CourseNotesOpenHelper.DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
	    final int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS "
		+ CourseNotesOpenHelper.TABLE_NAME);
	onCreate(db);
    }
}
