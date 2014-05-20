/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviders;

import org.sourceforge.ah.android.utilities.Widgets.ContentProviders.SimpleContentProvider;

import android.content.UriMatcher;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.CourseClassTimeContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers.CourseClassTimesOpenHelper;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseClassTimeProvider extends SimpleContentProvider {

    private static final UriMatcher sURIMatcher = new UriMatcher(
	    UriMatcher.NO_MATCH);
    static {
	CourseClassTimeProvider.sURIMatcher.addURI(
		CourseClassTimeContract.AUTHORITY,
		CourseClassTimeContract.BASE_PATH,
		SimpleContentProvider.URI_ALL_ENTITIES);
	CourseClassTimeProvider.sURIMatcher.addURI(
		CourseClassTimeContract.AUTHORITY,
		CourseClassTimeContract.BASE_PATH + "/#",
		SimpleContentProvider.URI_SPECIFIC_ENTITY);
    }

    @Override
    public boolean onCreate() {
	registerContentType(CourseClassTimeContract.CONTENT_ITEM_TYPE,
		CourseClassTimeContract.CONTENT_TYPE);
	registerSQLiteOpenHelper(new CourseClassTimesOpenHelper(getContext()));
	registerTable(CourseClassTimesOpenHelper.TABLE_NAME,
		CourseClassTimeContract.ID);
	registerURIMatcher(CourseClassTimeProvider.sURIMatcher);
	return super.onCreate();
    }

}
