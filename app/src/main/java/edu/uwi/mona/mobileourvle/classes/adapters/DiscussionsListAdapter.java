package edu.uwi.mona.mobileourvle.classes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.models.ForumDiscussion;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/22/15.
 */
public class DiscussionsListAdapter extends RecyclerView.Adapter<DiscussionsListAdapter.ViewHolder> {

    private List<ForumDiscussion> mDiscussions;
    private Context mContext;

    public DiscussionsListAdapter(List<ForumDiscussion> discussions, Context context)
    {
        mDiscussions = discussions;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_discussion, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ForumDiscussion discussion = mDiscussions.get(position);
        String title = discussion.getName().trim();
        String author = discussion.getFirstuserfullname().trim();

        holder.author.setText(author);
        holder.discussionTitle.setText(title);

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
        return mDiscussions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView discussionTitle;
        TextView author;
        public ViewHolder(View v) {
            super(v);
            discussionTitle = (TextView) v.findViewById(R.id.discussion_title);
            author = (TextView) v.findViewById(R.id.author);
        }

    }
}
