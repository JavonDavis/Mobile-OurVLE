/**
 *
 */
package edu.uwi.mona.mobileourvle.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.activities.CourseContentsActivity;
import edu.uwi.mona.mobileourvle.classes.helpers.Colors;
import edu.uwi.mona.mobileourvle.classes.helpers.RecyclerItemClickListener;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.classes.tasks.CourseTask;

/**
 * @author Aston Hamilton
 * @author Javon Davis
 */
public class CourseListFragment extends Fragment{

    //private MoodleCourseAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MoodleCourse> moodleCourses;
    private ProgressBar progressBar;
    private TextView emptyView;
    private String token;

    public static CourseListFragment newInstance() {
        final CourseListFragment f = new CourseListFragment();

        return f;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moodleCourses = (ArrayList<MoodleCourse>) MoodleCourse.listAll(MoodleCourse.class);
        token = SiteInfo.listAll(SiteInfo.class).get(0).getToken();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_courses, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.synchronize:
                new CourseSyncTask().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater
                .inflate(R.layout.fragment_course_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.course_list);
        emptyView = (TextView) view.findViewById(R.id.emptyText);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        if(!moodleCourses.isEmpty()) {

            mRecyclerView.setHasFixedSize(true);


            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);


            mAdapter = new CourseAdapter(getActivity(), moodleCourses);

//        mAdapter = new MoodleCourseAdapter(getParentActivity(), null,
//                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int itemPosition = position;

                    MoodleCourse tempCourse = moodleCourses.get(itemPosition);

                    Intent intent = new Intent(getActivity(), CourseContentsActivity.class);
                    intent.putExtra("courseid", tempCourse.getCourseid());
                    Log.d("course selected", tempCourse.getShortname());
                    getActivity().startActivity(intent);

                }
            }));
        }
        else
        {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        return view;
    }

    class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

        Context context;
        ArrayList<MoodleCourse> courses;

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView courseTitle;
            ImageView letter;
            public ViewHolder(View v) {
                super(v);
                courseTitle = (TextView) v.findViewById(R.id.textview_course_title);
                letter = (ImageView) v.findViewById(R.id.letter_view);
            }

        }


        public CourseAdapter(Context c,ArrayList<MoodleCourse> mCourses) {
            context = c;
            courses = mCourses;
        }

        @Override
        public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_course_list, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            MoodleCourse course = courses.get(position);

            String courseName = course.getFullname().trim();
            holder.courseTitle.setText(courseName);

            Colors colors = new Colors(context);
            Character letter = courseName.toUpperCase().charAt(0);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(Character.toString(letter).toUpperCase(), colors.getColor(letter));

            holder.letter.setImageDrawable(drawable);

        }

        @Override
        public int getItemCount() {
            return courses.size();
        }
    }

    private class CourseSyncTask extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            moodleCourses.clear();
            emptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            getCourseInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            moodleCourses.addAll(MoodleCourse.listAll(MoodleCourse.class));
            mAdapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);
            if(mAdapter.getItemCount()==0)
            {
                mRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else
                mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private boolean getCourseInfo() {
        CourseTask cTask = new CourseTask(token);

        Boolean usrCourseSyncStatus = cTask.syncUserCourses();

        // Success on user's course sync is what matters
        return usrCourseSyncStatus;
    }

}