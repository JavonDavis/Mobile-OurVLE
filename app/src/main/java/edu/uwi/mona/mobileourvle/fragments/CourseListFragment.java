/**
 *
 */
package edu.uwi.mona.mobileourvle.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.activities.CourseContentsActivity;
import edu.uwi.mona.mobileourvle.classes.Colors;
import edu.uwi.mona.mobileourvle.classes.RecyclerItemClickListener;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;

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

    public static CourseListFragment newInstance() {
        final CourseListFragment f = new CourseListFragment();

        return f;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {

        moodleCourses = (ArrayList<MoodleCourse>) MoodleCourse.listAll(MoodleCourse.class);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater
                .inflate(R.layout.fragment_course_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.course_list);

        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new CourseAdapter(getActivity(),moodleCourses);

//        mAdapter = new MoodleCourseAdapter(getParentActivity(), null,
//                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int itemPosition = position;

                MoodleCourse tempCourse = moodleCourses.get(itemPosition);

                Intent intent = new Intent(getActivity(), CourseContentsActivity.class);
                intent.putExtra("courseid",tempCourse.getCourseid());
                Log.d("course selected",tempCourse.getShortname());
                getActivity().startActivity(intent);

            }
        }));

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

}