package com.mits.mobile.ourvle.Fragments.Shared;

import com.mits.mobile.ourvle.R;
import org.sourceforge.ah.android.utilities.Plugins.BaseClass.PluggableFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class UnderDevelopementFragment extends PluggableFragment {

    public static UnderDevelopementFragment newInstance() {
	final UnderDevelopementFragment f = new UnderDevelopementFragment();
	f.setArguments(new Bundle());

	return f;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
	    final ViewGroup container,
	    final Bundle savedInstanceState) {
	// Inflate the layout for this fragment
	final View fragmentView = inflater.inflate(
		R.layout.fragment_under_developement, container, false);

	return fragmentView;
    }
}
