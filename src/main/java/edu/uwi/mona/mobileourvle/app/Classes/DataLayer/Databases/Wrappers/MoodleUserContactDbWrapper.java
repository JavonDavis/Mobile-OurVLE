/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.Wrappers;

import java.util.Arrays;

import org.sourceforge.ah.android.utilities.AndroidUtil.ContactsUtil;
import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseUtil.QB;
import org.sourceforge.ah.android.utilities.Databases.SQLiteDatabaseWrapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Android.PhoneContact;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers.MoodleUserContactOpenHelper;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers.MoodleUserContactOpenHelper.Schema;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 */
public class MoodleUserContactDbWrapper extends SQLiteDatabaseWrapper {

    private final Context mContext;

    public MoodleUserContactDbWrapper(final Context context) {
        super(new MoodleUserContactOpenHelper(context));
        mContext = context;
    }

    public PhoneContact getMoodleUserContact(final MoodleUser user) {
        final SQLiteDatabase db = getWrappedDatabase();
        final String query = QB.SELECT(
                Arrays.asList(Schema.Column.USER_ID,
                              Schema.Column.CONTACT_URI),
                Schema.USER_CONTACTS_TABLE,
                QB.EQ(Schema.Column.USER_ID,
                      user.getId().toString()));

        final Cursor c = db.rawQuery(query, new String[]{});

        if (c.getCount() == 0)
            return null;

        c.moveToFirst();

        final Uri contactUri = Uri.parse(c.getString(1));

        if (ContactsUtil.contactExists(mContext, contactUri))
            return new PhoneContact(mContext, contactUri);
        else
            db.delete(Schema.USER_CONTACTS_TABLE,
                      QB.EQ(Schema.Column.USER_ID, c.getString(0)), null);
        return null;
    }

    public void saveContact(final MoodleUser user, final PhoneContact contact) {
        final SQLiteDatabase db = getWrappedDatabase();

        final String query = QB.SELECT(
                Arrays.asList(Schema.Column.USER_ID),
                Schema.USER_CONTACTS_TABLE,
                QB.EQ(Schema.Column.USER_ID,
                      user.getId().toString()));

        final Cursor c = db.rawQuery(query, new String[]{});

        final ContentValues values = new ContentValues();
        values.put(Schema.Column.USER_ID,
                   user.getId().toString());
        values.put(Schema.Column.CONTACT_URI,
                   contact.getUri().toString());

        if (c.getCount() == 0)
            db.insert(Schema.USER_CONTACTS_TABLE, null,
                      values);
        else
            db.update(Schema.USER_CONTACTS_TABLE,
                      values,
                      QB.EQ(Schema.Column.USER_ID,
                            user.getId().toString()),
                      null);
    }
}
