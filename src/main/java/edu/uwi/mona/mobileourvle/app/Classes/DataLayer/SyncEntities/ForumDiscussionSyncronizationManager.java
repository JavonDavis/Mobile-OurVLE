/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.SyncEntities;

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

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Android.ApplicationPrivateStore;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviders.ForumDiscussionProvider;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum.CourseForumDescriptior;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum.ExtendedForumDiscussionDescriptior;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetCourseDiscussions;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetForumDiscussions;
import edu.uwi.mona.mobileourvle.app.Classes.Util.ApplicationDataManager;

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
            JsonObject forumModuleMap = null;

            final String forumModuleMapEncoded = ApplicationPrivateStore.get(context,
                                                                             "forum-module-map");

            try {
                forumModuleMap = new JsonParser().parse(forumModuleMapEncoded).getAsJsonObject();
            } catch (JsonParseException e) {
                // Do nothing if invalid JSON

                forumModuleMap = new JsonObject();
                Log.d(getClass().toString(), "Forum Module map not JSON: " + forumModuleMapEncoded,
                      e);
            } catch (ClassCastException e) {
                // Do nothing if not JSON Object

                forumModuleMap = new JsonObject();
                Log.d(getClass().toString(),
                      "Forum Module map not JSON Object: " + forumModuleMapEncoded, e);
            } catch (IllegalStateException e) {
                // Do nothing if not JSON Object

                forumModuleMap = new JsonObject();
                Log.d(getClass().toString(),
                      "Forum Module map not JSON Object: " + forumModuleMapEncoded, e);
            }

            if (!forumModuleMap.has(parent.getModule().getId().toString())) {

                response = CommuncationModule
                        .senRequest(context,
                                    new GetCourseDiscussions(parent.getModule().getCourseId(),
                                                             lastUserSession));


                if (response.getStatus() == 200) {
                    final List<CourseForum> forums = JSONDecoder.getObjectList(
                            new CourseForumDescriptior(), response.getResponseText());

                    forumModuleMap = new JsonObject();
                    for (final CourseForum forum : forums) {
                        forumModuleMap.addProperty(forum.getModuleId(),
                                                   forum.getForumid().toString());
                    }
                    ApplicationPrivateStore.put(context, "forum-module-map",
                                                forumModuleMap.toString());
                }
            }

            if (forumModuleMap.has(parent.getModule().getId().toString())) {
                response = CommuncationModule
                        .senRequest(context,
                                    new GetForumDiscussions(
                                            forumModuleMap
                                                    .get(parent.getModule().getId().toString())
                                                    .getAsString(),
                                            lastUserSession));
            } else {
                response = new ResponseError("Forum Module Id Not Found.");
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
