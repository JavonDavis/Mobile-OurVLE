/**
 /**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.ForumDiscussionPostContract;

import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseUtil.QB;

/**
 * @author Aston Hamilton
 */
public class ForumDiscussionPostOpenHelper extends SQLiteOpenHelper {

    public static final String DB_FILE_NAME = "forum_discussion_posts.db";
    public static final String TABLE_NAME = "forum_discussion_posts";
    public static final int VERSION = 4;

    private static final String DATABASE_TABLE_CREATE =
            QB.CREATE_TABLE(
                    ForumDiscussionPostOpenHelper.TABLE_NAME,
                    QB.PRIMARY_KEY(
                            QB.INTEGER(ForumDiscussionPostContract.Columns._ID)),
                    QB.TEXT(ForumDiscussionPostContract.Columns.POST_ID),
                    QB.TEXT(ForumDiscussionPostContract.Columns.DISCUSSION_ID),
                    QB.TEXT(ForumDiscussionPostContract.Columns.INDENTATION),
                    QB.TEXT(ForumDiscussionPostContract.Columns.POST_TITLE),
                    QB.TEXT(ForumDiscussionPostContract.Columns.POST_TEXT),
                    QB.TEXT(ForumDiscussionPostContract.Columns.POSTER),
                    QB.TEXT(ForumDiscussionPostContract.Columns.PARENT),
                    QB.TEXT(ForumDiscussionPostContract.Columns.MODIFIED),
                    QB.TEXT(ForumDiscussionPostContract.Columns.CREATED));

    public ForumDiscussionPostOpenHelper(final Context context) {
        super(context, ForumDiscussionPostOpenHelper.DB_FILE_NAME,
              null, ForumDiscussionPostOpenHelper.VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(ForumDiscussionPostOpenHelper.DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
                          final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "
                   + ForumDiscussionPostOpenHelper.TABLE_NAME);
        onCreate(db);
    }
}
