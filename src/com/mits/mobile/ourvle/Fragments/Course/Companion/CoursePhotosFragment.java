/**
 * 
 */
package com.mits.mobile.ourvle.Fragments.Course.Companion;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.AndroidUtil.AsyncManager;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Plugins.BaseClass.PluggableFragment;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.DynamicViewAdapter.SimpleViewHolder;
import org.sourceforge.ah.android.utilities.Widgets.Util.RemoteImageViewLoader;
import org.sourceforge.ah.android.utilities.Widgets.Util.RemoteImageViewLoader.AsyncImageViewLoaderTask;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
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
import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.R.id;
import com.mits.mobile.ourvle.Classes.SharedConstants.ParcelKeys;
import com.mits.mobile.ourvle.Classes.SharedConstants.PhotosContract;
import com.mits.mobile.ourvle.Classes.DataLayer.CompanionEntities.CoursePhoto;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.CourseClassTimeContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviderContracts.CoursePhotosContract;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.ParcableWrappers.MoodleCourseParcel;
import com.mits.mobile.ourvle.Classes.Util.MediaUtil;

/**
 * @author Aston Hamilton
 * 
 */
public class CoursePhotosFragment extends PluggableFragment implements
	LoaderCallbacks<Cursor> {
    private MoodleCourse mCourse;
    private CoursePhotoCursorAdatper mAdapter;

    private GridView mGridView;

    private File tPhotoFile;
    private CoursePhoto tPhoto;

    private TextView mEmptyTextView;

    private final String sComponentUri = getClass().getName();

    public static CoursePhotosFragment newInstance(
	    final MoodleCourse course) {
	final CoursePhotosFragment f = new CoursePhotosFragment();

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

	mAdapter = new CoursePhotoCursorAdatper(sComponentUri, mCourse,
		getParentActivity(), null,
		CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
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

	getLoaderManager().initLoader(Loaders.LoadCoursePhotos, null, this);

	setHasOptionsMenu(true);
	super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {

	inflater.inflate(R.menu.course_photos_fragment_menu, menu);

	super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
	    final ViewGroup container,
	    final Bundle savedInstanceState) {
	final View v = inflater.inflate(R.layout.fragment_course_photos_list,
		container, false);

	mGridView = (GridView) v.findViewById(android.R.id.list);
	mEmptyTextView = (TextView) v.findViewById(android.R.id.empty);
	mGridView.setAdapter(mAdapter);

	return v;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
	switch (item.getItemId()) {
	case id.menu_add_photo:
	    startPhotoCaptureIntent();
	    break;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
	    final Intent data) {
	switch (resultCode) {
	case Activities.TAKE_PICTURE:
	    handlePhotoTakenResult(resultCode);
	    break;
	}

	super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
	super.onResume();

	if (tPhoto != null && tPhotoFile != null)
	    handlePhotoTakenResult(Activity.RESULT_OK);

    }

    @Override
    public Loader<Cursor> onCreateLoader(final int arg0, final Bundle arg1) {
	return new CursorLoader(getParentActivity(),
		CoursePhotosContract.CONTENT_URI,
		new String[] {
			CoursePhotosContract.COURSE_ID,
			CoursePhotosContract._ID,
			CoursePhotosContract.PHOTO_FILE_PATH,
			CoursePhotosContract.NOTES,
			CoursePhotosContract.TIMESTAMP
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

    private void startPhotoCaptureIntent() {
	final Intent takePictureIntent = new Intent(
		MediaStore.ACTION_IMAGE_CAPTURE);

	try {
	    tPhotoFile = MediaUtil.createCourseImageFile(mCourse);
	} catch (final IOException e) {
	    Toast.makeText(getApplicationContext(),
		    "Cannot add new photo.\n" + e.getMessage(),
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
	tPhoto = new CoursePhoto(tPhotoFile, new DateTime(), mCourse, "");

	final ContentResolver cr = getApplicationContext().getContentResolver();
	final ContentValues values = new ContentValues();
	values.put(CoursePhotosContract.COURSE_ID,
		mCourse.getId().toString());
	values.put(CoursePhotosContract.PHOTO_FILE_PATH,
		tPhoto.getFileUri().toString());
	values.put(CoursePhotosContract.TIMESTAMP,
		tPhoto.getDateTaken().getMillis() / 1000);
	values.put(CoursePhotosContract.NOTES,
		tPhoto.getNotes());

	new AsyncQueryHandler(cr) {

	}.startInsert(0, null, CoursePhotosContract.CONTENT_URI, values);

	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
		Uri.fromFile(tPhotoFile));

	startActivityForResult(takePictureIntent, Activities.TAKE_PICTURE);
    }

    @Override
    public void onStop() {
	AsyncManager.cancelRunningTasks(sComponentUri, true);
	super.onStop();
    }

    /* ====================== Private Classes =================== */
    private static class CoursePhotoCursorWrapper {
	private final Cursor mCoursor;
	private final MoodleCourse mCourse;

	private Long cId;
	private Long cCourseId;
	private String cPhotoFilePath;
	private String cNotes;
	private Long cTimestamp;

	private CoursePhoto cPhoto;

	private static final int COURSE_ID = 0;
	private static final int PHOTO_ID = 1;
	private static final int Photo_FILE_PATH = 2;
	private static final int NOTES = 3;
	private static final int TIMESTAMP = 4;

	/**
	 * @param coursor
	 * @param course
	 */
	public CoursePhotoCursorWrapper(final Cursor cursor,
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
			.getLong(CoursePhotoCursorWrapper.COURSE_ID);
	    return cCourseId;
	}

	/**
	 * @return the Id
	 */
	@SuppressWarnings("unused")
	public Long getId() {
	    if (cId == null)
		cId = mCoursor.getLong(CoursePhotoCursorWrapper.PHOTO_ID);
	    return cId;
	}

	/**
	 * @return the Photo File Path
	 */
	public String getPhotoFilePath() {
	    if (cPhotoFilePath == null)
		cPhotoFilePath = mCoursor
			.getString(CoursePhotoCursorWrapper.Photo_FILE_PATH);
	    return cPhotoFilePath;
	}

	/**
	 * @return the Notes
	 */
	public String getNotes() {
	    if (cNotes == null)
		cNotes = mCoursor.getString(CoursePhotoCursorWrapper.NOTES);
	    return cNotes;
	}

	/**
	 * @return the Timestamp
	 */
	public Long getTimestamp() {
	    if (cTimestamp == null)
		cTimestamp = mCoursor
			.getLong(CoursePhotoCursorWrapper.TIMESTAMP);
	    return cTimestamp;
	}

	public CoursePhoto getCoursePhoto() {
	    if (cPhoto == null)
		cPhoto = new CoursePhoto(Uri.parse(getPhotoFilePath()),
			new DateTime(
				getTimestamp()),
			mCourse, getNotes());

	    return cPhoto;
	}

    }

    private static class CoursePhotoCursorAdatper
	    extends CursorAdapter {

	private final WeakReference<Context> mContext;

	private final MoodleCourse mCourse;

	private final String mAsyncTaskTag;

	public CoursePhotoCursorAdatper(final String asyncTaskTag,
		final MoodleCourse course,
		final Context context, final Cursor c,
		final int flags) {
	    super(context, c, flags);

	    mAsyncTaskTag = asyncTaskTag;
	    mContext = new WeakReference<Context>(context);
	    mCourse = course;
	}

	class ViewHolder implements SimpleViewHolder {
	    ImageView photo;
	    TextView date;
	    TextView notes;
	}

	@Override
	public void bindView(final View v, final Context context,
		final Cursor c) {
	    final ViewHolder h = (ViewHolder) v.getTag();

	    final CoursePhotoCursorWrapper data = new CoursePhotoCursorWrapper(
		    c, mCourse);

	    RemoteImageViewLoader.AsyncImageViewLoaderTask task =
		    (AsyncImageViewLoaderTask) h.photo.getTag();

	    if (task != null)
		task.cancel(true);

	    task = new RemoteImageViewLoader
		    .AsyncImageViewLoaderTask(h.photo, mAsyncTaskTag) {
			CoursePhoto mPhoto;
			WeakReference<Context> context;

			@Override
			protected void onPreExecute() {
			    mPhoto = data.getCoursePhoto();
			    context = new WeakReference<Context>(mContext.get());
			    super.onPreExecute();
			}

			@Override
			protected Bitmap doInBackground(final String... params) {
			    if (context.get() == null) {
				Log.e("Remote Image Loader", "Context expired");
				return null;
			    }
			    return mPhoto.getBitmap(context.get());
			}

			@Override
			protected void onPostExecute(final Bitmap result) {
			    final Bitmap thumb = ThumbnailUtils
				    .extractThumbnail(result,
					    PhotosContract.THUMB_HEIGHT_DP,
					    PhotosContract.THUMB_WIDTH_DP);
			    h.photo.setImageBitmap(thumb);
			}
		    };

	    task.execute();

	    final CoursePhoto coursePhoto = data.getCoursePhoto();
	    h.photo.setTag(task);
	    h.date.setText(DateFormatter.getShortDateTime(coursePhoto
		    .getDateTaken()));
	    h.notes.setText(data.getNotes());

	    h.photo.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(final View v) {
		    if (mContext.get() == null) {
			Log.e("Photo OnClick", "Context expired");
			return;
		    }

		    // Launch default viewer for the file
		    final Intent intent = new Intent();
		    intent.setAction(android.content.Intent.ACTION_VIEW);
		    intent.setDataAndType(
			    coursePhoto.getFileUri(), "image/*");

		    mContext.get().startActivity(intent);
		}
	    });
	}

	@Override
	public View newView(final Context context, final Cursor c,
		final ViewGroup root) {

	    final LayoutInflater inflater = (LayoutInflater) context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    final View view = inflater.inflate(R.layout.grid_item_course_photo,
		    root,
		    false);

	    final ViewHolder h = new ViewHolder();
	    h.photo = (ImageView) view.findViewById(R.id.imageview_photo);
	    h.date = (TextView) view.findViewById(R.id.textview_date);
	    h.notes = (TextView) view.findViewById(R.id.textview_notes);

	    view.setTag(h);

	    return view;
	}

    }

    /* =============================== Helper functions ====================== */

    private void handlePhotoTakenResult(final int resultCode) {
	if (resultCode != Activity.RESULT_OK) {
	    tPhotoFile.delete();

	    final ContentResolver cr = getApplicationContext()
		    .getContentResolver();

	    new AsyncQueryHandler(cr) {

	    }.startDelete(0, null, CoursePhotosContract.CONTENT_URI,
		    CoursePhotosContract.PHOTO_FILE_PATH + "=?",
		    new String[] { tPhoto.getFileUri().toString() });

	    tPhoto = null;
	    tPhotoFile = null;
	    return;
	}

	tPhoto = null;
	tPhotoFile = null;
    }

    /* =============================== Interfaces ============================ */
    private interface Activities {
	int TAKE_PICTURE = 0;
    }

    private static interface Loaders {
	int LoadCoursePhotos = 0;
    }
}
