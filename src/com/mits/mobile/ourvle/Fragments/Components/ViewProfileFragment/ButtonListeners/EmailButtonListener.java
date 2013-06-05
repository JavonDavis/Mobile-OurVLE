package com.mits.mobile.ourvle.Fragments.Components.ViewProfileFragment.ButtonListeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.UserProfileField;

public class EmailButtonListener implements OnClickListener {
    private final UserProfileField field;
    private final Context mContext;

    /**
     * @param context
     * @param field
     */
    public EmailButtonListener(final Context context,
    	final UserProfileField field) {
        super();
        this.field = field;
        mContext = context;
    }

    @Override
    public void onClick(final View v) {
        final Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("plain/text");
        i.putExtra(Intent.EXTRA_EMAIL, new String[] { field.getValue() });

        try {
    	mContext.startActivity(i);
        } catch (final android.content.ActivityNotFoundException ex) {
    	Toast.makeText(mContext,
    		"There are no email clients installed.",
    		Toast.LENGTH_SHORT).show();
        }
    }

}