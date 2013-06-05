/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Android;

import org.sourceforge.ah.android.utilities.Converters.PixelConverter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.mits.mobile.ourvle.Classes.SharedConstants.PhotosContract;

/**
 * @author Aston Hamilton
 * 
 */
public class PhonePhoto {
    private final Uri mPhotoFileUri;

    private Bitmap mPhotoBitmap;

    private final String mCurrentPhotoPath;

    /**
     * @param mContext
     * @param uri
     */
    public PhonePhoto(final Uri uri) {
	super();
	mPhotoFileUri = uri;
	mCurrentPhotoPath = uri.getPath();
    }

    public Bitmap getBitmap(final Context context) {
	if (mPhotoBitmap == null) {
	    // Get the dimensions of the View
	    final int targetW = PixelConverter.dpToPixels(
		    PhotosContract.THUMB_WIDTH_DP, context.getResources());
	    final int targetH = PixelConverter.dpToPixels(
		    PhotosContract.THUMB_HEIGHT_DP, context.getResources());

	    // Get the dimensions of the bitmap
	    final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    final int photoW = bmOptions.outWidth;
	    final int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    final int scaleFactor = Math
		    .min(photoW / targetW, photoH / targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    mPhotoBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,
		    bmOptions);
	}
	return mPhotoBitmap;
    }

    public Uri getFileUri() {
	return mPhotoFileUri;
    }
}
