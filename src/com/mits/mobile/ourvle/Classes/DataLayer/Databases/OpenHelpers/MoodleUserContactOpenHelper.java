/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers;

import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseUtil.QB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers.MoodleUserContactOpenHelper.Schema.Column;

/**
 * @author Aston Hamilton
 * 
 */
public class MoodleUserContactOpenHelper extends SQLiteOpenHelper {
    public interface Schema {
	public static final String USER_CONTACTS_TABLE = "user_contacts";
	public static final int VERSION = 1;

	public interface Column {
	    static final String USER_ID = "user_id";
	    static final String CONTACT_URI = "contact_uri";

	}
    }

    public static final String[] DATABASE_COLUMNS =
	    new String[] { Column.USER_ID, Column.CONTACT_URI };

    private static final String DATABASE_TABLE_CREATE =
	    QB.CREATE_TABLE(Schema.USER_CONTACTS_TABLE,
		    QB.PRIMARY_KEY(
			    QB.INTEGER(Column.USER_ID)),
		    QB.TEXT(Column.CONTACT_URI));

    public MoodleUserContactOpenHelper(final Context context) {
	super(context, Schema.USER_CONTACTS_TABLE + ".db",
		null, Schema.VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
	db.execSQL(MoodleUserContactOpenHelper.DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
	    final int newVersion) {
	db.execSQL(QB.DROP(Schema.USER_CONTACTS_TABLE));
	db.execSQL(MoodleUserContactOpenHelper.DATABASE_TABLE_CREATE);
    }

}
