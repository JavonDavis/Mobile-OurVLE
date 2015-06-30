package edu.uwi.mona.mobileourvle.app.Classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;
import edu.uwi.mona.mobileourvle.app.R;

/**
 * Created by javon_000 on 24/06/2015.
 */
public class CourseContentAdapter extends RecyclerView.Adapter<CourseContentAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CourseModule> mModules;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView title;
        private ImageView icon;

        public ViewHolder(View v) {
            super(v);

            icon = (ImageView) v.findViewById(R.id.icon_view);
            title = (TextView) v.findViewById(R.id.textview_course_content);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CourseContentAdapter(Context context,ArrayList<CourseModule> modules) {
        mContext = context;
        mModules = modules;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CourseContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_course_contents, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(mModules.get(position).getLabel());
        holder.icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.course_icon));
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mModules.size();
    }
}
