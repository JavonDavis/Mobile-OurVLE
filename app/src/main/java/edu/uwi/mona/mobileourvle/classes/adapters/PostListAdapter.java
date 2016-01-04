package edu.uwi.mona.mobileourvle.classes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.models.DiscussionPost;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/22/15.
 */
public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder>{

    private List<DiscussionPost> mPosts;

    public PostListAdapter(List<DiscussionPost> posts)
    {
        mPosts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DiscussionPost post = mPosts.get(position);
        String content = post.getMessage();
        String author = post.getUserfullname().trim();

        holder.message.setText(content.trim());
        holder.author.setText(author);

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView message;
        TextView author;
        public ViewHolder(View v) {
            super(v);
            message= (TextView) v.findViewById(R.id.post);
            author = (TextView) v.findViewById(R.id.author);
        }

    }
}
