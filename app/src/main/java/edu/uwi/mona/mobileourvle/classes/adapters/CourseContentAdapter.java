package edu.uwi.mona.mobileourvle.classes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.helpers.Colors;
import edu.uwi.mona.mobileourvle.classes.models.CourseModule;

/**
 * @author Javon
 * Created by Javon on 24/06/2015.
 */
public class CourseContentAdapter extends RecyclerView.Adapter<CourseContentAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CourseModule> mModules;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView icon;

        public ViewHolder(View v) {
            super(v);

            icon = (ImageView) v.findViewById(R.id.letter_view);
            title = (TextView) v.findViewById(R.id.textview_course_content);

        }
    }

    public CourseContentAdapter(Context context, ArrayList<CourseModule> modules) {
        mContext = context;
        mModules = modules;
    }

    @Override
    public CourseContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_course_contents, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String title = mModules.get(position).getName().trim();
        holder.title.setText(title);

        Colors colors = new Colors(mContext);
        Character letter = title.toUpperCase().charAt(0);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(Character.toString(letter).toUpperCase(), colors.getColor(letter));

        holder.icon.setImageDrawable(drawable);
    }


    @Override
    public int getItemCount() {
        return mModules.size();
    }
}
