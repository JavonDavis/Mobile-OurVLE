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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.adapters.DiscussionsListAdapter;
import edu.uwi.mona.mobileourvle.classes.helpers.RecyclerItemClickListener;
import edu.uwi.mona.mobileourvle.classes.models.ForumDiscussion;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.classes.tasks.DiscussionTask;

/**
 *
 * @author Javon Davis
*/
public class ForumFragment extends Fragment {

    private OnDiscussionSelectedListener mListener;
    private List<ForumDiscussion> mDiscussions;
    private int id;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private TextView emptyView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Integer> ids = new ArrayList<>();

    private static final String ARG_PARAM_ID = "id";
    private String token;


    public static ForumFragment newInstance(int mId) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_ID,mId);
        fragment.setArguments(args);
        return fragment;
    }

    public ForumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!=null)
        {
            setForumId(getArguments().getInt(ARG_PARAM_ID));
        }

        token = SiteInfo.listAll(SiteInfo.class).get(0).getToken();

        mDiscussions = ForumDiscussion.find(ForumDiscussion.class,
                "forumid = ?", getForumId()+"");

        for(ForumDiscussion discussion: mDiscussions)
        {
            ids.add(discussion.getDiscussionid());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.discussion_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        emptyView = (TextView) view.findViewById(R.id.emptyText);

        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new DiscussionsListAdapter(mDiscussions);

//        mAdapter = new MoodleCourseAdapter(getParentActivity(), null,
//                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int itemPosition = position;

                mListener.onDiscussionSelected(mDiscussions.get(itemPosition).getDiscussionid());

            }
        }));

        new AsyncDiscussionTask(token).execute();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDiscussionSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDiscussionSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public int getForumId() {
        return id;
    }

    public void setForumId(int id) {
        this.id = id;
    }

    public interface OnDiscussionSelectedListener {
        void onDiscussionSelected(int discussionId);
    }

    private class AsyncDiscussionTask extends AsyncTask<String, Integer, Boolean> {
        DiscussionTask dst;

        public AsyncDiscussionTask(String token) {
            dst = new DiscussionTask(token);
        }

        @Override
        protected void onPreExecute() {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (dst.syncDiscussions(getForumId())) {
                mDiscussions.clear();
                mDiscussions.addAll(ForumDiscussion
                        .find(ForumDiscussion.class,
                                "forumid = ?", getForumId() + ""));

            return true;
        }
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
