package edu.uwi.mona.mobileourvle.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.ForumListAdapter;
import edu.uwi.mona.mobileourvle.classes.RecyclerItemClickListener;
import edu.uwi.mona.mobileourvle.classes.models.CourseForum;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.classes.tasks.ForumTask;

/**
 *
 * @author Javon Davis
 */
public class ForumListFragment extends Fragment {
    private static final String ARG_PARAM_COURSE_ID = "Course ID";
    private OnForumSelectedListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private int courseId;
    private String token;

    private List<CourseForum> forums;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForumListFragment.
     */
    public static ForumListFragment newInstance(int courseId) {
        ForumListFragment fragment = new ForumListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_COURSE_ID,courseId);
        fragment.setArguments(args);
        return fragment;
    }

    public ForumListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null)
        {
            setCourseId(getArguments().getInt(ARG_PARAM_COURSE_ID));
        }

        token = SiteInfo.listAll(SiteInfo.class).get(0).getToken();

        forums = getCourseId() == 0 ? CourseForum.listAll(CourseForum.class):CourseForum.find(CourseForum.class,"courseid = ?",getCourseId()+"");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum_list, container, false);

        //ImageView letter = (ImageView) view.findViewById(R.id.letter_view);
        //TextView title = (TextView) view.findViewById(R.id.forum_title);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.forum_list);
        TextView emptyView = (TextView) view.findViewById(R.id.emptyText);

        if(!forums.isEmpty()) {

            mRecyclerView.setHasFixedSize(true);


            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);


            mAdapter = new ForumListAdapter(forums, getActivity());

//        mAdapter = new MoodleCourseAdapter(getParentActivity(), null,
//                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int itemPosition = position;

                    mListener.onForumSelected(forums.get(itemPosition).getForumid());

                }
            }));
        }
        else
        {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        //new ForumLoaderTask(token).execute();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnForumSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnForumSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     */
    public interface OnForumSelectedListener {
        public void onForumSelected(int id);
    }

    private class ForumLoaderTask extends AsyncTask<String,Integer,Boolean>
    {
        ForumTask task;

        public ForumLoaderTask(String token)
        {
            task = new ForumTask(token);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            // Get course ids
            List<MoodleCourse> mCourses = MoodleCourse.listAll(MoodleCourse.class);
            ArrayList<String> courseIds = new ArrayList<String>();
            for (int i = 0; i < mCourses.size(); i++)
                courseIds.add(mCourses.get(i).getCourseid() + "");
            if (task.syncForums(courseIds)) {
                forums.clear();
                forums.addAll(getCourseId() == 0 ? CourseForum.listAll(CourseForum.class) : CourseForum.find(CourseForum.class, "courseid = ?", getCourseId() + ""));
                return true;
            }
            else
                return false;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//            mAdapter = new ForumListAdapter(forums,getActivity());
//            mRecyclerView.swapAdapter(mAdapter, false);

            mAdapter.notifyDataSetChanged();
        }
    }
}
