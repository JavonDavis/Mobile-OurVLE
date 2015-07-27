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
import edu.uwi.mona.mobileourvle.classes.models.CourseSection;
import edu.uwi.mona.mobileourvle.classes.transport.MoodleRestCall;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/7/15.
 */
public class CourseRestContent {

    private final String DEBUG_TAG = "Wrapped Course Contents";
    private String token;

    public CourseRestContent(String token) {
        this.token = token;
    }

    /**
     * Get all the sections in the Course.<br/>
     * User may not have permission to do this. In such cases, only one course
     * entry is added to the list with only error fields filled. If no entries
     * are found, then it could mean a network or encoding issue.
     *
     * @return ArrayList<MoodleSection>
     *
     * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
     */
    public ArrayList<CourseSection> getCourseContent(String courseid) {
        ArrayList<CourseSection> sections = null;
        String format = MoodleFunctions.RESPONSE_FORMAT;
        String function = MoodleFunctions.FUNCTION_GET_COURSE_CONTENTS;

        try {
            // Adding all parameters.
            String params = "&courseid=" + URLEncoder.encode(courseid, "UTF-8");

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
            sections = gson.fromJson(reader,
                    new TypeToken<List<CourseSection>>() {
                    }.getType());
            reader.close();

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "URL encoding failed");
            e.printStackTrace();
        }

        return sections;
    }
}
