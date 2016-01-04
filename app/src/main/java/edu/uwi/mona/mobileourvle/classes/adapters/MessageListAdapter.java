package edu.uwi.mona.mobileourvle.classes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.models.Message;
import edu.uwi.mona.mobileourvle.fragments.MessageListFragment;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 8/4/15.
 */
public class MessageListAdapter extends BaseAdapter {

    private List<MessageListFragment.ListMessage> messages;
    private Context context;

    public MessageListAdapter(Context context,List<MessageListFragment.ListMessage> messages)
    {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.message_list_item, parent, false);

        MessageListFragment.ListMessage message = (MessageListFragment.ListMessage) getItem(position);

        String from = message.message.getUserfromfullname();
        String messageShort = message.message.getSmallmessage();

        TextView fromView = (TextView) view.findViewById(R.id.from);
        TextView shortView = (TextView) view.findViewById(R.id.message_short);

        fromView.setText(from);
        shortView.setText(messageShort);

        return view;
    }
}
