/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes;

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

	public final String MOODLE_COURSE = "edu.uwi.mona.mobileourvle.app.course";
	public final String MOODLE_COURSE_NOTE = "edu.uwi.mona.mobileourvle.app.course.note";
	public final String PARENT = "edu.uwi.mona.mobileourvle.app.course.module.forum.discussion.parent";
	public final String FORUM_DISCUSSION_ID = "edu.uwi.mona.mobileourvle.app.course.module.forum.discussion.id";
	public final String FORUM_DISCUSSION = "edu.uwi.mona.mobileourvle.app.course.module.forum.discussion";
	public final String FORUM_DISCUSSION_LIST = "edu.uwi.mona.mobileourvle.app.course.module.forum.discussion.list";
	public final String DISCUSSION_POST = "edu.uwi.mona.mobileourvle.app.course.module.forum.discussion.post";

	/** The user session. */
	public final String USER_SESSION = "edu.uwi.mona.mobileourvle.app.user.session";
	public final String MOODLE_USER = "edu.uwi.mona.mobileourvle.app.moodle.user";
	public final String COURSE_FORUM_MODULE = "edu.uwi.mona.mobileourvle.app.course.module";

	public final String USER_SESSION_KEY = "edu.uwi.mona.mobileourvle.app.user.session.keey";
    }

    public static interface SharedContract {
	public final String SYNCRONIZATION_PERMISSION = "edu.uwi.mona.mobileourvle.app.USE_LOCAL_ENTITY_SYNCRONIZER";
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
