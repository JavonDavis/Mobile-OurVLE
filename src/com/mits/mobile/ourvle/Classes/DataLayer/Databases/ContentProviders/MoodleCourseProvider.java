/**
 *
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviders;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;

import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.MoodleCourseContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.OpenHelpers.MoodleCourseOpenHelper;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.ExtendedMoodleCourse;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.CourseManager;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Users.CourseManagerDescriptor;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONEncoder;
import org.sourceforge.ah.android.utilities.Widgets.ContentProviders.SimpleContentProvider;

import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author Aston Hamilton
 */
public class MoodleCourseProvider extends SimpleContentProvider {

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        MoodleCourseProvider.sURIMatcher.addURI(
                MoodleCourseContract.AUTHORITY,
                MoodleCourseContract.BASE_PATH,
                SimpleContentProvider.URI_ALL_ENTITIES);
        MoodleCourseProvider.sURIMatcher.addURI(
                MoodleCourseContract.AUTHORITY,
                MoodleCourseContract.BASE_PATH + "/#",
                SimpleContentProvider.URI_SPECIFIC_ENTITY);
    }

    @Override
    public boolean onCreate() {
        registerContentType(MoodleCourseContract.CONTENT_ITEM_TYPE,
                            MoodleCourseContract.CONTENT_TYPE);
        registerSQLiteOpenHelper(new MoodleCourseOpenHelper(getContext()));
        registerTable(MoodleCourseOpenHelper.TABLE_NAME,
                      MoodleCourseContract.Columns._ID);
        registerURIMatcher(MoodleCourseProvider.sURIMatcher);
        return super.onCreate();
    }

    @SuppressWarnings("unchecked")
    public static int insertMoodleCourses(final Context context,
                                          final List<MoodleCourse> courses) {

        final ContentValues[] values = new ContentValues[courses
                .size()];

        for (int i = 0; i < courses.size(); i++) {
            final MoodleCourse course = courses.get(i);
            final ContentValues newValues = new ContentValues();
            newValues.put(MoodleCourseContract.Columns.COURSE_ID,
                          course.getId());
            newValues.put(MoodleCourseContract.Columns.COURSE_NAME,
                          course.getName());

            if (course instanceof ExtendedMoodleCourse)
                newValues
                        .put(MoodleCourseContract.Columns.COURSE_MANAGERS,
                             MoodleCourseProvider
                                     .getSerializedCourseManagers(
                                             Arrays
                                                     .asList(((ExtendedMoodleCourse) course)
                                                                     .getManagers())));
            values[i] = newValues;
        }

        return context.getContentResolver().bulkInsert(
                MoodleCourseContract.CONTENT_URI, values);
    }

    public static int removeAllCourses(final Context context) {
        return context.getContentResolver().delete(
                MoodleCourseContract.CONTENT_URI,
                null, null);
    }

    public static String getSerializedCourseManagers(
            final List<CourseManager> managers) {
        return JSONEncoder.getEncodedObjectList(
                new CourseManagerDescriptor(),
                managers);
    }

    public static List<CourseManager> getDeserializedCourseManagers(
            final String managersJSON) {
        return JSONDecoder.getObjectList(
                new CourseManagerDescriptor(), managersJSON);
    }

}
