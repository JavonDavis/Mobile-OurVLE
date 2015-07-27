package edu.uwi.mona.mobileourvle.classes.tasks;

import java.util.ArrayList;
import java.util.List;

import edu.uwi.mona.mobileourvle.classes.models.DiscussionPost;
import edu.uwi.mona.mobileourvle.classes.moodle.DiscussionRestPost;
import edu.uwi.mona.mobileourvle.classes.moodle.Posts;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/22/15.
 */
public class PostTask {
    String token;

    String error;

    /**
     * @param token
     *
     */
    public PostTask(String token) {
        this.token = token;
    }

    /**
     * Sync all topics in a discussion.
     *
     * @return syncStatus
     */
    public Boolean syncPosts(int discussionid) {
        DiscussionRestPost discussionRestPost = new DiscussionRestPost(token);
        Posts moodlePosts = discussionRestPost.getPosts(discussionid);

        /** Error checking **/
        // Some network or encoding issue.
        if (moodlePosts == null) {
            error = "Network issue!";
            return false;
        }

        // Moodle exception
        if (moodlePosts.getErrorcode() != null) {
            error = moodlePosts.getErrorcode();
            // No additional debug info as that needs context
            return false;
        }

        ArrayList<DiscussionPost> mPosts = moodlePosts.getPosts();
        // Warnings are not being handled
        List<DiscussionPost> dbPosts;
        DiscussionPost post;

        if (mPosts != null)
            for (int i = 0; i < mPosts.size(); i++) {
                post = mPosts.get(i);

                dbPosts = DiscussionPost.find(DiscussionPost.class,
                        "postid = ?", post.getPostid() + "");

                if (dbPosts.size() > 0)
                    post.setId(dbPosts.get(0).getId());


                post.save();
            }

        return true;
    }

    /**
     * Sync all topics in the list of discussions.
     *
     * Note: Moodle doesn't support fetching of posts from more than one
     * discussion at a time so, this is realized using multiple calls - one per
     * discussionid.
     *
     * @return syncStatus
     */
    public Boolean syncPosts(ArrayList<Integer> discussionids) {
        Boolean status = true;

        if (discussionids == null || discussionids.size() == 0)
            return false;

        for (int i = 0; i < discussionids.size(); i++)
            status = status & syncPosts(discussionids.get(i));

        return status;
    }
}
