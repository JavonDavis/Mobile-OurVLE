package edu.uwi.mona.mobileourvle.classes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.helpers.Colors;
import edu.uwi.mona.mobileourvle.classes.models.CourseForum;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/9/15.
 */
public class ForumListAdapter extends RecyclerView.Adapter<ForumListAdapter.ViewHolder> {

    private List<CourseForum> mForums;
    private Context mContext;

    public ForumListAdapter(List<CourseForum> forums, Context context)
    {
        mForums = forums;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_forum, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CourseForum forum = mForums.get(position);
        holder.forumTitle.setText(forum.getName().trim());
        String course = forum.getCoursename();
        holder.courseTitle.setText(course.trim());
        holder.description.setText(forum.getIntro().trim());

        Colors colors = new Colors(mContext);
        Character letter = course.trim().trim().toUpperCase().charAt(0);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(Character.toString(letter).toUpperCase(), colors.getColor(letter));

        holder.letter.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return mForums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView courseTitle;
        TextView forumTitle;
        ImageView letter;
        TextView description;
        public ViewHolder(View v) {
            super(v);
            courseTitle = (TextView) v.findViewById(R.id.course_title);
            forumTitle = (TextView) v.findViewById(R.id.forum_title);
            letter = (ImageView) v.findViewById(R.id.letter_view);
            description = (TextView) v.findViewById(R.id.forum_description);
        }

    }
}
