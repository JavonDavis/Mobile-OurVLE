package edu.uwi.mona.mobileourvle.fragments;

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
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.adapters.PostListAdapter;
import edu.uwi.mona.mobileourvle.classes.helpers.RecyclerItemClickListener;
import edu.uwi.mona.mobileourvle.classes.models.DiscussionPost;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.classes.tasks.PostTask;

/**
 * @author Javon Davis
 */
public class PostListFragment extends Fragment {
    private static final String ARG_PARAM1 = "discussionId";

    private int discussionId;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ProgressBar progressBar;
    private TextView emptyView;

    private String token;

    List<DiscussionPost> mPosts;

    /**
     *
     * @return A new instance of fragment PostListFragment.
     */
    public static PostListFragment newInstance(int id) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    public PostListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setDiscussionId(getArguments().getInt(ARG_PARAM1));
        }

        token = SiteInfo.listAll(SiteInfo.class).get(0).getToken();

        mPosts = DiscussionPost.find(DiscussionPost.class, "discussionid=?", getDiscussionId() + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.discussion_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        emptyView = (TextView) view.findViewById(R.id.emptyText);

        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new PostListAdapter(mPosts);

//        mAdapter = new MoodleCourseAdapter(getParentActivity(), null,
//                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int itemPosition = position;

                Toast.makeText(getActivity(), "Click " + position, Toast.LENGTH_LONG).show();

            }
        }));

        new AsyncPostsTask(token).execute();
        return view;
    }

    /**
     * This will sort the posts by the time they were posted. This is requried
     * after every update to posts list or else the order may be lost.
     */
    private void sortPostsByTime() {

        Collections.sort(mPosts, new Comparator<DiscussionPost>() {
            public int compare(DiscussionPost o1, DiscussionPost o2) {
                if (o1.getCreated() == o2.getCreated())
                    return 0;
                return o1.getCreated() < o2.getCreated() ? -1 : 1;
            }
        });
    }

    private class AsyncPostsTask extends AsyncTask<String, Integer, Boolean> {
        PostTask pst;
        Boolean syncStatus;

        public AsyncPostsTask(String token) {
            pst = new PostTask(token);
        }

        @Override
        protected void onPreExecute() {
            mRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            syncStatus = pst.syncPosts(getDiscussionId());

            if (syncStatus) {
                mPosts.clear();
                mPosts.addAll(DiscussionPost.find(DiscussionPost.class,
                        "discussionid = ?", getDiscussionId() + ""));
                sortPostsByTime();
                return true;
            } else
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
            else
            {
                emptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            //mAdapter = new PostListAdapter(mPosts,getActivity());
            //mRecyclerView.swapAdapter(mAdapter,false);
        }

    }

    public int getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(int discussionId) {
        this.discussionId = discussionId;
    }
}
