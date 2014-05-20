package edu.uwi.mona.mobileourvle.app.Fragments.Shared.Dialogs;

import org.sourceforge.ah.android.utilities.Widgets.Fragments.DialogFragmentBase;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

/* ====================== Dialogs ======================= */
public class ProgressDialogFragment extends DialogFragmentBase {
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
	final ProgressDialog d = new ProgressDialog(getActivity());
	d.setTitle("Loading");
	d.setMessage("Please wait.....");

	return d;
    }
}