/**
/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers;

import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseUtil.QB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.ForumDiscussionContract;

/**
 * @author Aston Hamilton
 * 
 */
public class ForumDiscussionOpenHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "forum_discussions.db";
    public static final String TABLE_NAME = "forum_discussions";
    public static final int VERSION = 4;

    private static final String DATABASE_TABLE_CREATE =
	    QB.CREATE_TABLE(ForumDiscussionOpenHelper.TABLE_NAME,
		    QB.PRIMARY_KEY(
			    QB.INTEGER(ForumDiscussionContract.Columns._ID)),
		    QB.TEXT(ForumDiscussionContract.Columns.FORUM_ID),
		    QB.TEXT(ForumDiscussionContract.Columns.DISCUSSION_ID),
		    QB.TEXT(ForumDiscussionContract.Columns.MODIFIED),
		    QB.TEXT(ForumDiscussionContract.Columns.COURSE_MODULE_ID),
		    QB.TEXT(ForumDiscussionContract.Columns.DISCUSSION_NAME),
		    QB.TEXT(ForumDiscussionContract.Columns.CREATOR),
		    QB.TEXT(ForumDiscussionContract.Columns.LAST_POST_ID),
		    QB.TEXT(ForumDiscussionContract.Columns.LAST_POST_TEXT));

    public ForumDiscussionOpenHelper(final Context context) {
	super(context, ForumDiscussionOpenHelper.DB_FILE_NAME,
		null, ForumDiscussionOpenHelper.VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
	db.execSQL(ForumDiscussionOpenHelper.DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
	    final int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS "
		+ ForumDiscussionOpenHelper.TABLE_NAME);
	onCreate(db);
    }
}
