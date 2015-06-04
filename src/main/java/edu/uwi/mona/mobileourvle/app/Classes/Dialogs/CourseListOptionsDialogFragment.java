package edu.uwi.mona.mobileourvle.app.Classes.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import edu.uwi.mona.mobileourvle.app.R;

/**
 * Created by Javon on 7/28/2014.
 */
public class CourseListOptionsDialogFragment extends DialogFragment {

    public interface CourseOptionListener
    {
        public void OnOptionItemSelected(int loc);
    }

    private CourseOptionListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            mListener = (CourseOptionListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement CourseOptionListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select an Option");
        builder.setItems(R.array.course_options,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mListener.OnOptionItemSelected(which);
            }
        });
        return builder.create();
    }
}
