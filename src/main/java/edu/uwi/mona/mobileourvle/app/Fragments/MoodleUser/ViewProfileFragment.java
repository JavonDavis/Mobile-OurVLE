/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Fragments.MoodleUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.sourceforge.ah.android.utilities.AndroidUtil.AsyncManager;
import org.sourceforge.ah.android.utilities.AndroidUtil.ContactsUtil;
import org.sourceforge.ah.android.utilities.AndroidUtil.ManagedAsyncTask;
import org.sourceforge.ah.android.utilities.Communication.CommuncationModule;
import org.sourceforge.ah.android.utilities.Communication.Interfaces.OnCommunicationResponseListener;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin.OnReloadFragmentListener;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.CompositeArrayAdapter.Partition;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.PinnedHeaderListAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.uwi.mona.mobileourvle.app.Activities.CourseListActivity;
import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Android.PhoneContact;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.Wrappers.MoodleUserContactDbWrapper;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.UserProfileField;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleCourseParcel;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.MoodleUserParcel;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetUserProfile;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.AuthenticatedListFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.ViewProfileFragment.Constants.UserProfileFieldCategories;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.ViewProfileFragment.Constants.UserProfileFieldCategories.Category;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.ViewProfileFragment.Factory.UserProfileFieldFactoryUtil;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.ViewProfileFragment.ListAdapters.ProfileFieldListAdaper;

/**
 * @author Aston Hamilton
 */
public class ViewProfileFragment extends AuthenticatedListFragment implements
        OnCommunicationResponseListener {
    private MoodleUser mUser;
    private MoodleCourse mCourse;
    private PhoneContact mPhoneContact;

    private MoodleUserContactDbWrapper mDbWrapper;

    private TextView mFullNameTextView;
    private QuickContactBadge mContactBadge;

    private PinnedHeaderListAdapter<String, UserProfileField> mProfileFieldListAdaper;

    private DefaultCommunicationModulePlugin mCommunicationModulePlugin;

    private Partition<String, UserProfileField> mInfoPartiton;
    private Partition<String, UserProfileField> mPhoneNumberListPartiton;
    private Partition<String, UserProfileField> mEmailListPartiton;
    private Partition<String, UserProfileField> mSocialListPartiton;
    private Partition<String, UserProfileField> mOtherFieldsListPartiton;
    private Partition<String, UserProfileField> mGroupsListPartiton;
    private Partition<String, UserProfileField> mRolesListPartiton;
    private Partition<String, UserProfileField> mPreferencesListPartiton;
    private Partition<String, UserProfileField> mOtherCoursesListPartiton;

    private final HashMap<String, UserProfileField> mProfileFieldCollection = new HashMap<String, UserProfileField>();
    private String mEmptyListString;
    private Activity mActivity;
    private final String sComponentUri;
    private static boolean isLargeScreen = false;

    public ViewProfileFragment() {
        super();

        sComponentUri = this.getClass().getName();
    }

    public static ViewProfileFragment newInstance(final UserSession session,
                                                  final MoodleUser user,
                                                  final MoodleCourse course) {
        final ViewProfileFragment f = new ViewProfileFragment();


        f.setUserSession(session);
        f.setMoodleUser(user);
        f.setMoodleCourse(course);

        return f;
    }

    private void setMoodleUser(final MoodleUser user) {
        getFragmentArguments().putParcelable(ParcelKeys.MOODLE_USER,
                                             new MoodleUserParcel(user));
        mUser = user;
    }

    private void setMoodleCourse(final MoodleCourse course) {
        getFragmentArguments().putParcelable(ParcelKeys.MOODLE_COURSE,
                                             new MoodleCourseParcel(course));
        mCourse = course;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = ((MoodleUserParcel) getFragmentArguments()
                .getParcelable(ParcelKeys.MOODLE_USER))
                .getWrappedObejct();
        mCourse = ((MoodleCourseParcel) getFragmentArguments()
                .getParcelable(ParcelKeys.MOODLE_COURSE))
                .getWrappedObejct();

        mProfileFieldListAdaper = new ProfileFieldListAdaper(mActivity);

        setListAdapter(mProfileFieldListAdaper);

        mCommunicationModulePlugin = new DefaultCommunicationModulePlugin(
                new OnReloadFragmentListener() {

                    @Override
                    public void onCommunicationMenuItemTriggered() {
                        loadProfile();
                    }
                });

        registerPlugin(mCommunicationModulePlugin);

        mDbWrapper = new MoodleUserContactDbWrapper(mActivity);

        mEmptyListString = getString(R.string.no_profile);
    }

    /*@Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        if(!isLargeScreen) {

            inflater.inflate(R.menu.view_user_profile_menu, menu);

            if (mPhoneContact != null) {
                inflater.inflate(R.menu.change_user_profile_menu, menu);
                menu.findItem(R.id.menu_add_profile).setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
            }

            super.onCreateOptionsMenu(menu, inflater);
        }
    }*/

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_view_user_profile,
                                        container, false);

        mFullNameTextView = (TextView) v.findViewById(R.id.textview_fullname);
        mContactBadge = (QuickContactBadge) v
                .findViewById(R.id.quickcontact_contact);
        mFullNameTextView.setText("Full Name");
        mContactBadge
                .setImageResource(R.drawable.ic_contact_picture);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadProfile();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Check if the contact was deleted while the app was down
        if (mPhoneContact != null)
            if (!ContactsUtil.contactExists(mActivity,
                                            mPhoneContact.getUri()))
                mPhoneContact = null;
    }

    /*@Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_profile:
                addProfileToContacts();
                break;
            case R.id.menu_change_profile:
                pickContact();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {

        if (requestCode == ActivityResults.ADD_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                final Uri newUri;
                if (data != null)
                    newUri = data.getData();
                else
                    // a new contact was created but the Contacts app
                    // implementation didn't return us a uri
                    // This happens on HTC_HERO and maybe some other devices.
                    // As a work around let's set the newUri to the uri of the
                    // most recently added contact
                    // returned from a query of the contacts. (returns uri with
                    // most recent contact _ID)
                    newUri = ContactsUtil.getNewestContactUri(mActivity);

                if (newUri != null)
                    setContactUri(newUri);
                else
                    Toast.makeText(mActivity,
                                   "Please link to new contact.", Toast.LENGTH_LONG)
                         .show();
            } else
                Toast.makeText(mActivity,
                               "Please link to new contact.", Toast.LENGTH_LONG)
                     .show();
        } else if (requestCode == ActivityResults.PICK_CONTACT)
            if (resultCode == Activity.RESULT_OK) {
                final Uri newUri = data.getData();

                setContactUri(newUri);
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCommunicationError(final ResponseError response) {
        mCommunicationModulePlugin.defaultResponseError(response);
    }

    @Override
    public void onCommunicationResponse(final int requestId,
                                        final ResponseObject response) {
        setEmptyText(mEmptyListString);
        switch (requestId) {
            case Requests.GET_PROFILE:
                mCommunicationModulePlugin.turnOffLoadingIcon();
        /*
	     * The API returns a list of profiles but we only want one since we
	     * only requested one with GetUserProfileRequest
	     */
                final JsonArray profileFields = (JsonArray) new JsonParser()
                        .parse(response.getResponseText());
                if (profileFields.size() == 0) {

                    Toast.makeText(mActivity, "No profile found",
                                   Toast.LENGTH_SHORT)
                         .show();
                    // I expect the database to be loaded by the time the request is
                    // done
                    mPhoneContact = mDbWrapper.getMoodleUserContact(mUser);

                    refreshQuickContact();
                    mFullNameTextView.setText(mUser.getFullName());
                    return;
                }
                // I expect the database to be loaded by the time the request is
                // done
                mPhoneContact = mDbWrapper.getMoodleUserContact(mUser);

                refreshQuickContact();
                loadProfileFromJson((JsonObject) profileFields.get(0));

                break;
        }

    }

    @Override
    public void onDestroy() {
        mDbWrapper.close();

        AsyncManager.cancelRunningTasks(sComponentUri, true);
        CommuncationModule.cancelAllRunningAsyncRequests(this);
        super.onDestroy();
    }

    private void pickContact() {
        final Intent pickContactIntent = new Intent(Intent.ACTION_PICK,
                                                    ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(pickContactIntent, ActivityResults.PICK_CONTACT);
    }

    private void addProfileToContacts() {
        final Intent i = new Intent(Intent.ACTION_INSERT);
        i.setType(ContactsContract.Contacts.CONTENT_TYPE);
        i.putExtra(ContactsContract.Intents.Insert.NAME, mUser.getFullName());

        final ArrayList<ContentValues> dataList = new ArrayList<ContentValues>();
        helper_addPhoneNumbersToInsertDataList(dataList);
        helper_addEmailToInsertDataList(dataList);
        helper_addIMToInsertDataList(dataList);
        helper_addInfoToInsertDataList(dataList);
        helper_addAddressToInsertDataList(dataList);
        helper_addPhotoToInsertDataList(dataList);

        i.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA,
                                      dataList);

	/*
	 * Android 4.0.3 upwards uses this clause to check if the activity
	 * should finish after the save so I must pass it to the intent
	 */
        i.putExtra("finishActivityOnSaveCompleted", true);
        startActivityForResult(i, ActivityResults.ADD_CONTACT);
    }

    private void loadProfile() {
        mCommunicationModulePlugin.turnOnLoadingIcon();

        setEmptyText("Loading profile...");
        final GetUserProfile request;
        if (mCourse == null)
            request = new GetUserProfile(getUserSession(), mUser);
        else
            request = new GetUserProfile(getUserSession(), mUser, mCourse);

        new ManagedAsyncTask<AsyncRequestParams, Void, Void>(sComponentUri) {

            @Override
            protected Void doInBackground(final AsyncRequestParams... params) {
                final AsyncRequestParams param = params[0];

                // The actual loading of the database is time consuming so I'm
                // doing it in an asynctask before I fetch the data from the API
                param.dbWrapper.loadDatabase();
                CommuncationModule.sendAsyncRequest(
                        mActivity,
                        param.request,
                        Requests.GET_PROFILE, param.listener);

                return null;
            }

        }.execute(new AsyncRequestParams(mDbWrapper, request, this));

    }

    private void setContactUri(final Uri uri) {
        if (ContactsUtil.contactExists(mActivity, uri)) {
            mPhoneContact = new PhoneContact(mActivity, uri);
            mDbWrapper.saveContact(mUser, mPhoneContact);
        } else
            mPhoneContact = null;

        refreshQuickContact();
    }

    @SuppressLint("NewApi")
    private void refreshQuickContact() {
        if (mPhoneContact != null) {
            mContactBadge.assignContactUri(mPhoneContact.getUri());
            mContactBadge.setOnClickListener(mContactBadge);
            mContactBadge
                    .setImageBitmap(mPhoneContact.getProfileAvatarBitmap());
        } else {
            mContactBadge
                    .setImageResource(R.drawable.ic_contact_picture);
            new ManagedAsyncTask<String, Void, Bitmap>(sComponentUri) {

                @Override
                protected void onPreExecute() {

                    mContactBadge
                            .setImageResource(R.drawable.ic_contact_picture);
                }

                @Override
                protected Bitmap doInBackground(final String... params) {
                    try {
                        final URL newurl = new URL(params[0]);
                        final Bitmap imageBitmap = BitmapFactory
                                .decodeStream(
                                        newurl.openConnection().getInputStream());

                        return imageBitmap;
                    } catch (final IOException e) {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(final Bitmap imageBitmap) {
                    if (imageBitmap != null)
                        mContactBadge.setImageBitmap(imageBitmap);
                    else
                        Toast.makeText(mActivity,
                                       "Profile picture not found.",
                                       Toast.LENGTH_SHORT)
                             .show();

                    super.onPostExecute(imageBitmap);
                }

            }.execute(mUser.getPictureUrl());

            mContactBadge.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    pickContact();
                }
            });
        }

        if (mActivity != null)
            if (android.os.Build.VERSION.SDK_INT >= 11)
                mActivity.invalidateOptionsMenu();
    }

    private void loadProfileFromJson(final JsonObject profileJsonObject) {
        String fullname = "";

        if (profileJsonObject.has("fullname"))
            fullname = profileJsonObject.get("fullname").getAsString();

        if (fullname.trim().length() == 0) {
            if (profileJsonObject.has("firstname"))
                fullname = profileJsonObject.get("firstname").getAsString();

            if (profileJsonObject.has("lastname "))
                fullname += profileJsonObject.get("lastname ").getAsString();

            if (fullname.trim().length() == 0)
                fullname = "Full Name";
        }

        if (fullname.trim().length() == 0) {
            fullname = mUser.getFullName();
        }

        mFullNameTextView.setText(fullname);

        mInfoPartiton = new Partition<String, UserProfileField>(
                false, true, "INFO", new ArrayList<UserProfileField>());
        mPhoneNumberListPartiton = new Partition<String, UserProfileField>(
                false, true, "PHONE NUMBERS", new ArrayList<UserProfileField>());
        mEmailListPartiton = new Partition<String, UserProfileField>(
                false, true, "EMAIL ADDRESSES",
                new ArrayList<UserProfileField>());
        mSocialListPartiton = new Partition<String, UserProfileField>(
                false, true, "IM", new ArrayList<UserProfileField>());
        mOtherFieldsListPartiton = new Partition<String, UserProfileField>(
                false, true, "OTHER", new ArrayList<UserProfileField>());
        mGroupsListPartiton = new Partition<String, UserProfileField>(
                false, true, "GROUPS", new ArrayList<UserProfileField>());
        mRolesListPartiton = new Partition<String, UserProfileField>(
                false, true, "ROLES", new ArrayList<UserProfileField>());
        mPreferencesListPartiton = new Partition<String, UserProfileField>(
                false, true, "PREFERENCES", new ArrayList<UserProfileField>());
        mOtherCoursesListPartiton = new Partition<String, UserProfileField>(
                false, true,
                (mCourse == null ? "" : "OTHER ") + "COURSES",
                new ArrayList<UserProfileField>());

        UserProfileFieldCategories.setPartitionCategory(mInfoPartiton,
                                                        Category.INFO);
        UserProfileFieldCategories.setPartitionCategory(
                mPhoneNumberListPartiton,
                Category.PHONE);
        UserProfileFieldCategories.setPartitionCategory(mEmailListPartiton,
                                                        Category.EMAIL);
        UserProfileFieldCategories.setPartitionCategory(mSocialListPartiton,
                                                        Category.SOCIAL);
        UserProfileFieldCategories.setPartitionCategory(
                mOtherFieldsListPartiton,
                Category.OTHER);
        UserProfileFieldCategories.setPartitionCategory(mGroupsListPartiton,
                                                        Category.GROUPS);
        UserProfileFieldCategories.setPartitionCategory(mRolesListPartiton,
                                                        Category.ROLES);
        UserProfileFieldCategories.setPartitionCategory(
                mPreferencesListPartiton,
                Category.PERFERENCES);
        UserProfileFieldCategories.setPartitionCategory(
                mOtherCoursesListPartiton,
                Category.COURSES);

        UserProfileFieldFactoryUtil.loadFieldsIntoPartitonFromJson(
                profileJsonObject,
                mInfoPartiton,
                mProfileFieldCollection,
                UserProfileFieldCategories.INFO_PROFILE_FIELDS);
        UserProfileFieldFactoryUtil.loadFieldsIntoPartitonFromJson(
                profileJsonObject,
                mPhoneNumberListPartiton,
                mProfileFieldCollection,
                UserProfileFieldCategories.PHONE_PROFILE_FIELDS);
        UserProfileFieldFactoryUtil.loadFieldsIntoPartitonFromJson(
                profileJsonObject,
                mEmailListPartiton,
                mProfileFieldCollection,
                UserProfileFieldCategories.EMAIL_PROFILE_FIELDS);
        UserProfileFieldFactoryUtil.loadFieldsIntoPartitonFromJson(
                profileJsonObject,
                mSocialListPartiton,
                mProfileFieldCollection,
                UserProfileFieldCategories.SOCIAL_PROFILE_FIELDS);
        UserProfileFieldFactoryUtil.loadFieldsIntoPartitonFromJson(
                profileJsonObject,
                mOtherFieldsListPartiton,
                mProfileFieldCollection,
                UserProfileFieldCategories.OTHER_PROFILE_FIELDS);

        UserProfileFieldFactoryUtil
                .loadCustomFieldsToPartitionFromJson(
                        (JsonArray) profileJsonObject.get("customfields"),
                        mOtherFieldsListPartiton,
                        mProfileFieldCollection);
        UserProfileFieldFactoryUtil
                .loadGroupsFieldsToPartitionFromJson(
                        (JsonArray) profileJsonObject.get("groups"),
                        mGroupsListPartiton,
                        mProfileFieldCollection);
        UserProfileFieldFactoryUtil
                .loadRoleFieldsToPartitionFromJson(
                        (JsonArray) profileJsonObject.get("roles"),
                        mRolesListPartiton,
                        mProfileFieldCollection);
	/*
	 * I chose not showing preferences but left in the capability
	 * UserProfileFieldFactoryUtil.loadPreferenceFieldsToPartitionFromJson(
	 * (JsonArray) profileJsonObject.get("preferences"),
	 * mPreferencesListPartiton, mProfileFieldCollection);
	 */

        UserProfileFieldFactoryUtil
                .loadCourseFieldsToPartitionFromJson(
                        (JsonArray) profileJsonObject.get("enrolledcourses"),
                        mOtherCoursesListPartiton,
                        mProfileFieldCollection);

        mProfileFieldListAdaper.setNotificationsEnabled(false);
        mProfileFieldListAdaper.clearPartitions();
        mProfileFieldListAdaper.addPartition(mInfoPartiton);
        mProfileFieldListAdaper.addPartition(mPhoneNumberListPartiton);
        mProfileFieldListAdaper.addPartition(mEmailListPartiton);
        mProfileFieldListAdaper.addPartition(mSocialListPartiton);
        mProfileFieldListAdaper.addPartition(mOtherFieldsListPartiton);
        mProfileFieldListAdaper.addPartition(mGroupsListPartiton);
        mProfileFieldListAdaper.addPartition(mRolesListPartiton);
        mProfileFieldListAdaper.addPartition(mPreferencesListPartiton);
        mProfileFieldListAdaper.addPartition(mOtherCoursesListPartiton);
        mProfileFieldListAdaper.setNotificationsEnabled(true);
        mProfileFieldListAdaper.notifyDataSetChanged();
    }

    /* =============== Private classes ======================= */
    // Small class to transport some args to the load profile asynctask
    private static class AsyncRequestParams {
        final private MoodleUserContactDbWrapper dbWrapper;
        final private GetUserProfile request;
        final private OnCommunicationResponseListener listener;

        public AsyncRequestParams(final MoodleUserContactDbWrapper dbWrapper,
                                  final GetUserProfile request,
                                  final OnCommunicationResponseListener listener) {
            super();
            this.dbWrapper = dbWrapper;
            this.request = request;
            this.listener = listener;
        }

    }

    /* ================ Interfaces ================== */
    private static interface Requests {
        static final int GET_PROFILE = 0;
    }

    private static interface ActivityResults {
        static final int ADD_CONTACT = 0;
        static final int PICK_CONTACT = 1;
    }

    /* ================== Helper Methods ================ */

    private void helper_addPhoneNumbersToInsertDataList(
            final ArrayList<ContentValues> dataList) {

        for (final UserProfileField field : mPhoneNumberListPartiton
                .getDataSource()) {
            final ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Phone.NUMBER,
                    field.getValue());
            dataList.add(row);
        }
    }

    private void helper_addEmailToInsertDataList(
            final ArrayList<ContentValues> dataList) {

        for (final UserProfileField field : mEmailListPartiton
                .getDataSource()) {
            final ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Email.ADDRESS,
                    field.getValue());
            dataList.add(row);
        }
    }

    private void helper_addIMToInsertDataList(
            final ArrayList<ContentValues> dataList) {

        for (final UserProfileField field : mSocialListPartiton
                .getDataSource()) {
            final ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Im.PROTOCOL,
                    UserProfileFieldCategories.getImProtocol(field.getRawName()));
            row.put(ContactsContract.CommonDataKinds.Im.DATA,
                    field.getValue());
            dataList.add(row);
        }
    }

    private void helper_addAddressToInsertDataList(
            final ArrayList<ContentValues> dataList) {

        final ContentValues row = new ContentValues();
        if (mProfileFieldCollection.containsKey("city")) {
            row.put(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                    mProfileFieldCollection.get("city").getValue());

        }
        if (mProfileFieldCollection.containsKey("address")) {
            row.put(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS,
                    mProfileFieldCollection.get("address").getValue());

        }
        if (mProfileFieldCollection.containsKey("country")) {
            row.put(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                    mProfileFieldCollection.get("country").getValue());

        }
        dataList.add(row);

    }

    private void helper_addInfoToInsertDataList(
            final ArrayList<ContentValues> dataList) {

        if (mProfileFieldCollection.containsKey("url")) {
            final ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Website.URL,
                    mProfileFieldCollection.get("url").getValue());

            dataList.add(row);
        }

        if (mProfileFieldCollection.containsKey("description")) {
            final ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Note.NOTE,
                    mProfileFieldCollection.get("description").getValue());

            dataList.add(row);
        }
    }

    private void helper_addPhotoToInsertDataList(
            final ArrayList<ContentValues> dataList) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ((BitmapDrawable) mContactBadge.getDrawable()).getBitmap()
                                                      .compress(Bitmap.CompressFormat.PNG, 100,
                                                                stream);

        final byte[] byteArray = stream.toByteArray();

        final ContentValues row = new ContentValues();
        row.put(ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
        row.put(ContactsContract.CommonDataKinds.Photo.PHOTO,
                byteArray);

        dataList.add(row);
    }

}
