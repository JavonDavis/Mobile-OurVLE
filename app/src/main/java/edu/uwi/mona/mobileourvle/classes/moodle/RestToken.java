package edu.uwi.mona.mobileourvle.classes.moodle;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/6/15.
 */
public class RestToken {

    private String username;
    private String password;
    private String url;

    public RestToken(String username, String password) {
        this.username = username;
        this.password = password;
        this.url = MoodleFunctions.API_HOST+"login/token.php";
    }

    public Token getToken() {
        String urlParams = "";

        // set required parameters for moodleToken url
        try {
            urlParams = "username=" + URLEncoder.encode(username, "UTF-8")
                    + "&password=" + URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d("Wrapped Token", "credential encoding failed!");
            e.printStackTrace();
        }

        return getTokenRequest(urlParams,
                MoodleFunctions.SERVICE_MOODLE_MOBILE);
    }

    private Token getTokenRequest(String urlParams, String serviceName) {

        HttpURLConnection con;
        Token token = new Token();
        try {
            con = (HttpURLConnection) new URL(url + "?" + urlParams
                    + "&service=" + serviceName).openConnection();
            con.setRequestProperty("Accept", "application/xml");
            con.setRequestProperty("Content-Language", "en-US");
            con.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(
                    con.getOutputStream());
            writer.write("");
            writer.flush();
            writer.close();

            // Get Response
            Reader reader = new InputStreamReader(con.getInputStream());

            Gson gson = new GsonBuilder().create();
            token = gson.fromJson(reader, Token.class);
            reader.close();
            return token;

        } catch (Exception e) {
            token.appenedError("\n" + serviceName + " : " + e.getMessage());
        }

        return token;
    }
}
