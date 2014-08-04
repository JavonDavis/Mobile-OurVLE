package edu.uwi.mona.mobileourvle.app.Classes.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import edu.uwi.mona.mobileourvle.app.R;

/**
 * Created by Javon on 8/4/2014.
 */
public class CourseMediaOptionsDialogFragment extends DialogFragment {

    private Long id;
    private Uri uri;
    private int identifier;

    public Long getMediaId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public interface MediaOptionListener
    {
        public void OnPhotoItemSelected(int loc, Long id, Uri uri);
        public void onVideoItemSelected(int loc, Long id,Uri uri);
    }

    private MediaOptionListener mListener;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            mListener = (MediaOptionListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement MediaOptionListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select an Option");
        builder.setItems(R.array.photo_options,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch(getIdentifier())
                {
                    case 0:
                        mListener.OnPhotoItemSelected(which, getMediaId(), getUri());
                        break;
                    case 1:
                        mListener.onVideoItemSelected(which, getMediaId(), getUri());
                        break;
                }

            }
        });
        return builder.create();
    }
}
