package edu.uwi.mona.mobileourvle.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.adapters.ParticipantListAdapter;
import edu.uwi.mona.mobileourvle.classes.helpers.RecyclerItemClickListener;
import edu.uwi.mona.mobileourvle.classes.models.CourseParticipant;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.classes.tasks.ParticipantTask;

/**
 * @author Javon
 */
public class ParticipantsListFragment extends Fragment {
    private static final String ARG_PARAM1 = "courseid";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ProgressBar progressBar;
    private String token;
    private TextView emptyView;
    private List<CourseParticipant> mParticipants;

    private int mCourseId;

    private OnParticipantSelectedListener mListener;

    /**
     *
     * @param courseId Parameter 1.
     * @return A new instance of fragment ParticipantsListFragment associated with the courseid.
     */
    public static ParticipantsListFragment newInstance(int courseId) {
        ParticipantsListFragment fragment = new ParticipantsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, courseId);
        fragment.setArguments(args);
        return fragment;
    }

    public ParticipantsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourseId= getArguments().getInt(ARG_PARAM1);
        }

        mParticipants = CourseParticipant.find(CourseParticipant.class,"courseid=?",mCourseId+"");
        token = SiteInfo.listAll(SiteInfo.class).get(0).getToken();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_participants_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.synchronize:
                new ParticipantSyncTask().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participants_list, container, false);

        emptyView = (TextView) view.findViewById(R.id.emptyText);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.participants_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        mRecyclerView.setHasFixedSize(true);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new ParticipantListAdapter(mParticipants,getActivity());

//        mAdapter = new MoodleCourseAdapter(getParentActivity(), null,
//                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //int itemPosition = position;

                CourseParticipant participant = mParticipants.get(position);
                mListener.onParticipantSelected(participant);


            }
        }));
        if(mParticipants.isEmpty())
            new ParticipantSyncTask().execute();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnParticipantSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnParticipantSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnParticipantSelectedListener {
        void onParticipantSelected(CourseParticipant participant);
    }

    private class ParticipantSyncTask extends
            AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            emptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ParticipantTask ust = new ParticipantTask(
                    token);
            if (ust.syncUsers(mCourseId)) {
                mParticipants.clear();
                mParticipants.addAll(CourseParticipant.find(CourseParticipant.class,"courseid=?",mCourseId+""));
                return true;
            }
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            if(mAdapter.getItemCount()==0)
            {
                mRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        }

    }

}
