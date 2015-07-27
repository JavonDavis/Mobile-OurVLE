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
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;
import edu.uwi.mona.mobileourvle.classes.models.MoodleFunction;
import edu.uwi.mona.mobileourvle.classes.transport.MoodleRestCall;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/6/15.
 */
public class MoodleRestCourse {

    private final String DEBUG_TAG = "Wrapped Moodle Courses";
    private String token;

    public MoodleRestCourse(String token) {
        this.token = token;
    }

    /**
     * Get all the courses in the Moodle site.<br/>
     * User may not have permission to do this. In such cases, only one course
     * entry is added to the list with only error fields filled. If no entries
     * are found, then it could mean a network or encoding issue.
     *
     * This feature could be proven useful to administator users (Kirk) and lecturers
     *
     * @return ArrayList of MoodleCourse
     */
    public ArrayList<MoodleCourse> getAllCourses() {
        ArrayList<MoodleCourse> mCourses = new ArrayList<MoodleCourse>();
        String format = MoodleFunctions.RESPONSE_FORMAT;
        String function = MoodleFunctions.FUNCTION_GET_ALL_COURSES;

        try {
            // Adding all parameters.
            String params = "" + URLEncoder.encode("", "UTF-8");

            // Build a REST call url to make a call.
            String restUrl = MoodleFunctions.API_HOST + "/webservice/rest/server.php" + "?wstoken="
                    + token + "&wsfunction=" + function
                    + "&moodlewsrestformat=" + format;

            // Fetch content now.
            MoodleRestCall restCall = new MoodleRestCall();
            Reader reader = restCall.fetchContent(restUrl, params);
            GsonExclude exclude = new GsonExclude();
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(exclude)
                    .addSerializationExclusionStrategy(exclude).create();
            mCourses = gson.fromJson(reader,
                    new TypeToken<List<MoodleCourse>>() {
                    }.getType());
            reader.close();

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "URL encoding failed");
            e.printStackTrace();
        }

        return mCourses;
    }

    /**
     * Get all the courses that a user is enrolled in<br/>
     * In case of errors, only one course entry is added to the list with only
     * error fields filled. If no entries are found, then it could mean a
     * network or encoding issue.
     *
     *
     * @param userId
     *            userId of the user whose courses are needed
     * @return ArrayList<MoodleCourse>
     *
     */
    public ArrayList<MoodleCourse> getEnrolledCourses(String userId) {
        ArrayList<MoodleCourse> mCourses = new ArrayList<MoodleCourse>();
        String format = MoodleFunctions.RESPONSE_FORMAT;
        String function = MoodleFunctions.FUNCTION_GET_ENROLLED_COURSES;

        try {
            // Adding all parameters.
            String params = "&" + URLEncoder.encode("userid", "UTF-8") + "="
                    + userId;

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
            mCourses = gson.fromJson(reader,
                    new TypeToken<List<MoodleCourse>>() {
                    }.getType());
            reader.close();

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "URL encoding failed");
            e.printStackTrace();
        }

        return mCourses;
    }
}
