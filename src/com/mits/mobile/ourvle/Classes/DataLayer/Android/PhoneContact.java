/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Android;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

import com.mits.mobile.ourvle.R;


/**
 * @author Aston Hamilton
 * 
 */
public class PhoneContact {
    private final Uri contactUri;
    private final Context appContext;

    private String mContactId;
    private String mContactName;
    private Bitmap mContactProfilePic;
    private ArrayList<String> mPhoneNumbers;
    private boolean mHasPhoneNumbers;

    /**
     * @param contactName
     * @param contctPhoneNumber
     * @param profilePicStream
     */
    public PhoneContact(final Context context, final Uri contactUri) {
	super();
	this.contactUri = contactUri;
	appContext = context;

	mContactId = null;
	mContactName = null;
	mContactProfilePic = null;
	mHasPhoneNumbers = false;
	mPhoneNumbers = null;
    }

    private void helper_initializeContactBasicDetials() {
	if (mContactName == null) {
	    final ContentResolver contentResolver = appContext
		    .getContentResolver();
	    Cursor c = null;
	    try {
		c = contentResolver
			.query(contactUri,
				new String[] {
					BaseColumns._ID,
					ContactsContract.Contacts.HAS_PHONE_NUMBER,
					ContactsContract.Contacts.DISPLAY_NAME
				}, null, null, null);

		if (c != null && c.moveToFirst()) {
		    final String id = c.getString(0);
		    final int numPhoneNumbers = Integer
			    .parseInt(c.getString(1));

		    mContactId = id;
		    mContactName = c.getString(2);
		    mHasPhoneNumbers = numPhoneNumbers > 0;
		}
	    } finally {
		if (c != null)
		    c.close();
	    }
	}
    }

    private void helper_InitialiseContactPhoneNumbers() {
	if (mPhoneNumbers == null) {
	    mPhoneNumbers = new ArrayList<String>();
	    helper_initializeContactBasicDetials();
	    Cursor pCur = null;
	    try {
		final ContentResolver contentResolver = appContext
			.getContentResolver();
		pCur = contentResolver
			.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] {
					ContactsContract.CommonDataKinds.Phone.NUMBER,
					ContactsContract.CommonDataKinds.Phone.TYPE
				},
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID
					+ " = ?",
				new String[] {
				mContactId
				}, null);

		while (pCur.moveToNext()) {
		    final String phoneNumber = pCur
			    .getString(0);
		    mPhoneNumbers.add(phoneNumber);
		}

		pCur.close();
	    } finally {
		if (pCur != null)
		    pCur.close();
	    }
	}
    }

    /**
     * @return the contact name
     */
    public String getName() {
	helper_initializeContactBasicDetials();
	return mContactName;
    }

    /**
     * @return the contact phone numbers
     */
    public ArrayList<String> getPhoneNumbers() {
	helper_InitialiseContactPhoneNumbers();
	return mPhoneNumbers;
    }

    /**
     * @return the number of contact phone numbers
     */
    public Integer getPhoneNumberCount() {
	helper_InitialiseContactPhoneNumbers();
	return mPhoneNumbers.size();
    }

    /**
     * @return the contact phone number
     */
    public String getPhoneNumber(final int index) {
	helper_InitialiseContactPhoneNumbers();
	return mPhoneNumbers.get(index);
    }

    /**
     * @return the contact phone number
     */
    public boolean hasPhoneNumbers() {
	helper_InitialiseContactPhoneNumbers();
	return mHasPhoneNumbers;
    }

    public Bitmap getProfileAvatarBitmap() {
	if (mContactProfilePic == null) {
	    helper_initializeContactBasicDetials();
	    if (mContactId != null) {
		final ContentResolver contentResolver = appContext
			.getContentResolver();
		final Uri photoUri = ContentUris.withAppendedId(
			ContactsContract.Contacts.CONTENT_URI,
			Long.parseLong(mContactId));

		final InputStream input = ContactsContract.Contacts
			.openContactPhotoInputStream(contentResolver,
				photoUri);

		if (input != null)
		    mContactProfilePic = BitmapFactory.decodeStream(input);
		else
		    mContactProfilePic = BitmapFactory.decodeResource(
			    appContext.getResources(),
			    R.drawable.ic_contact_picture);
	    }

	}
	return mContactProfilePic;
    }

    public Context getContext() {
	return appContext;
    }

    public Uri getUri() {
	return contactUri;
    }

}
