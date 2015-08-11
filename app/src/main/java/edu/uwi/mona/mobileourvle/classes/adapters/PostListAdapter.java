package edu.uwi.mona.mobileourvle.classes.adapters;

import android.content.Context;
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
    private Context mContext;

    public PostListAdapter(List<DiscussionPost> posts, Context context)
    {
        mPosts = posts;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_post, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DiscussionPost post = mPosts.get(position);
        String content = post.getMessage();
        String author = post.getUserfullname().trim();

        holder.message.setText(content.trim());
        holder.author.setText(author);
//        String course = forum.getCoursename();
//        holder.courseTitle.setText(course.trim());
//        Colors colors = new Colors(mContext);
//        Character letter = course.trim().trim().toUpperCase().charAt(0);
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(Character.toString(letter).toUpperCase(), colors.getColor(letter));
//
//        holder.letter.setImageDrawable(drawable);

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
