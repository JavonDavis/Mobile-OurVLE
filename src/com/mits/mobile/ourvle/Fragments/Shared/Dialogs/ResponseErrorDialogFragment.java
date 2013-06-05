package com.mits.mobile.ourvle.Fragments.Shared.Dialogs;

import org.sourceforge.ah.android.utilities.Widgets.Fragments.DialogFragmentBase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

public class ResponseErrorDialogFragment extends DialogFragmentBase {
    private String mErrorMessage;

    public interface Arguments {
	public static final String ERROR_MESSAGE = "dialog.args.errormessage";
    }

    public void setErrorMessage(final String errString) {
	getFragmentArguments()
		.putString(Arguments.ERROR_MESSAGE, mErrorMessage);

	mErrorMessage = errString;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
	mErrorMessage = getFragmentArguments().getString(
		Arguments.ERROR_MESSAGE);

	return new AlertDialog.Builder(getActivity())
		.setTitle("Error")
		.setMessage(mErrorMessage).create();
    }
}