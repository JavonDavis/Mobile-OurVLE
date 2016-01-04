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
import edu.uwi.mona.mobileourvle.classes.models.CourseParticipant;
import edu.uwi.mona.mobileourvle.fragments.ParticipantsListFragment;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 8/3/15.
 */
public class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.ViewHolder> {

    private List<CourseParticipant> mParticipants;
    private Context mContext;

    public ParticipantListAdapter(List<CourseParticipant> mParticipants, Context context)
    {
        this.mParticipants = mParticipants;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_participant, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CourseParticipant participant = mParticipants.get(position);

        String name = participant.getFullname().trim();
        holder.name.setText(name);

        Colors colors = new Colors(mContext);
        Character letter = name.trim().toUpperCase().charAt(0);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(Character.toString(letter).toUpperCase(), colors.getColor(letter));

        holder.letterView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return mParticipants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView letterView;
        TextView name;
        public ViewHolder(View v) {
            super(v);
            letterView = (ImageView) v.findViewById(R.id.letter_view);
            name = (TextView) v.findViewById(R.id.textview_participant);
        }

    }
}
