package edu.uwi.mona.mobileourvle.classes.tasks;

import java.util.ArrayList;
import java.util.List;

import edu.uwi.mona.mobileourvle.classes.models.ForumDiscussion;
import edu.uwi.mona.mobileourvle.classes.moodle.ForumRestDiscussion;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/22/15.
 */
public class DiscussionTask {

    String token;

    String error;
    /**
     *
     * @param token
     */
    public DiscussionTask(String token) {
        this.token = token;
    }




    /**
     * Sync all the discussion topics of a forum.
     *
     * @return syncStatus
     *
     */
    public Boolean syncDiscussions(int forumid) {
        ArrayList<String> forumids = new ArrayList<String>();
        forumids.add(forumid + "");
        return syncDiscussions(forumids);
    }

    /**
     * Sync all the discussion topics in the list of forums.
     *
     * @return syncStatus
     */
    public Boolean syncDiscussions(ArrayList<String> forumids) {
        ForumRestDiscussion forumRestDiscussion = new ForumRestDiscussion(token);
        ArrayList<ForumDiscussion> mTopics = forumRestDiscussion.getDiscussions(forumids);

        /** Error checking **/
        // Some network or encoding issue.
        if (mTopics == null) {
            error = "Network issue!";
            return false;
        }

        // Moodle exception
        if (mTopics.size() == 0) {
            error = "No data received";
            // No additional debug info as that needs context
            return false;
        }

        List<ForumDiscussion> dbTopics;
        ForumDiscussion topic;
        for (int i = 0; i < mTopics.size(); i++) {
            topic = mTopics.get(i);

            dbTopics = ForumDiscussion.find(ForumDiscussion.class,
                    "discussionid = ?", topic.getDiscussionid()+"");
            if (dbTopics.size() > 0)
                topic.setId(dbTopics.get(0).getId());


            topic.save();
        }

        return true;
    }

}
