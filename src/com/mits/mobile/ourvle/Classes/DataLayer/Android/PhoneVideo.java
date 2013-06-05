/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Android;

import android.net.Uri;

/**
 * @author Aston Hamilton
 * 
 */
public class PhoneVideo {
    private final Uri mUri;

    /**
     * @param mContext
     * @param uri
     */
    public PhoneVideo(final Uri uri) {
	super();
	mUri = uri;
    }

    public Uri getFileUri() {
	return mUri;
    }
}
