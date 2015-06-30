/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.SyncEntities;

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

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviders.MoodleCourseProvider;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.MoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Courses.ExtendedMoodleCourseDescriptior;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetUserCourses;
import edu.uwi.mona.mobileourvle.app.Classes.Util.ApplicationDataManager;

/**
 * @author aston
 */
public class MoodleCourseSyncronizationManager extends
        EntitySyncronizationManager {

    public static List<MoodleCourse> getCourses(Context context)
    {
        final UserSession lastUserSession = ApplicationDataManager
                .getLastUserSession(context);

        if (lastUserSession == null) {
            Log.d("MoodleSycnManager",
                    "Last Session not found so aborting syncronization.");
            return null;
        }

        final ResponseObject response = CommuncationModule.senRequest(context,
                new GetUserCourses(
                        lastUserSession));

        if (response instanceof ResponseError) {
            Log.e("response error","MoodleaCourseSyncronizationManager");
            return null;
        }

        final String coursesJSON = response.getResponseText();
        final List<? extends MoodleCourse> extendedMoodleCourses = JSONDecoder
                .getObjectList(new ExtendedMoodleCourseDescriptior(),
                        coursesJSON);

        return (List<MoodleCourse>) extendedMoodleCourses;
    }

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
        Log.d("MCSM",
              "Executing push syncronization for "
              + getEntityManagerClassName());
    }

}
