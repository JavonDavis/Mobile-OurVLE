package edu.uwi.mona.mobileourvle.app.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.util.EncodingUtils;
import org.sourceforge.ah.android.utilities.Cryptography.AESUtil;

import edu.uwi.mona.mobileourvle.app.R;

/**
 * @author Javon Davis
 */
public class CourseContentResourceActivity extends ActionBarActivity {

    private static String OURVLE_URL = "http://ourvle.mona.uwi.edu/login/index.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content_resource);

        final SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                LoginMainActivity.SAVED_LOGIN_PREFERENCES_NAME, Context.MODE_PRIVATE);

        final String savedEncryptionKey = preferences.getString(LoginMainActivity.ENCRYPTION_KEY, "");
        final String savedUsername = preferences.getString(LoginMainActivity.USERNAME_KEY, "");
        final String savedPassword = preferences.getString(LoginMainActivity.PASSWORD_KEY, "");


        if (!savedEncryptionKey.isEmpty() && !savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            final String username = AESUtil.decryptAESString(savedEncryptionKey, savedUsername);
            final String password = AESUtil.decryptAESString(savedEncryptionKey, savedPassword);


            WebView coursePage = (WebView) findViewById(R.id.webview);
            coursePage.setWebViewClient(new WebViewClient());

            WebSettings webSettings = coursePage.getSettings();

            /*
            Some quizes or other resources consumed might require javascript so I went ahead and enabled it
             */
            webSettings.setJavaScriptEnabled(true);

            String courseUrl = getIntent().getStringExtra("URL");

            byte[] post = EncodingUtils.getBytes("username="+username+"&password="+password, "BASE64"); // sets userdata in byte format to make post request

            try {
                coursePage.postUrl(OURVLE_URL, post);//validates the user online
                coursePage.loadUrl(courseUrl);//loads webpage
            } catch (NullPointerException e) {
                Log.e("webview error",e.toString());
            }
        }
    }

}
