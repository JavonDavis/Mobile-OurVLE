package edu.uwi.mona.mobileourvle.classes.moodle;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.net.URLEncoder;

import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.classes.transport.MoodleRestCall;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/6/15.
 */
public class SiteRestInfo {

    private final String DEBUG_TAG = "Wrapped Site Info";
    private String token;
    SiteInfo siteInfo = new SiteInfo();

    public SiteRestInfo(String token) {
        this.token = token;
    }

    /**
     * Get the siteinfo for the current account
     *
     * @return siteinfo object
     */
    public SiteInfo getSiteInfo() {
        String format = MoodleFunctions.RESPONSE_FORMAT;
        String function = MoodleFunctions.FUNCTION_GET_SITE_INFO;

        try {
            // Adding all parameters.
            String params = "" + URLEncoder.encode("", "UTF-8");

            // Build a REST call url to make a call.
            String restUrl = MoodleFunctions.API_HOST + "/webservice/rest/server.php" + "?wstoken="
                    + token + "&wsfunction=" + function
                    + "&moodlewsrestformat=" + format;

            // Fetch content now
            MoodleRestCall restCall = new MoodleRestCall();
            Reader reader = restCall.fetchContent(restUrl, params);
            Gson gson = new GsonBuilder().create();
            siteInfo = gson.fromJson(reader, SiteInfo.class);
            reader.close();

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "URL encoding failed");
            e.printStackTrace();
        }

        if (siteInfo == null)
            return new SiteInfo();

        return siteInfo;
    }
}
