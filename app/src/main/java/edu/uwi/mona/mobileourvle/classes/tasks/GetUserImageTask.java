package edu.uwi.mona.mobileourvle.classes.tasks;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/8/15.
 */
public class GetUserImageTask extends AsyncTask<String,Void,Void> {

    public GetUserImageTask()
    {}


    @Override
    protected Void doInBackground(String... params) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // Make directories if required
            File f = new File(Environment.getExternalStorageDirectory()
                    + "/OurVLE/");
            f.mkdirs();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(
                    Environment.getExternalStorageDirectory() + "/OurVLE/"
                            + "profile_pic");

            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
