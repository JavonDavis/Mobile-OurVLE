/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers;

import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseUtil.QB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.CoursePhotosContract;

/**
 * @author Aston Hamilton
 * 
 */
public class CoursePhotosOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "course_photos";
    public static final int VERSION = 5;

    private static final String DATABASE_TABLE_CREATE =
	    QB.CREATE_TABLE(CoursePhotosOpenHelper.TABLE_NAME,
		    QB.PRIMARY_KEY(
			    QB.INTEGER(CoursePhotosContract._ID)),
		    QB.INTEGER(CoursePhotosContract.COURSE_ID),
		    QB.TEXT(CoursePhotosContract.PHOTO_FILE_PATH),
		    QB.INTEGER(CoursePhotosContract.TIMESTAMP),
		    QB.TEXT(CoursePhotosContract.NOTES));

    public CoursePhotosOpenHelper(final Context context) {
	super(context, CoursePhotosOpenHelper.TABLE_NAME + ".db",
		null, CoursePhotosOpenHelper.VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
	db.execSQL(CoursePhotosOpenHelper.DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
	    final int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS "
		+ CoursePhotosOpenHelper.TABLE_NAME);
	onCreate(db);
    }


    public boolean deletePhoto(Long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CoursePhotosOpenHelper.TABLE_NAME, CoursePhotosContract._ID+" = "+id , null);

        return true;
    }

    public boolean deletePhotoByFilePath(String path)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CoursePhotosOpenHelper.TABLE_NAME, CoursePhotosContract.PHOTO_FILE_PATH+" = ?" , new String[]{path});

        return true;
    }
}
