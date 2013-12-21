/**
 *
 */
package com.mits.mobile.ourvle.Classes.DataLayer.SyncEntities;

import java.util.List;

import org.sourceforge.ah.android.utilities.Communication.CommuncationModule;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.Client.EntitySyncronizationManager;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.Client.SyncRecord;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.ContentProviders.EntitySyncronizerContract;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;

import android.content.Context;
import android.util.Log;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviders.MoodleCourseProvider;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Courses.ExtendedMoodleCourseDescriptior;
import com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetUserCourses;
import com.mits.mobile.ourvle.Classes.Util.ApplicationDataManager;

/**
 * @author aston
 */
public class MoodleCourseSyncronizationManager extends
        EntitySyncronizationManager {

    @SuppressWarnings("unchecked")
    @Override
    protected void doPullSyncronization(final Context context,
                                        final SyncRecord record) {
        final UserSession lastUserSession = ApplicationDataManager
                .getLastUserSession(context);

        if (lastUserSession == null) {
            Log.d(getClass().getName(),
                  "Last Session not found so aborting syncronization.");
            return;
        }

        final ResponseObject response = CommuncationModule.senRequest(context,
                                                                      new GetUserCourses(
                                                                              lastUserSession));

        if (response instanceof ResponseError) {
            record.withLastResponseText(response.getResponseText());
            record.withSyncStatus(EntitySyncronizerContract.Status.UNSYNCED);
            return;
        }

        final String coursesJSON = response.getResponseText();
        final List<? extends MoodleCourse> extendedMoodleCourses = JSONDecoder
                .getObjectList(new ExtendedMoodleCourseDescriptior(),
                               coursesJSON);

        // to syncronize, i'll just remove all items in the db and refresh it
        MoodleCourseProvider.removeAllCourses(context);
        MoodleCourseProvider
                .insertMoodleCourses(context,
                                     (List<MoodleCourse>) extendedMoodleCourses);

        record.withSyncStatus(EntitySyncronizerContract.Status.SYNCRONIZED);
    }

    @Override
    protected void doPushSyncronization(final Context context,
                                        final SyncRecord record) {
        Log.d("MoodleCourseSyncronizationManager",
              "Executing push syncronization for "
              + getEntityManagerClassName());
    }

}
