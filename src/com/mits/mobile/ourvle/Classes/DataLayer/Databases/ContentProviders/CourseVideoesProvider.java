/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviders;

import org.sourceforge.ah.android.utilities.Widgets.ContentProviders.SimpleContentProvider;

import android.content.UriMatcher;

import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.CourseVideoesContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers.CourseVideosOpenHelper;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseVideoesProvider extends SimpleContentProvider {

    private static final UriMatcher sURIMatcher = new UriMatcher(
	    UriMatcher.NO_MATCH);
    static {
	CourseVideoesProvider.sURIMatcher.addURI(
		CourseVideoesContract.AUTHORITY,
		CourseVideoesContract.BASE_PATH,
		SimpleContentProvider.URI_ALL_ENTITIES);
	CourseVideoesProvider.sURIMatcher.addURI(
		CourseVideoesContract.AUTHORITY,
		CourseVideoesContract.BASE_PATH + "/#",
		SimpleContentProvider.URI_SPECIFIC_ENTITY);
    }

    @Override
    public boolean onCreate() {
	registerContentType(CourseVideoesContract.CONTENT_ITEM_TYPE,
		CourseVideoesContract.CONTENT_TYPE);
	registerSQLiteOpenHelper(new CourseVideosOpenHelper(getContext()));
	registerTable(CourseVideosOpenHelper.TABLE_NAME,
		CourseVideoesContract._ID);
	registerURIMatcher(CourseVideoesProvider.sURIMatcher);
	return super.onCreate();
    }

}
