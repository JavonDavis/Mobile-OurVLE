package edu.uwi.mona.mobileourvle.app.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;
import org.sourceforge.ah.android.utilities.Cryptography.AESUtil;

import edu.uwi.mona.mobileourvle.app.R;

/**
 * @author Javon Davis
 */
public class CourseContentsResourceActivity extends ActionBarActivity {

    private static final String OURVLE_URL = "http://ourvle.mona.uwi.edu/login/index.php";
    private int load = -1; // variable used to decide whether to call the webview loadurl method when a page is finished loading
    private WebView coursePage;
    private String courseUrl;

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

            CookieSyncManager.createInstance(getBaseContext());
            courseUrl = getIntent().getStringExtra("URL");

            coursePage = (WebView) findViewById(R.id.webview);
            coursePage.setWebViewClient(new WebViewClient()
            {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    //Users will be notified in case there's an error (i.e. no internet connection)
                    Toast.makeText(getApplicationContext(), "Error Loading... Please Internet Connection", Toast.LENGTH_SHORT).show();
                }

                public void onPageFinished(WebView view, String url) {
                    CookieSyncManager.getInstance().sync(); // saves the cookie
                    if(load<0) {
                        coursePage.loadUrl(courseUrl);     //loads webpage
                        load = 1;
                    }
                }
            });

            WebSettings webSettings = coursePage.getSettings();
            //webSettings.setSaveFormData(false);

            /*
            Some quizes or other resources consumed might require javascript so I went ahead and enabled it
             */
            webSettings.setJavaScriptEnabled(true);

            byte[] post = EncodingUtils.getBytes("username="+username+"&password="+password, "BASE64"); // sets userdata in byte format to make post request
            try {
                coursePage.postUrl(OURVLE_URL, post);//validates the user online
            } catch (NullPointerException e) {
                Log.e("webview error",e.toString());
            }
        }
    }

}
