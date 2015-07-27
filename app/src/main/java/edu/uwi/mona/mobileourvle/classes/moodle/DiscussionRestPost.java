package edu.uwi.mona.mobileourvle.classes.moodle;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.net.URLEncoder;

import edu.uwi.mona.mobileourvle.classes.helpers.GsonExclude;
import edu.uwi.mona.mobileourvle.classes.transport.MoodleRestCall;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/8/15.
 */
public class DiscussionRestPost {

    private final String DEBUG_TAG = "Discussion Rest Post";
    private String token;

    public DiscussionRestPost(String token) {
        this.token = token;
    }

    /**
     * Get all posts for a discussion.
     *
     * @param discussionid
     *
     * @return MoodlePosts
     *
     * @author Javon Davis
     */
    public Posts getPosts(int discussionid) {
        Posts mPosts = null;
        String format = MoodleFunctions.RESPONSE_FORMAT;
        String function = MoodleFunctions.FUNCTION_GET_POSTS;

        try {
            // Adding all parameters.
            String params = "";

            params += "&discussionid="
                    + URLEncoder.encode(discussionid + "", "UTF-8");

            // Build a REST call url to make a call.
            String restUrl = MoodleFunctions.API_HOST + "/webservice/rest/server.php" + "?wstoken="
                    + token + "&wsfunction=" + function
                    + "&moodlewsrestformat=" + format;

            // Fetch content now.
            MoodleRestCall mrc = new MoodleRestCall();
            Reader reader = mrc.fetchContent(restUrl, params);
            GsonExclude ex = new GsonExclude();
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(ex)
                    .addSerializationExclusionStrategy(ex).create();
            mPosts = gson.fromJson(reader, Posts.class);
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "URL encoding failed");
            e.printStackTrace();
        }

        return mPosts;
    }

}
