package edu.uwi.mona.mobileourvle.app.Classes.Util;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.PhotosContract;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.VideoContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;

public class MediaUtil {
    public static String JPEG_FILE_PREFIX = "jpg";
    public static String JPEG_FILE_EXT = ".jpg";
    public static String MP4_FILE_PREFIX = "mp4";
    public static String MP4_FILE_EXT = ".mp4";

    public static File createVideoFile() throws IOException {
	// Create an image file name
	final String timeStamp =
		new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
			.format(new Date());
	final String imageFileName = timeStamp
		+ "_";

	if (!VideoContract.ALBUM_DIR.exists())
	    VideoContract.ALBUM_DIR.mkdirs();

	final File image = File.createTempFile(
		imageFileName,
		MediaUtil.MP4_FILE_EXT,
		VideoContract.ALBUM_DIR);

	return image;
    }

    public static File createImageFile() throws IOException {
	// Create an image file name
	final String timeStamp =
		new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
			.format(new Date());
	final String imageFileName = timeStamp
		+ "_";

	if (!PhotosContract.ALBUM_DIR.exists())
	    PhotosContract.ALBUM_DIR.mkdirs();

	final File image = File.createTempFile(
		imageFileName,
		MediaUtil.JPEG_FILE_EXT,
		PhotosContract.ALBUM_DIR);

	return image;
    }

    public static File createCourseImageFile(final MoodleCourse course)
	    throws IOException {
	// Create an image file name
	final String timeStamp =
		new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
			.format(new Date());
	final String imageFileName = timeStamp
		+ "_";

	final File albumDirectory =
		new File(PhotosContract.ALBUM_DIR, course.getName().trim().replaceAll(":","-"));

	if (!albumDirectory.exists())
	    albumDirectory.mkdirs();


	final File image = File.createTempFile(
		imageFileName,
		MediaUtil.JPEG_FILE_EXT,
		albumDirectory);

	return image;
    }

    public static File createCourseVideoFile(final MoodleCourse course)
	    throws IOException {
	// Create an image file name
	final String timeStamp =
		new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
			.format(new Date());
	final String imageFileName = timeStamp
		+ "_";

    //@see COurseContentsActivity OnCourseModuleSelected method
	final File albumDirectory =
		new File(VideoContract.ALBUM_DIR, course.getName().replaceAll(":","-"));

	if (!albumDirectory.exists())
	    albumDirectory.mkdirs();

	final File image = File.createTempFile(
		imageFileName,
		MediaUtil.MP4_FILE_EXT,
		albumDirectory);

	return image;
    }
}
