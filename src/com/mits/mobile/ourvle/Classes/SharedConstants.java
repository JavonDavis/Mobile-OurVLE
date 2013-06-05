/**
 * 
 */
package com.mits.mobile.ourvle.Classes;

import java.io.File;

import android.os.Environment;

/**
 * The Interface SharedConstants.
 * 
 * @author Aston Hamilton
 */
public interface SharedConstants {

    /**
     * The Interface ParcelKeys.
     */
    public static interface ParcelKeys {

	public final String MOODLE_COURSE = "com.mits.mobile.ourvle.course";
	public final String MOODLE_COURSE_NOTE = "com.mits.mobile.ourvle.course.note";
	public final String PARENT = "com.mits.mobile.ourvle.course.module.forum.discussion.parent";
	public final String FORUM_DISCUSSION_ID = "com.mits.mobile.ourvle.course.module.forum.discussion.id";
	public final String FORUM_DISCUSSION = "com.mits.mobile.ourvle.course.module.forum.discussion";
	public final String FORUM_DISCUSSION_LIST = "com.mits.mobile.ourvle.course.module.forum.discussion.list";
	public final String DISCUSSION_POST = "com.mits.mobile.ourvle.course.module.forum.discussion.post";

	/** The user session. */
	public final String USER_SESSION = "com.mits.mobile.ourvle.user.session";
	public final String MOODLE_USER = "com.mits.mobile.ourvle.moodle.user";
	public final String COURSE_FORUM_MODULE = "com.mits.mobile.ourvle.course.module";

	public final String USER_SESSION_KEY = "com.mits.mobile.ourvle.user.session.keey";
    }

    public static interface SharedContract {
	public final String SYNCRONIZATION_PERMISSION = "com.mits.mobile.ourvle.USE_LOCAL_ENTITY_SYNCRONIZER";
    }

    public interface PhotosContract {
	public int THUMB_HEIGHT_DP = 135; // dip
	public int THUMB_WIDTH_DP = 135; // dip
	public final String ALBUM_NAME = "Mobile Moodle/Photos/";
	public final File ALBUM_DIR =
		new File(
			Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES
				),
			PhotosContract.ALBUM_NAME
		);
    }

    public interface VideoContract {
	public int THUMB_HEIGHT_DP = 135; // dip
	public int THUMB_WIDTH_DP = 135; // dip
	public final String ALBUM_NAME = "Mobile Moodle/Video/";
	public final File ALBUM_DIR =
		new File(
			Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_MOVIES
				),
			VideoContract.ALBUM_NAME
		);
    }
}
