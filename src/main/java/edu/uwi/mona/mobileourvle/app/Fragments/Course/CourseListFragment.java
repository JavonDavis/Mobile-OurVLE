/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Course;

import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.ContentProviders.EntityManagerContract;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.EntitySyncronizer;
import org.sourceforge.ah.android.utilities.Plugins.EntitySyncronizerPlugin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.MoodleCourseContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviders.MoodleCourseProvider;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.CourseManager;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.SyncEntities.MoodleCourseSyncronizationManager;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.AuthenticatedListFragment;

/**
 * @author Aston Hamilton
 */
public class CourseListFragment extends AuthenticatedListFragment implements
        LoaderCallbacks<Cursor> {

    private MoodleCourseAdapter mAdapter;

    public static CourseListFragment newInstance(final UserSession session) {
        final CourseListFragment f = new CourseListFragment();

        f.setUserSession(session);

        return f;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {

        mAdapter = new MoodleCourseAdapter(getParentActivity(), null,
                                           CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


        final EntitySyncronizerPlugin plugin = new EntitySyncronizerPlugin(
                new MoodleCourseSyncronizationManager());

        registerPlugin(plugin);

        getLoaderManager().initLoader(Loaders.LoadCourses, null, this);

        setListAdapter(mAdapter);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater
                .inflate(R.layout.fragment_course_list, container, false);
    }

    @Override
    public void onListItemClick(final ListView l, final View v,
                                final int position, final long id) {
        final MoodleCourse course = MoodleCourseCursorWrapper
                .getCourse((Cursor) mAdapter.getItem(position));

        final Bundle b = new Bundle();
        b.putParcelable(SharedConstants.ParcelKeys.MOODLE_COURSE,
                        new MoodleCourseParcel(course));
        sendResponse(Responses.onCourseSelected, b);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle arg1) {
        switch (id) {
            case Loaders.LoadCourses:
                return new CursorLoader(getParentActivity(),
                                        MoodleCourseContract.CONTENT_URI,
                                        new String[]{
                                                MoodleCourseContract.Columns._ID,
                                                MoodleCourseContract.Columns.COURSE_ID,
                                                MoodleCourseContract.Columns.COURSE_NAME,
                                                MoodleCourseContract.Columns.COURSE_MANAGERS
                                        },
                                        null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        switch (loader.getId()) {
            case Loaders.LoadCourses:
                mAdapter.swapCursor(cursor);
        }

    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        switch (loader.getId()) {
            case Loaders.LoadCourses:
                mAdapter.swapCursor(null);
        }

    }


    /* ================= Private classes ================= */
    private static class MoodleCourseCursorWrapper {
        public static Long getCourseId(final Cursor cursor) {
            return cursor.getLong(1);
        }

        public static String getCourseName(final Cursor cursor) {
            return cursor.getString(2);
        }

        public static MoodleCourse getCourse(final Cursor cursor) {
            return new MoodleCourse(
                    MoodleCourseCursorWrapper.getCourseId(cursor),
                    MoodleCourseCursorWrapper.getCourseName(cursor));
        }

        public static List<CourseManager> getCourseManagers(final Cursor cursor) {
            final String serializedManagers = cursor.getString(3);

            return MoodleCourseProvider
                    .getDeserializedCourseManagers(serializedManagers);
        }

    }

    private static class MoodleCourseAdapter extends CursorAdapter {

        class ViewHolder {
            TextView courseTitle;
            TextView courseManagers;
        }

        public MoodleCourseAdapter(final Context context, final Cursor c,
                                   final int flags) {
            super(context, c, flags);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void bindView(final View v, final Context arg1,
                             final Cursor arg2) {
            final ViewHolder viewHolder = (ViewHolder) v.getTag();

            final String courseName = MoodleCourseCursorWrapper
                    .getCourseName(arg2);

            final List<CourseManager> managers = MoodleCourseCursorWrapper
                    .getCourseManagers(arg2);

            final StringBuilder managersBuilder = new StringBuilder();

            boolean first = true;
            for (final CourseManager manager : managers) {
                if (!first) {
                    first = false;
                    managersBuilder.append(", ");
                }
                managersBuilder.append(manager.getFirstName()
                                              .toUpperCase(Locale.US)
                                              .charAt(0));
                managersBuilder.append(". ");
                managersBuilder.append(manager.getLastName().toUpperCase(
                        Locale.US));
            }
            viewHolder.courseTitle.setText(courseName);
            viewHolder.courseManagers.setText(managersBuilder.toString());
        }

        @Override
        public View newView(final Context context, final Cursor arg1,
                            final ViewGroup root) {

            final LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.list_item_course_list,
                                               root,
                                               false);

            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.courseTitle = (TextView) view
                    .findViewById(R.id.textview_course_title);

            viewHolder.courseManagers = (TextView) view
                    .findViewById(R.id.textview_course_managers);

            view.setTag(viewHolder);

            return view;

        }
    }

    /* ========================== Interfaces ======================= */
    public static interface ResponseArgs {
        String Course = SharedConstants.ParcelKeys.MOODLE_COURSE;
    }

    public static interface Responses {
        String onCourseSelected = "com.mits.fragments.responses.CourseListFragment.onCourseSelected";
    }

    private static interface Loaders {
        int LoadCourses = 0;
    }

}
