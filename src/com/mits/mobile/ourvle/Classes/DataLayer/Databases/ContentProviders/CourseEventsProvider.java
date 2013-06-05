/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviders;

import org.sourceforge.ah.android.utilities.Widgets.ContentProviders.SimpleContentProvider;

import android.content.UriMatcher;

import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.CourseEventsContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers.CourseEventsOpenHelper;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseEventsProvider extends SimpleContentProvider {

    private static final UriMatcher sURIMatcher = new UriMatcher(
	    UriMatcher.NO_MATCH);
    static {
	CourseEventsProvider.sURIMatcher.addURI(
		CourseEventsContract.AUTHORITY,
		CourseEventsContract.BASE_PATH,
		SimpleContentProvider.URI_ALL_ENTITIES);
	CourseEventsProvider.sURIMatcher.addURI(
		CourseEventsContract.AUTHORITY,
		CourseEventsContract.BASE_PATH + "/#",
		SimpleContentProvider.URI_SPECIFIC_ENTITY);
    }

    @Override
    public boolean onCreate() {
	registerContentType(CourseEventsContract.CONTENT_ITEM_TYPE,
		CourseEventsContract.CONTENT_TYPE);
	registerSQLiteOpenHelper(new CourseEventsOpenHelper(getContext()));
	registerTable(CourseEventsOpenHelper.TABLE_NAME,
		CourseEventsContract.ID);
	registerURIMatcher(CourseEventsProvider.sURIMatcher);
	return super.onCreate();
    }

}
