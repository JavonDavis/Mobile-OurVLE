package edu.uwi.mona.mobileourvle.classes.moodle;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import edu.uwi.mona.mobileourvle.classes.helpers.GsonExclude;
import edu.uwi.mona.mobileourvle.classes.models.ForumDiscussion;
import edu.uwi.mona.mobileourvle.classes.transport.MoodleRestCall;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/8/15.
 */
public class ForumRestDiscussion {
    private final String DEBUG_TAG = "Forum Rest Discussion";
    private String token;

    public ForumRestDiscussion(String token) {
        this.token = token;
    }

    /**
     * Get all discussions for the list of forums.
     *
     * @param forumids
     *            List of forumids
     *
     * @return ArrayList of ForumDiscussion
     */
    public ArrayList<ForumDiscussion> getDiscussions(List<String> forumids) {
        ArrayList<ForumDiscussion> mDiscussions = null;
        String format = MoodleFunctions.RESPONSE_FORMAT;
        String function = MoodleFunctions.FUNCTION_GET_DISCUSSIONS;

        try {
            // Adding all parameters.
            String params = "";

            if (forumids == null)
                forumids = new ArrayList<String>();

            for (int i = 0; i < forumids.size(); i++)
                params += "&forumids[" + i + "]="
                        + URLEncoder.encode(forumids.get(i), "UTF-8");

            // Build a REST call url to make a call.
            String restUrl = MoodleFunctions.API_HOST + "/webservice/rest/server.php" + "?wstoken="
                    + token + "&wsfunction=" + function
                    + "&moodlewsrestformat=" + format;

            // Fetch content now.
            MoodleRestCall restCall = new MoodleRestCall();
            Reader reader = restCall.fetchContent(restUrl, params);
            GsonExclude ex = new GsonExclude();
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(ex)
                    .addSerializationExclusionStrategy(ex).create();
            mDiscussions = gson.fromJson(reader,
                    new TypeToken<List<ForumDiscussion>>() {
                    }.getType());
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "URL encoding failed");
            e.printStackTrace();
        }

        return mDiscussions;
    }
}
