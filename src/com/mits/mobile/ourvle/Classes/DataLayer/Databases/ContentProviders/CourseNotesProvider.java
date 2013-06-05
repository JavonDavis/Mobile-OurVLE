/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviders;

import org.sourceforge.ah.android.utilities.Widgets.ContentProviders.SimpleContentProvider;

import android.content.UriMatcher;

import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.CourseNotesContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers.CourseNotesOpenHelper;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseNotesProvider extends SimpleContentProvider {

    private static final UriMatcher sURIMatcher = new UriMatcher(
	    UriMatcher.NO_MATCH);
    static {
	CourseNotesProvider.sURIMatcher.addURI(
		CourseNotesContract.AUTHORITY,
		CourseNotesContract.BASE_PATH,
		SimpleContentProvider.URI_ALL_ENTITIES);
	CourseNotesProvider.sURIMatcher.addURI(
		CourseNotesContract.AUTHORITY,
		CourseNotesContract.BASE_PATH + "/#",
		SimpleContentProvider.URI_SPECIFIC_ENTITY);
    }

    @Override
    public boolean onCreate() {
	registerContentType(CourseNotesContract.CONTENT_ITEM_TYPE,
		CourseNotesContract.CONTENT_TYPE);
	registerSQLiteOpenHelper(new CourseNotesOpenHelper(getContext()));
	registerTable(CourseNotesOpenHelper.TABLE_NAME,
		CourseNotesContract._ID);
	registerURIMatcher(CourseNotesProvider.sURIMatcher);
	return super.onCreate();
    }

}
