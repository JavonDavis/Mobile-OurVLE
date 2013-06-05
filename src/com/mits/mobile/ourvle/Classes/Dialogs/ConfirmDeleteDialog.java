/**
 * 
 */
package com.mits.mobile.ourvle.Classes.Dialogs;

import org.sourceforge.ah.android.utilities.Widgets.Fragments.DialogFragmentBase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * @author Aston Hamilton
 * 
 */
public class ConfirmDeleteDialog extends DialogFragmentBase {

    private String mDeleteMessage;
    private Context mContext;

    private Listener mListener;

    public static ConfirmDeleteDialog newInstance(
	    final String deleteMessage) {
	final ConfirmDeleteDialog f = new ConfirmDeleteDialog();

	f.setDeleteMessage(deleteMessage);
	return f;
    }

    public void setDeleteMessage(final String deleteMessage) {
	getFragmentArguments().putString(Arugments.DELETE_MESSAGE,
		deleteMessage);

	mDeleteMessage = deleteMessage;
    }

    @Override
    public void onAttach(final Activity activity) {
	mContext = activity;

	mListener = (Listener) activity;

	super.onAttach(activity);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	mDeleteMessage = getFragmentArguments().getString(
		Arugments.DELETE_MESSAGE);

	super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
	final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	builder
		.setMessage(mDeleteMessage)
		.setCancelable(false)
		.setPositiveButton("Yes",
			new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(final DialogInterface dialog,
				    final int id) {
				mListener.onPositiveClicked();
			    }
			})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(final DialogInterface dialog,
			    final int id) {
			mListener.onNegativeClicked();

		    }
		});
	final AlertDialog alert = builder.create();
	return alert;
    }

    public static interface Listener {
	void onPositiveClicked();

	void onNegativeClicked();
    }

    private static interface Arugments {
	String DELETE_MESSAGE = "delete..message";
    }
}
