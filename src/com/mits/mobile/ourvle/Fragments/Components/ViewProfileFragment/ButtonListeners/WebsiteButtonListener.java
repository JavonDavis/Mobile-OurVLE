package com.mits.mobile.ourvle.Fragments.Components.ViewProfileFragment.ButtonListeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.UserProfileField;

public class WebsiteButtonListener implements OnClickListener {
    private final UserProfileField field;
    private final Context mContext;

    /**
     * @param context
     * @param field
     */
    public WebsiteButtonListener(final Context context,
	    final UserProfileField field) {
	super();
	this.field = field;
	mContext = context;
    }

    @Override
    public void onClick(final View v) {
	final Intent browserIntent = new Intent(Intent.ACTION_VIEW,
		Uri.parse(field.getValue()));
	mContext.startActivity(browserIntent);
    }

}