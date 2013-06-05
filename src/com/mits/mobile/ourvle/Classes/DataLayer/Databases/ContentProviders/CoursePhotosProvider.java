/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviders;

import org.sourceforge.ah.android.utilities.Widgets.ContentProviders.SimpleContentProvider;

import android.content.UriMatcher;

import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.CoursePhotosContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers.CoursePhotosOpenHelper;

/**
 * @author Aston Hamilton
 * 
 */
public class CoursePhotosProvider extends SimpleContentProvider {

    private static final UriMatcher sURIMatcher = new UriMatcher(
	    UriMatcher.NO_MATCH);
    static {
	CoursePhotosProvider.sURIMatcher.addURI(
		CoursePhotosContract.AUTHORITY,
		CoursePhotosContract.BASE_PATH,
		SimpleContentProvider.URI_ALL_ENTITIES);
	CoursePhotosProvider.sURIMatcher.addURI(
		CoursePhotosContract.AUTHORITY,
		CoursePhotosContract.BASE_PATH + "/#",
		SimpleContentProvider.URI_SPECIFIC_ENTITY);
    }

    @Override
    public boolean onCreate() {
	registerContentType(CoursePhotosContract.CONTENT_ITEM_TYPE,
		CoursePhotosContract.CONTENT_TYPE);

	registerSQLiteOpenHelper(new CoursePhotosOpenHelper(getContext()));

	registerTable(CoursePhotosOpenHelper.TABLE_NAME,
		CoursePhotosContract._ID);

	registerURIMatcher(CoursePhotosProvider.sURIMatcher);
	return super.onCreate();
    }

}
