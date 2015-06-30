package edu.uwi.mona.mobileourvle.app.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.uwi.mona.mobileourvle.app.Classes.HomeOptionsAdapter;
import edu.uwi.mona.mobileourvle.app.Classes.options.OptionContent;

/**
 * A fragment representing a list of options for the home screen.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnOptionSelectedListener}
 * interface.
 */
public class OptionListFragment extends ListFragment {

    private OnOptionSelectedListener mListener;

    public static OptionListFragment newInstance() {
        OptionListFragment fragment = new OptionListFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OptionListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new HomeOptionsAdapter(getActivity()));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnOptionSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnOptionSelectedListener");
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
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onOptionSelected(OptionContent.ITEMS.get(position).id);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getListView();
//        listView.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
//        listView.setDivider(new ColorDrawable(Color.GRAY));
        listView.setDividerHeight(3);
    }

    public interface OnOptionSelectedListener {
        void onOptionSelected(String id);
    }

}
