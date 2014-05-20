/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers;

import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseUtil.QB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.CourseVideoesContract;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseVideosOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "course_videos";
    public static final int VERSION = 2;

    private static final String DATABASE_TABLE_CREATE =
	    QB.CREATE_TABLE(CourseVideosOpenHelper.TABLE_NAME,
		    QB.PRIMARY_KEY(
			    QB.INTEGER(CourseVideoesContract._ID)),
		    QB.INTEGER(CourseVideoesContract.COURSE_ID),
		    QB.TEXT(CourseVideoesContract.VIDEO_FILE_PATH),
		    QB.INTEGER(CourseVideoesContract.TIMESTAMP),
		    QB.TEXT(CourseVideoesContract.NOTES));

    public CourseVideosOpenHelper(final Context context) {
	super(context, CourseVideosOpenHelper.TABLE_NAME + ".db",
		null, CourseVideosOpenHelper.VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
	db.execSQL(CourseVideosOpenHelper.DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
	    final int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS "
		+ CourseVideosOpenHelper.TABLE_NAME);
	onCreate(db);
    }

}
