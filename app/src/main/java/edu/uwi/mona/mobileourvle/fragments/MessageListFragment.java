package edu.uwi.mona.mobileourvle.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.uwi.mona.mobileourvle.R;
import edu.uwi.mona.mobileourvle.classes.adapters.MessageListAdapter;
import edu.uwi.mona.mobileourvle.classes.models.Message;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.classes.tasks.MessageTask;

/**
 * A fragment representing a list of Messages.
 * <p>
 * <p>
 * Activities containing this fragment MUST implement the {@link OnMessageSelectedListener}
 * interface.
 */
public class MessageListFragment extends ListFragment {

    private MessageListAdapter mAdapter;
    private List<ListMessage> messages = new ArrayList<>();
    private ProgressBar progressBar;
    private SiteInfo siteInfo;

    private OnMessageSelectedListener mListener;

    public static MessageListFragment newInstance() {
        MessageListFragment fragment = new MessageListFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessageListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new MessageListAdapter(getActivity(),messages);
        siteInfo = SiteInfo.listAll(SiteInfo.class).get(0);

        setupMessages();
        setListAdapter(mAdapter);
        //setEmptyText(getResources().getString(R.string.no_messages));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list,container,false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new MessageSyncerBg().execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMessageSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMessageSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            Message message = (Message) mAdapter.getItem(position);
            mListener.onMessageSelected(message);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnMessageSelectedListener {
        void onMessageSelected(Message message);
    }

    public void setupMessages()
    {
        List<Message> mMessages = Message.listAll(Message.class);
        messages.clear();


        // Sort messages with newest first in list
        Collections.sort(mMessages, new Comparator<Message>() {
            public int compare(Message m1, Message m2) {
                if (m1.getTimecreated() == m2.getTimecreated())
                    return 0;
                return m1.getTimecreated() < m2.getTimecreated() ? 1 : -1;
            }
        });

        List<Integer> userids = new ArrayList<Integer>();
        int currentuserid = siteInfo.getUserid();

        for (int i = 0; i < mMessages.size(); i++) {

            // Message sent by current user
            if (currentuserid != mMessages.get(i).getUseridto()
                    && !isInList(userids, mMessages.get(i).getUseridto())) {
                ListMessage mes = new ListMessage();
                mes.message = mMessages.get(i);
                mes.userid = mMessages.get(i).getUseridto();
                mes.userfullname = mMessages.get(i).getUsertofullname();
                messages.add(mes);
                userids.add(mMessages.get(i).getUseridto());
            }

            // Message received by current user
            else if (currentuserid != mMessages.get(i).getUseridfrom()
                    && !isInList(userids, mMessages.get(i).getUseridfrom())) {
                ListMessage mes = new ListMessage();
                mes.message = mMessages.get(i);
                mes.userid = mMessages.get(i).getUseridfrom();
                mes.userfullname = mMessages.get(i).getUserfromfullname();
                messages.add(mes);
                userids.add(mMessages.get(i).getUseridfrom());
            }
        }
        messages.addAll((messages != null) ? messages
                : new ArrayList<ListMessage>());
    }

    Boolean isInList(List<Integer> ids, int id) {
        for (int i = 0; i < ids.size(); i++)
            if (ids.get(i).intValue() == id)
                return true;
        return false;
    }

    private class MessageSyncerBg extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            getListView().setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            getListView().getEmptyView().setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // Sync from server and update
            MessageTask mst = new MessageTask(siteInfo.getToken());
            if (mst.syncMessages(siteInfo.getUserid()))
                return true;
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            getListView().setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            getListView().getEmptyView().setVisibility(View.VISIBLE);
            setupMessages();
            mAdapter.notifyDataSetChanged();
        }

    }

    public class ListMessage {
        /**
         * Moodle message
         */
        public Message message;
        /**
         * userid of the user, other the current login user, who is
         * participating in this message
         */
        public int userid;

        /**
         * Fullname of the user, other the current login user, who is
         * participating in this message
         */
        public String userfullname;
    }

}
