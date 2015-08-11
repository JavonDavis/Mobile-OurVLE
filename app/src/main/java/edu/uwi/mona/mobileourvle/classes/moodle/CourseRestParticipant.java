package edu.uwi.mona.mobileourvle.classes.moodle;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.net.URLEncoder;
import java.util.List;

import edu.uwi.mona.mobileourvle.classes.helpers.GsonExclude;
import edu.uwi.mona.mobileourvle.classes.models.CourseParticipant;
import edu.uwi.mona.mobileourvle.classes.models.MoodleFunction;
import edu.uwi.mona.mobileourvle.classes.transport.MoodleRestCall;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 8/3/15.
 */
public class CourseRestParticipant {
    private final String DEBUG_TAG = "CourseRestParticipant";
    private String mUrl;
    private String token;

    public CourseRestParticipant(String token) {
        this.mUrl = MoodleFunctions.API_HOST;
        this.token = token;
    }

    /**
     * Retrieve users registered in a course
     *
     * @param courseid
     *            Moodle courseid of the course
     *
     * @return list of CourseParticipant
     */
    public List<CourseParticipant> getUsers(int courseid) {
        List<CourseParticipant> mUsers = null; // So that we know about network
        // failures
        String format = MoodleFunctions.RESPONSE_FORMAT;
        String function = MoodleFunctions.FUNCTION_GET_USERS_FROM_COURSE;

        try {
            // Adding all parameters.
            String params = "&courseid="
                    + URLEncoder.encode(courseid + "", "UTF-8");

            // Build a REST call url to make a call.
            String restUrl = mUrl + "/webservice/rest/server.php" + "?wstoken="
                    + token + "&wsfunction=" + function
                    + "&moodlewsrestformat=" + format;

            // Fetch content now.
            MoodleRestCall mrc = new MoodleRestCall();
            Reader reader = mrc.fetchContent(restUrl, params);
            GsonExclude ex = new GsonExclude();
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(ex)
                    .addSerializationExclusionStrategy(ex).create();
            mUsers = gson.fromJson(reader, new TypeToken<List<CourseParticipant>>() {
            }.getType());
            reader.close();

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "URL encoding failed");
            e.printStackTrace();
        }

        return mUsers;
    }
}
