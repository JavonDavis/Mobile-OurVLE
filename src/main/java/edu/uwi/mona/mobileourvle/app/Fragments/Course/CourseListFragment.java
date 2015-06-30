/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.sourceforge.ah.android.utilities.Plugins.EntitySyncronizerPlugin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import edu.uwi.mona.mobileourvle.app.Activities.LoginMainActivity;
import edu.uwi.mona.mobileourvle.app.Classes.Colors;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.AuthenticatedFragment;
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
public class CourseListFragment extends AuthenticatedFragment {

    //private MoodleCourseAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MoodleCourse> moodleCourses;

    public static CourseListFragment newInstance(final UserSession session) {
        final CourseListFragment f = new CourseListFragment();

        f.setUserSession(session);

        return f;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {


//        final EntitySyncronizerPlugin plugin = new EntitySyncronizerPlugin(
//                new MoodleCourseSyncronizationManager());

        moodleCourses = (ArrayList<MoodleCourse>) MoodleCourseSyncronizationManager.getCourses(getActivity());

//        registerPlugin(plugin);
//
//        getLoaderManager().initLoader(Loaders.LoadCourses, null, this);
//
//        setListAdapter(mAdapter);

        super.onCreate(savedInstanceState);
    }

   /* @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem logOut = menu.add("Log Out");
//        logOut.setIcon(android.R.drawable.ic_lock_power_off);
        logOut.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        logOut.setOnMenuItemClickListener(new LogOutListener());
    }*/

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater
                .inflate(R.layout.fragment_course_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.course_list);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CourseAdapter(getActivity(),moodleCourses);
//        mAdapter = new MoodleCourseAdapter(getParentActivity(), null,
//                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    /*@Override
    public void onListItemClick(final ListView l, final View v,
                                final int position, final long id) {
        final MoodleCourse course = MoodleCourseCursorWrapper
                .getCourse((Cursor) mAdapter.getItem(position));

        final Bundle b = new Bundle();
        b.putParcelable(SharedConstants.ParcelKeys.MOODLE_COURSE,
                new MoodleCourseParcel(course));
        sendResponse(Responses.onCourseSelected, b);
    }*/

    /*@Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle arg1) {
        switch (id) {
            case Loaders.LoadCourses:
                return new CursorLoader(getParentActivity(),
                        MoodleCourseContract.CONTENT_URI,
                        new String[]{
                                MoodleCourseContract.Columns._ID,
                                MoodleCourseContract.Columns.COURSE_ID,
                                MoodleCourseContract.Columns.COURSE_NAME,
                                MoodleCourseContract.Columns.SHORT_NAME,
                                MoodleCourseContract.Columns.COURSE_MANAGERS
                        },
                        null, null, null
                );
            default:
                return null;
        }
    }*/

    /*@Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        switch (loader.getId()) {
            case Loaders.LoadCourses:
                mAdapter.iAdapter.swapCursor(cursor);
        }

    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        switch (loader.getId()) {
            case Loaders.LoadCourses:
                mAdapter.iAdapter.swapCursor(null);
        }

    }*/


    /* ================= Private classes ================= */
    /*private static class MoodleCourseCursorWrapper {
        public static Long getCourseId(final Cursor cursor) {
            return cursor.getLong(1);
        }

        public static String getCourseName(final Cursor cursor) {
            return cursor.getString(2);
        }

        public static MoodleCourse getCourse(final Cursor cursor) {
            return new MoodleCourse(
                    MoodleCourseCursorWrapper.getCourseId(cursor),
                    MoodleCourseCursorWrapper.getCourseName(cursor), MoodleCourseCursorWrapper.getShortName(cursor));
        }

        private static String getShortName(final Cursor cursor) {
            return cursor.getString(3);
        }

        public static List<CourseManager> getCourseManagers(final Cursor cursor) {
            final String serializedManagers = cursor.getString(4);

            return MoodleCourseProvider
                    .getDeserializedCourseManagers(serializedManagers);
        }

    }

    private static class MoodleCourseAdapter extends CursorAdapter {

        private Context mContext;
        class ViewHolder {
            TextView courseTitle;
            TextView courseManagers;
            ImageView letter;
        }

        public MoodleCourseAdapter(final Context context, final Cursor c,
                                   final int flags) {

            super(context, c, flags);

            mContext = context;
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
                if (first) {
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
            String cName = courseName.trim();
            viewHolder.courseTitle.setText(cName);
            Colors colors = new Colors(mContext);
            Character letter = cName.toUpperCase().charAt(0);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(Character.toString(letter).toUpperCase(), colors.getColor(letter));

            viewHolder.letter.setImageDrawable(drawable);
//
//            viewHolder.courseManagers.setText(managersBuilder.toString());
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

            viewHolder.letter = (ImageView) view.findViewById(R.id.letter_view);

//            viewHolder.courseManagers = (TextView) view
//                    .findViewById(R.id.textview_course_managers);

            view.setTag(viewHolder);

            return view;

        }
    }*/

    class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

        Context context;
        ArrayList<MoodleCourse> courses;

        public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

            public TextView courseTitle;
            ImageView letter;
            public ViewHolder(View v) {
                super(v);
                courseTitle = (TextView) v.findViewById(R.id.course_title);
                letter = (ImageView) itemView.findViewById(R.id.letter_view);
            }

            @Override
            public void onClick(View view) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(view);

                MoodleCourse tempCourse = courses.get(itemPosition);

                final Bundle b = new Bundle();
                b.putParcelable(SharedConstants.ParcelKeys.MOODLE_COURSE,
                        new MoodleCourseParcel(tempCourse));
                sendResponse(Responses.onCourseSelected, b);
            }
        }


        public CourseAdapter(Context c,ArrayList<MoodleCourse> mCourses) {
            courses = mCourses;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_course_list, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            MoodleCourse course = courses.get(position);

            String courseName = course.getName();
            String cName = courseName.trim();
            holder.courseTitle.setText(cName);
            Colors colors = new Colors(context);
            Character letter = cName.toUpperCase().charAt(0);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(Character.toString(letter).toUpperCase(), colors.getColor(letter));

            holder.letter.setImageDrawable(drawable);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return courses.size();
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

    /* =========================== Listener ========================= */
    private class LogOutListener implements MenuItem.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.log_out)
                    .setMessage(R.string.log_out_prompt)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            final SharedPreferences preferences =
                                    getApplicationContext().getSharedPreferences(
                                            LoginMainActivity.SAVED_LOGIN_PREFERENCES_NAME,
                                            Context.MODE_PRIVATE);

                            preferences.edit()
                                    .putString(LoginMainActivity.ENCRYPTION_KEY, "")
                                    .putString(LoginMainActivity.USERNAME_KEY, "")
                                    .putString(LoginMainActivity.PASSWORD_KEY, "")
                                    .commit();

                            final Intent intent = new Intent(getActivity(), LoginMainActivity.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                            getActivity().finish();
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
    }
}