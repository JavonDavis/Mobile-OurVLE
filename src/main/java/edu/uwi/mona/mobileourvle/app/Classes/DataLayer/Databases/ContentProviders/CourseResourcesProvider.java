/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviders;

import org.sourceforge.ah.android.utilities.Widgets.Util.ArrayUtils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.CourseResourcesContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers.CourseResourceGroupsOpenHelper;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.OpenHelpers.CourseResourcesOpenHelper;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseResourcesProvider extends ContentProvider {

    public static final int URI_ALL_RESOURCES = 0;
    public static final int URI_SPECIFIC_RESOURCE = 1;
    public static final int URI_ALL_RESOURCE_GROUPS = 2;
    public static final int URI_SPECIFIC_RESOURCE_GROUP = 3;

    private static final UriMatcher sURIMatcher = new UriMatcher(
	    UriMatcher.NO_MATCH);
    static {
	CourseResourcesProvider.sURIMatcher.addURI(
		CourseResourcesContract.AUTHORITY,
		CourseResourcesContract.RESOURCES_PATH,
		CourseResourcesProvider.URI_ALL_RESOURCES);
	CourseResourcesProvider.sURIMatcher.addURI(
		CourseResourcesContract.AUTHORITY,
		CourseResourcesContract.RESOURCES_PATH + "/#",
		CourseResourcesProvider.URI_SPECIFIC_RESOURCE);
	CourseResourcesProvider.sURIMatcher.addURI(
		CourseResourcesContract.AUTHORITY,
		CourseResourcesContract.RESOURCE_GROUPS_PATH,
		CourseResourcesProvider.URI_ALL_RESOURCE_GROUPS);
	CourseResourcesProvider.sURIMatcher.addURI(
		CourseResourcesContract.AUTHORITY,
		CourseResourcesContract.RESOURCE_GROUPS_PATH + "/#",
		CourseResourcesProvider.URI_SPECIFIC_RESOURCE_GROUP);
    }

    private SQLiteOpenHelper mResourcesOpenHelper;
    private SQLiteOpenHelper mResourceGroupsOpenHelper;

    @Override
    public boolean onCreate() {
	mResourcesOpenHelper = new CourseResourcesOpenHelper(getContext());
	mResourceGroupsOpenHelper = new CourseResourceGroupsOpenHelper(
		getContext());
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#delete(android.net.Uri,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(final Uri uri, final String selection,
	    String[] selectionArgs) {
	final int uriType = CourseResourcesProvider.sURIMatcher.match(uri);
	final SQLiteDatabase sqlDB;

	final String tableName;
	final String primaryKey = CourseResourcesContract._ID;

	switch (uriType) {
	case URI_ALL_RESOURCES:
	case URI_SPECIFIC_RESOURCE:
	    sqlDB = mResourcesOpenHelper.getWritableDatabase();
	    tableName = CourseResourcesOpenHelper.TABLE_NAME;
	    break;
	case URI_ALL_RESOURCE_GROUPS:
	case URI_SPECIFIC_RESOURCE_GROUP:
	    sqlDB = mResourceGroupsOpenHelper.getWritableDatabase();
	    tableName = CourseResourceGroupsOpenHelper.TABLE_NAME;
	    break;
	default:
	    throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
	}

	int rowsAffected = 0;
	switch (uriType) {
	case URI_SPECIFIC_RESOURCE:
	case URI_SPECIFIC_RESOURCE_GROUP:
	    final String id = uri.getLastPathSegment();
	    if (TextUtils.isEmpty(selection))
		rowsAffected = sqlDB
			.delete(tableName,
				primaryKey + "= ?",
				new String[] { id });
	    else {
		if (selectionArgs != null)
		    selectionArgs = ArrayUtils.extend(selectionArgs,
			    new String[selectionArgs.length + 1],
			    uri.getLastPathSegment());
		else
		    selectionArgs = new String[] { uri.getLastPathSegment() };

		rowsAffected = sqlDB.delete(tableName,
			"(" + selection + ") AND " + primaryKey
				+ "= ?",
			selectionArgs);
	    }
	    break;
	case URI_ALL_RESOURCES:
	case URI_ALL_RESOURCE_GROUPS:
	    rowsAffected = sqlDB.delete(tableName,
		    selection, selectionArgs);

	    break;
	default:
	    throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
	}

	getContext().getContentResolver().notifyChange(uri, null);
	return rowsAffected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(final Uri uri) {
	final int uriType = CourseResourcesProvider.sURIMatcher.match(uri);

	switch (uriType) {
	case URI_ALL_RESOURCE_GROUPS:
	    return CourseResourcesContract.RESOURCE_GROUP_TYPE;
	case URI_ALL_RESOURCES:
	    return CourseResourcesContract.RESOURCE_TYPE;
	case URI_SPECIFIC_RESOURCE_GROUP:
	    return CourseResourcesContract.RESOURCE_GROUP_ITEM_TYPE;
	case URI_SPECIFIC_RESOURCE:
	    return CourseResourcesContract.RESOURCE_ITEM_TYPE;
	}

	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#insert(android.net.Uri,
     * android.content.ContentValues)
     */
    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
	final int uriType = CourseResourcesProvider.sURIMatcher.match(uri);
	final SQLiteDatabase sqlDB;

	final String tableName;

	switch (uriType) {
	case URI_ALL_RESOURCES:
	case URI_SPECIFIC_RESOURCE:
	    sqlDB = mResourcesOpenHelper.getWritableDatabase();
	    tableName = CourseResourcesOpenHelper.TABLE_NAME;
	    break;
	case URI_ALL_RESOURCE_GROUPS:
	case URI_SPECIFIC_RESOURCE_GROUP:
	    sqlDB = mResourceGroupsOpenHelper.getWritableDatabase();
	    tableName = CourseResourceGroupsOpenHelper.TABLE_NAME;
	    break;
	default:
	    tableName = "";
	    throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
	}

	long newId = 0;
	switch (uriType) {
	case URI_ALL_RESOURCES:
	case URI_ALL_RESOURCE_GROUPS:
	    newId = sqlDB.insert(tableName,
		    null, values);

	    break;
	default:
	    throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
	}

	getContext().getContentResolver().notifyChange(uri, null);
	return ContentUris.withAppendedId(uri, newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#query(android.net.Uri,
     * java.lang.String[], java.lang.String, java.lang.String[],
     * java.lang.String)
     */
    @Override
    public Cursor query(final Uri uri, final String[] projection,
	    final String selection,
	    String[] selectionArgs, final String sortOrder) {

	final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

	final int uriType = CourseResourcesProvider.sURIMatcher.match(uri);
	final SQLiteDatabase sqlDB;

	final String tableName;
	final String primaryKey = CourseResourcesContract._ID;

	switch (uriType) {
	case URI_ALL_RESOURCES:
	case URI_SPECIFIC_RESOURCE:
	    sqlDB = mResourcesOpenHelper.getReadableDatabase();
	    tableName = CourseResourcesOpenHelper.TABLE_NAME;
	    break;
	case URI_ALL_RESOURCE_GROUPS:
	case URI_SPECIFIC_RESOURCE_GROUP:
	    sqlDB = mResourceGroupsOpenHelper.getReadableDatabase();
	    tableName = CourseResourceGroupsOpenHelper.TABLE_NAME;
	    break;
	default:
	    throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
	}

	queryBuilder.setTables(tableName);

	switch (uriType) {
	case URI_SPECIFIC_RESOURCE:
	case URI_SPECIFIC_RESOURCE_GROUP:
	    queryBuilder.appendWhere(primaryKey + "= ?");

	    if (selectionArgs != null)
		selectionArgs = ArrayUtils.extend(selectionArgs,
			new String[selectionArgs.length + 1],
			uri.getLastPathSegment());
	    else
		selectionArgs = new String[] { uri.getLastPathSegment() };

	    break;
	case URI_ALL_RESOURCES:
	case URI_ALL_RESOURCE_GROUPS:
	    // no filter
	    break;
	default:
	    throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
	}

	final Cursor cursor = queryBuilder.query(
		sqlDB,
		projection, selection, selectionArgs, null, null, sortOrder);
	cursor.setNotificationUri(getContext().getContentResolver(), uri);
	return cursor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.ContentProvider#update(android.net.Uri,
     * android.content.ContentValues, java.lang.String, java.lang.String[])
     */
    @Override
    public int update(final Uri uri, final ContentValues values,
	    final String selection,
	    String[] selectionArgs) {
	final int uriType = CourseResourcesProvider.sURIMatcher.match(uri);
	final SQLiteDatabase sqlDB;

	final String tableName;
	final String primaryKey = CourseResourcesContract._ID;

	switch (uriType) {
	case URI_ALL_RESOURCES:
	case URI_SPECIFIC_RESOURCE:
	    sqlDB = mResourcesOpenHelper.getWritableDatabase();
	    tableName = CourseResourcesOpenHelper.TABLE_NAME;
	    break;
	case URI_ALL_RESOURCE_GROUPS:
	case URI_SPECIFIC_RESOURCE_GROUP:
	    sqlDB = mResourceGroupsOpenHelper.getWritableDatabase();
	    tableName = CourseResourceGroupsOpenHelper.TABLE_NAME;
	    break;
	default:
	    tableName = "";
	    throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
	}

	int rowsAffected = 0;
	switch (uriType) {
	case URI_SPECIFIC_RESOURCE:
	case URI_SPECIFIC_RESOURCE_GROUP:
	    final String id = uri.getLastPathSegment();
	    if (TextUtils.isEmpty(selection))
		rowsAffected = sqlDB
			.update(tableName, values,
				primaryKey + "= ?",
				new String[] { id });
	    else {
		if (selectionArgs != null)
		    selectionArgs = ArrayUtils.extend(selectionArgs,
			    new String[selectionArgs.length + 1],
			    uri.getLastPathSegment());
		else
		    selectionArgs = new String[] { uri.getLastPathSegment() };

		rowsAffected = sqlDB.delete(
			tableName,
			"(" + selection + ") AND " + primaryKey
				+ "= ?",
			selectionArgs);
	    }
	    break;
	case URI_ALL_RESOURCES:
	case URI_ALL_RESOURCE_GROUPS:
	    rowsAffected = sqlDB.update(tableName,
		    values, selection, selectionArgs);

	    break;
	default:
	    throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
	}

	getContext().getContentResolver().notifyChange(uri, null);
	return rowsAffected;
    }
}
