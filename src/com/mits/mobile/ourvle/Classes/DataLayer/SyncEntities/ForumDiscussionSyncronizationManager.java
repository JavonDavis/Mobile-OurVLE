/**
 *
 */
package com.mits.mobile.ourvle.Classes.DataLayer.SyncEntities;

import java.util.List;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Communication.CommuncationModule;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.Client.EntitySyncronizationManager;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.Client.SyncRecord;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.ContentProviders.EntityManagerContract;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.ContentProviders.EntitySyncronizerContract;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.EntitySyncronizer;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;

import android.content.Context;
import android.util.Log;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.DataLayer.Databases.ContentProviders.ForumDiscussionProvider;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum.CourseForumDescriptior;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum.ExtendedForumDiscussionDescriptior;
import com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetCourseDiscussions;
import com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetForumDiscussions;
import com.mits.mobile.ourvle.Classes.Util.ApplicationDataManager;

/**
 * @author aston
 */
public class ForumDiscussionSyncronizationManager extends
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

        // store the forum or module to pull in the cookie
        final String discussionParentCookie = record.getCookie();

        final DiscussionParent parent = ForumDiscussionProvider
                .getDeserializedParent(discussionParentCookie);

        ResponseObject response;
        if (parent.isForum())
            response = CommuncationModule
                    .senRequest(context,
                                new GetForumDiscussions(parent.getForum(),
                                                        lastUserSession));
        else {
            response = CommuncationModule
                    .senRequest(context,
                                new GetCourseDiscussions(parent.getModule().getCourseId(),
                                                        lastUserSession));

            if (response.getStatus() == 200) {

                List<CourseForum> forums = JSONDecoder.getObjectList(new CourseForumDescriptior(), response.getResponseText());

                response = CommuncationModule
                        .senRequest(context,
                                    new GetForumDiscussions(parent.getModule(),
                                                            lastUserSession));
            }

        }


        if (response instanceof ResponseError) {
            Log.e(getClass().toString(), "Discussion Sync Failed: " + response.getResponseText());
            record.withLastResponseText(response.getResponseText());
            record.withSyncStatus(EntitySyncronizerContract.Status.UNSYNCED);
            return;
        }

        final String discussionListJSON = response.getResponseText();
        final List<? extends ForumDiscussion> extendedForumDiscussions = JSONDecoder
                .getObjectList(new ExtendedForumDiscussionDescriptior(parent),
                               discussionListJSON);

        // to syncronize, i'll just remove all items in the db and refresh it
        ForumDiscussionProvider.removeAllDiscussions(context, parent);
        ForumDiscussionProvider.insertForumDiscussions(context,
                                                       (List<ForumDiscussion>) extendedForumDiscussions);

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
