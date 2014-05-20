package edu.uwi.mona.mobileourvle.app.Fragments.Components.ViewProfileFragment.ButtonListeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.UserProfileField;

public class PhoneButtonListener implements OnClickListener {
    private final UserProfileField field;
    private final Context mContext;

    /**
     * @param context
     * @param field
     */
    public PhoneButtonListener(final Context context,
    	final UserProfileField field) {
        super();
        this.field = field;
        mContext = context;
    }

    @Override
    public void onClick(final View v) {
        final String url = "tel:"
    	    + field.getValue().replaceAll("[(),\\- .]", "");
        final Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        mContext.startActivity(intent);
    }
}