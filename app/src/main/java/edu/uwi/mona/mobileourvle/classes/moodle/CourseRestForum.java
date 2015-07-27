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
import edu.uwi.mona.mobileourvle.classes.models.CourseForum;
import edu.uwi.mona.mobileourvle.classes.transport.MoodleRestCall;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/8/15.
 */
public class CourseRestForum {

    private final String DEBUG_TAG = "Wrapped Moodle Forum";
    private String token;

    public CourseRestForum(String token) {
        this.token = token;
    }

    /**
     * Get all the forums of given course on OurVLE.<br/>
     *
     * @return ArrayList of CourseForums
     *
     * @author Javon Davis
     */
    public ArrayList<CourseForum> getForums(String courseid) {
        ArrayList<String> courseids = new ArrayList<String>();
        courseids.add(courseid);
        return getForums(courseids);
    }

    /**
     * Get all the forums of given courses on OurVLE.<br/>
     *
     * @return ArrayList of CourseForums
     *
     * @author Javon Davis
     */
    public ArrayList<CourseForum> getForums(ArrayList<String> courseids) {
        ArrayList<CourseForum> mForums = new ArrayList<CourseForum>();
        String format = MoodleFunctions.RESPONSE_FORMAT;
        String function = MoodleFunctions.FUNCTION_GET_FORUMS;

        try {
            // Adding all parameters.
            String params = "";
            for (int i = 0; i < courseids.size(); i++) {
                params += "&courseids[" + i + "]="
                        + URLEncoder.encode(courseids.get(i), "UTF-8");
            }

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
            mForums = gson.fromJson(reader, new TypeToken<List<CourseForum>>() {
            }.getType());
            reader.close();

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "URL encoding failed");
            e.printStackTrace();
        }

        return mForums;
    }
}
