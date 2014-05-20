/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.SyncEntities;

import android.content.Context;
import android.util.Log;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviders.ForumDiscussionPostProvider;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Databases.ContentProviders.ForumDiscussionProvider;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum.DiscussionPostDescriptior;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetDiscussionPosts;
import edu.uwi.mona.mobileourvle.app.Classes.Util.ApplicationDataManager;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.ForumDiscussionPostListFragment.EntityWrappers.ExtendedDiscussionPostWrapper;

import org.sourceforge.ah.android.utilities.Communication.CommuncationModule;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.Client.EntitySyncronizationManager;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.Client.SyncRecord;
import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.ContentProviders.EntitySyncronizerContract;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @author aston
 */
public class ForumDiscussionPostSyncronizationManager extends EntitySyncronizationManager {

    @Override
    protected void doPullSyncronization(final Context context, final SyncRecord record) {
        final UserSession lastUserSession = ApplicationDataManager.getLastUserSession(context);

        if (lastUserSession == null) {
            Log.d(getClass().getName(), "Last Session not found so aborting syncronization.");
            return;
        }

        // store the forum or module to pull in the cookie
        final String discussionParentCookie = record.getCookie();

        final ForumDiscussion discussion = ForumDiscussionProvider.getDeserializedDiscussion(
                discussionParentCookie);

        final ResponseObject response = CommuncationModule.senRequest(context,
                                                                      new GetDiscussionPosts(
                                                                              discussion,
                                                                              lastUserSession));

        if (response instanceof ResponseError) {
            record.withLastResponseText(response.getResponseText());
            record.withSyncStatus(EntitySyncronizerContract.Status.UNSYNCED);
            return;
        }

        final String discussionPostListJSON = response.getResponseText();
        final List<DiscussionPost> discussionPosts = JSONDecoder.getObjectList(
                new DiscussionPostDescriptior(), discussionPostListJSON);
        final ExtendedDiscussionPostWrapper[] extendedPostList = helper_organisePostList(
                discussionPosts);

        // to syncronize, i'll just remove all items in the db and refresh it
        ForumDiscussionPostProvider.removeAllPosts(context, discussion);
        ForumDiscussionPostProvider.insertExtendedDiscussionPosts(context, Arrays.asList(extendedPostList));

        record.withSyncStatus(EntitySyncronizerContract.Status.SYNCRONIZED);
    }

    @Override
    protected void doPushSyncronization(final Context context, final SyncRecord record) {
        Log.d("MoodleCourseSyncronizationManager",
              "Executing push syncronization for " + getEntityManagerClassName());
    }

    private ExtendedDiscussionPostWrapper[] helper_organisePostList(
            final List<DiscussionPost> postList) {

        // My sorting and indentation algorithm needs to be able to direct address
        // posts by id, so this object will facilitate that requirement
        final HashMap<Long, ExtendedDiscussionPostWrapper> extendedPostCache = new HashMap<Long, ExtendedDiscussionPostWrapper>();

        final ExtendedDiscussionPostWrapper[] extendedPostList =
                new ExtendedDiscussionPostWrapper[postList.size()];


        //
        // Wrap the posts in my extended post and prepare my container that
        // facilitates direct addressing of posts by their id.
        //
        for (int i = 0; i < postList.size(); i++) {
            extendedPostList[i] = new ExtendedDiscussionPostWrapper(postList.get(i));
            extendedPostCache.put(postList.get(i).getId(), extendedPostList[i]);
        }

        // Calculate the indentation factors
        for (final ExtendedDiscussionPostWrapper extendedPost : extendedPostList)
            extendedPost.setIndentationFactor(helper_getPostIndentationFactor(extendedPost,
                                                                              extendedPostCache));

        // Sort them properly
        Arrays.sort(extendedPostList, new Comparator<ExtendedDiscussionPostWrapper>() {

            private HashMap<Long, ExtendedDiscussionPostWrapper> postRootCache = new HashMap<Long, ExtendedDiscussionPostWrapper>();

            private ExtendedDiscussionPostWrapper helper_getPostAnscestorAtLevel(
                    final ExtendedDiscussionPostWrapper post, final int level) {

                if (post.getIndentationFactor() == level)
                    return post;

                // Assuming a maimum level of 9 since and level grater will not look
                // good on the phone
                final long uniqueKey = post.getPost().getParentId() * 10 + level;
                ExtendedDiscussionPostWrapper postParent = postRootCache.get(uniqueKey);

                if (postParent == null) {
                    postParent = helper_getPostAnscestorAtLevel(extendedPostCache.get(
                            post.getPost().getParentId()), level);

                    postRootCache.put(uniqueKey, postParent);

                }
                return postParent;
            }

            @Override
            public int compare(final ExtendedDiscussionPostWrapper extendedDiscussionPostWrapper,
                               final ExtendedDiscussionPostWrapper extendedDiscussionPostWrapper2) {
                if (extendedDiscussionPostWrapper
                        .getIndentationFactor().equals(extendedDiscussionPostWrapper2
                                                               .getIndentationFactor()))
                    return extendedDiscussionPostWrapper.getPost().getDateCreaded().compareTo(
                            extendedDiscussionPostWrapper2.getPost().getDateCreaded());

                final ExtendedDiscussionPostWrapper currentRoot;
                final ExtendedDiscussionPostWrapper compareToRoot;
                if (extendedDiscussionPostWrapper
                            .getIndentationFactor() > extendedDiscussionPostWrapper2
                            .getIndentationFactor()) {
                    currentRoot = helper_getPostAnscestorAtLevel(extendedDiscussionPostWrapper,
                                                                 extendedDiscussionPostWrapper2
                                                                         .getIndentationFactor());
                    compareToRoot = extendedDiscussionPostWrapper2;

                } else {
                    compareToRoot = helper_getPostAnscestorAtLevel(extendedDiscussionPostWrapper2,
                                                                   extendedDiscussionPostWrapper
                                                                           .getIndentationFactor());
                    currentRoot = extendedDiscussionPostWrapper;
                }

                if (compareToRoot.getPost().getId() == currentRoot.getPost().getId())
                    return extendedDiscussionPostWrapper.getIndentationFactor().compareTo(
                            extendedDiscussionPostWrapper2.getIndentationFactor());

                return compare(currentRoot, compareToRoot);
            }
        });

        return extendedPostList;
    }

    private int helper_getPostIndentationFactor(final ExtendedDiscussionPostWrapper post,
                                                final HashMap<Long, ExtendedDiscussionPostWrapper> extendedPostCache) {
        if (post.getPost().getParentId() == 0)
            return 0;

        if (post.getIndentationFactor() != 0)
            return post.getIndentationFactor();
        else
            post.setIndentationFactor(1 + helper_getPostIndentationFactor(extendedPostCache.get(
                    post.getPost().getParentId()), extendedPostCache));

        return post.getIndentationFactor();
    }
}