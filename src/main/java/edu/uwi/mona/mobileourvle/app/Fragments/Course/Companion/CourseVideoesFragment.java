/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Course.Companion;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Plugins.BaseClass.PluggableFragment;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.DynamicViewAdapter.SimpleViewHolder;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.uwi.mona.mobileourvle.app.Classes.Dialogs.CourseMediaOptionsDialogFragment;
import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.CompanionEntities.CourseVideo;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.CourseClassTimeContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviderContracts.CourseVideoesContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;
import edu.uwi.mona.mobileourvle.app.Classes.Util.MediaUtil;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseVideoesFragment extends PluggableFragment implements
	LoaderCallbacks<Cursor> {
    private MoodleCourse mCourse;
    private CourseVideoCursorAdatper mAdapter;

    private GridView mGridView;

    private File tVideoFile;
    private CourseVideo tVideo;

    private TextView mEmptyTextView;

    public static CourseVideoesFragment newInstance(
	    final MoodleCourse course) {
	final CourseVideoesFragment f = new CourseVideoesFragment();

	f.setMoodleCourse(course);
	return f;
    }

    public void setMoodleCourse(final MoodleCourse course) {
	getFragmentArguments().putParcelable(ParcelKeys.MOODLE_COURSE,
		new MoodleCourseParcel(course));
	mCourse = course;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	mCourse = ((MoodleCourseParcel) getFragmentArguments()
		.getParcelable(ParcelKeys.MOODLE_COURSE))
		.getWrappedObejct();

	mAdapter = new CourseVideoCursorAdatper(mCourse, getParentActivity(),
		null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
	    @Override
	    public void notifyDataSetChanged() {

		if (mEmptyTextView != null)
		    if (mAdapter.getCount() == 0)
			mEmptyTextView.setVisibility(View.VISIBLE);
		    else
			mEmptyTextView.setVisibility(View.INVISIBLE);

		super.notifyDataSetChanged();
	    }
	};

	getLoaderManager().initLoader(Loaders.LoadCourseVideoes, null, this);

	setHasOptionsMenu(true);
	super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {

	inflater.inflate(R.menu.course_videos_fragment_menu, menu);

	super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
	    final ViewGroup container,
	    final Bundle savedInstanceState) {
	final View v = inflater.inflate(R.layout.fragment_course_videoes_list,
		container, false);

	mGridView = (GridView) v.findViewById(android.R.id.list);
	mEmptyTextView = (TextView) v.findViewById(android.R.id.empty);
	mGridView.setAdapter(mAdapter);

	return v;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
	switch (item.getItemId()) {
	case R.id.menu_add_video:
	    startVideoCaptureIntent();
	    break;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
	    final Intent data) {
	switch (resultCode) {
	case Activities.TAKE_PICTURE:
	    handleVideoTaken(resultCode);
	    break;
	}

	super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
	super.onResume();

	if (tVideo != null && tVideoFile != null)
	    handleVideoTaken(Activity.RESULT_OK);

	if (mAdapter != null)
	    mAdapter.notifyDataSetChanged();
    }

    public void refresh()
    {
        getLoaderManager().restartLoader(Loaders.LoadCourseVideoes,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int arg0, final Bundle arg1) {
	return new CursorLoader(getParentActivity(),
		CourseVideoesContract.CONTENT_URI,
		new String[] {
			CourseVideoesContract.COURSE_ID,
			CourseVideoesContract._ID,
			CourseVideoesContract.VIDEO_FILE_PATH,
			CourseVideoesContract.NOTES,
			CourseVideoesContract.TIMESTAMP
		},
		CourseClassTimeContract.COURSE_ID + " = ?",
		new String[] { mCourse.getId().toString() },
		null);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> arg0, final Cursor arg1) {
	mAdapter.swapCursor(arg1);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> arg0) {
	mAdapter.swapCursor(null);
    }

    private void startVideoCaptureIntent() {
	final Intent takeVideoIntent = new Intent(
		MediaStore.ACTION_VIDEO_CAPTURE);

	try {
	    tVideoFile = MediaUtil.createCourseVideoFile(mCourse);
	} catch (final IOException e) {
	    Toast.makeText(getApplicationContext(),
		    "Cannot add new video.\n" + e.toString(),
		    Toast.LENGTH_LONG)
		    .show();
	    return;
	}

	/*
	 * Sometimes the activity is killed when taking a pic so
	 * onActivityResult is not called.
	 * 
	 * To fix this, I assume that the pic was added and then when loading
	 * the pics I take this assumption into account.
	 */
	tVideo = new CourseVideo(tVideoFile, new DateTime(), mCourse, "");

	final ContentResolver cr = getApplicationContext().getContentResolver();
	final ContentValues values = new ContentValues();
	values.put(CourseVideoesContract.COURSE_ID,
		mCourse.getId().toString());
	values.put(CourseVideoesContract.VIDEO_FILE_PATH,
		tVideo.getFileUri().toString());
	values.put(CourseVideoesContract.TIMESTAMP,
		tVideo.getDateTaken().getMillis() / 1000);
	values.put(CourseVideoesContract.NOTES,
		tVideo.getNotes());

	new AsyncQueryHandler(cr) {

	}.startInsert(0, null, CourseVideoesContract.CONTENT_URI, values);

	takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
		Uri.fromFile(tVideoFile));

	startActivityForResult(takeVideoIntent, Activities.TAKE_PICTURE);
    }

    /* ====================== Private Classes =================== */
    private static class CourseVideoCursorWrapper {
	private final Cursor mCoursor;
	private final MoodleCourse mCourse;

	private Long cId;
	private Long cCourseId;
	private String cVideoFilePath;
	private String cNotes;
	private Long cTimestamp;

	private static final int COURSE_ID = 0;
	private static final int VIDEO_ID = 1;
	private static final int VIDEO_FILE_PATH = 2;
	private static final int NOTES = 3;
	private static final int TIMESTAMP = 4;

	/**
	 * @param cursor
	 * @param course
	 */
	public CourseVideoCursorWrapper(final Cursor cursor,
		final MoodleCourse course) {
	    super();
	    mCoursor = cursor;
	    mCourse = course;
	}

	/**
	 * @return the Course Id
	 */
	@SuppressWarnings("unused")
	public Long getCourseId() {
	    if (cCourseId == null)
		cCourseId = mCoursor
			.getLong(CourseVideoCursorWrapper.COURSE_ID);
	    return cCourseId;
	}

	/**
	 * @return the Id
	 */
	public Long getId() {
	    if (cId == null)
		cId = mCoursor.getLong(CourseVideoCursorWrapper.VIDEO_ID);
	    return cId;
	}

	/**
	 * @return the Video File Path
	 */
	public String getVideoFilePath() {
	    if (cVideoFilePath == null)
		cVideoFilePath = mCoursor
			.getString(CourseVideoCursorWrapper.VIDEO_FILE_PATH);
	    return cVideoFilePath;
	}

	/**
	 * @return the Notes
	 */
	public String getNotes() {
	    if (cNotes == null)
		cNotes = mCoursor.getString(CourseVideoCursorWrapper.NOTES);
	    return cNotes;
	}

	/**
	 * @return the Timestamp
	 */
	public Long getTimestamp() {
	    if (cTimestamp == null)
		//cTimestamp = mCoursor
		//	.getLong(CourseVideoCursorWrapper.TIMESTAMP);
            cTimestamp=new File(Uri.parse(getVideoFilePath()).getPath()).lastModified();
	    return cTimestamp;
	}

	public CourseVideo getCourseVideo() {
	    return new CourseVideo(getVideoFilePath(), new DateTime(
		    getTimestamp()),
		    mCourse, getNotes());
	}

    }

    private static class CourseVideoCursorAdatper
	    extends CursorAdapter {

	private final WeakReference<Context> mContext;

	private final MoodleCourse mCourse;

	public CourseVideoCursorAdatper(final MoodleCourse course,
		final Context context, final Cursor c,
		final int flags) {
	    super(context, c, flags);

	    mContext = new WeakReference<Context>(context);
	    mCourse = course;
	}

	class ViewHolder implements SimpleViewHolder {
	    ImageView video;
	    TextView date;
	    TextView notes;
	}

	@Override
	public void bindView(final View v, final Context context,
		final Cursor c) {
	    final ViewHolder h = (ViewHolder) v.getTag();

	    final CourseVideoCursorWrapper cWrapper = new CourseVideoCursorWrapper(
		    c,
		    mCourse);

	    final CourseVideo data = cWrapper.getCourseVideo();

	    final File videoFile = new File(data.getFileUri().getPath());

	    if (mContext.get() == null) {
		Log.e("Course Video Adapter", "Context expired.");
		return;
	    }
	    if (!videoFile.exists()) {

		h.date.setText("Video Error");
		h.notes.setText("");
		final ContentResolver cr = ((ContextWrapper) mContext.get())
			.getContentResolver();
		new AsyncQueryHandler(cr) {

		}.startDelete(0, null, CourseVideoesContract.CONTENT_URI,
			CourseVideoesContract.VIDEO_FILE_PATH + "=?",
			new String[] { data.getFileUri().toString() });

		return;
	    }

	    h.date.setText(DateFormatter.getShortDateTime(data.getDateTaken()));
	    h.notes.setText(data.getNotes());

	    h.video.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(final View v) {

		    if (mContext.get() == null) {
			Log.e("Course Video Adapter", "Context expired.");
			return;
		    }

		    // Launch default viewer for the file
		    final Uri uri = data.getFileUri();

            CourseMediaOptionsDialogFragment dialog = new CourseMediaOptionsDialogFragment();
            android.app.FragmentManager fragmentManager = ((Activity) mContext.get()).getFragmentManager();

            dialog.setId(cWrapper.getId());
            dialog.setUri(uri);
            dialog.setIdentifier(1);

            dialog.show(fragmentManager,"dialog");

		}
	    });

	    new AsyncTask<String, Void, Bitmap>() {

		@Override
		protected Bitmap doInBackground(final String... params) {
		    final Bitmap bMap = ThumbnailUtils.createVideoThumbnail(
			    params[0],
			    MediaStore.Video.Thumbnails.MICRO_KIND);

		    return bMap;
		}

		@Override
		protected void onPostExecute(final Bitmap result) {
		    h.video.setImageBitmap(result);
		};

	    }.execute(data.getFileUri().getPath());

	}

	@Override
	public View newView(final Context context, final Cursor c,
		final ViewGroup root) {

	    final LayoutInflater inflater = (LayoutInflater) context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    final View v = inflater.inflate(R.layout.grid_item_course_video,
		    root,
		    false);

	    final ViewHolder h = new ViewHolder();
	    h.video = (ImageView) v.findViewById(R.id.imageview_video);
	    h.date = (TextView) v.findViewById(R.id.textview_date);
	    h.notes = (TextView) v.findViewById(R.id.textview_notes);

	    v.setTag(h);

	    return v;
	}

    }

    /* =============================== Helper functions ====================== */

    private void handleVideoTaken(final int resultCode) {
	if (resultCode != Activity.RESULT_OK) {
	    tVideoFile.delete();

	    final ContentResolver cr = getApplicationContext()
		    .getContentResolver();

	    new AsyncQueryHandler(cr) {

	    }.startDelete(0, null, CourseVideoesContract.CONTENT_URI,
		    CourseVideoesContract.VIDEO_FILE_PATH + "=?",
		    new String[] { tVideo.getFileUri().toString() });

	    tVideo = null;
	    tVideoFile = null;
	    return;
	}

	tVideo = null;
	tVideoFile = null;
    }

    /* =============================== Interfaces ============================ */
    private static interface Activities {
	int TAKE_PICTURE = 0;
    }

    private static interface Loaders {
	int LoadCourseVideoes = 0;
    }
}
