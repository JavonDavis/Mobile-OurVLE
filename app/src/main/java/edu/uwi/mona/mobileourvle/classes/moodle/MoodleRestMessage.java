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
import edu.uwi.mona.mobileourvle.classes.models.Message;
import edu.uwi.mona.mobileourvle.classes.models.Messages;
import edu.uwi.mona.mobileourvle.classes.transport.MoodleRestCall;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 8/4/15.
 */
public class MoodleRestMessage {

    private final String DEBUG_TAG = "Moodle Rest Message";
    private String mUrl;
    private String token;
    private String error;

    public MoodleRestMessage(String mUrl, String token) {
        this.mUrl = MoodleFunctions.API_HOST;
        this.token = token;
    }

    /**
     * Sends a message to a Moodle user.
     *
     * @param message
     *            Message to send with the required params (touserid, text at
     *            the least) set
     *
     * @return status - true if success. false if failed.
     */
    public Boolean sendMessage(Message message) {
        String format = MoodleFunctions.RESPONSE_FORMAT;
        String function = MoodleFunctions.FUNCTION_SEND_MESSAGE;
        ArrayList<Message> mMessages;

        if (message == null) {
            Log.d(DEBUG_TAG, "Message not setup correctly");
            error = "Message not setup correctly. Report to developer";
            return false;
        }

        try {
            // Adding all parameters.
            String params = "" + URLEncoder.encode("", "UTF-8");

            params += "&messages[0][touserid]="
                    + URLEncoder.encode(String.valueOf(message.getTouserid()),
                    "UTF-8");
            params += "&messages[0][text]="
                    + URLEncoder.encode(message.getText(), "UTF-8");
            params += "&messages[0][textformat]="
                    + URLEncoder.encode(
                    String.valueOf(message.getTextformat()), "UTF-8");
            if (message.getClientmsgid() != null)
                params += "&messages[0][clientmsgid]="
                        + URLEncoder.encode(message.getClientmsgid(), "UTF-8");

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
            mMessages = gson.fromJson(reader,
                    new TypeToken<List<Message>>() {
                    }.getType());
            reader.close();

            if (mMessages == null)
                return false;

            if (mMessages.size() == 0)
                return false;

            Message mMessage = mMessages.get(0);
            if (mMessage.getMsgid() == -1) {
                /**
                 * Don't show error when moodle gives null error. Some issue on
                 * Moodle end. Report and wait for a fix. Till then, say sent
                 * because messages are actually being sent in this case.
                 */
                if (mMessage.getErrormessage() == null
                        || mMessage.getErrormessage().contentEquals("null"))
                    return true;
                error = mMessage.getErrormessage();
                return false;
            }

        } catch (Exception e) {
            Log.d(DEBUG_TAG, "URL encoding failed");
            error = "Check internet connection!";
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Error while sending last message.
     *
     * @return error
     */
    public String getError() {
        return error;
    }

    /**
     * Get all the messages exchanged by two users. User MUST have the required
     * permissions (user who logged into this account) or an access denied
     * exception may occur.
     *
     * @param useridto
     *            userid of the user who received these messages
     * @param useridfrom
     *            userid of the user who sent these messages
     * @param read
     *            read - only read messages. 0 - only unread messages.
     * @return Messages
     *
     * <br/>
     *         <b>Note:<b/> <br/>
     *         1. To get list of messages received by a user, set useridfrom to
     *         0 <br/>
     *         2. To get list of messages sent by a user, set useridto to 0 <br/>
     *         3. Due to Moodle API limitation you need make 2 calls - read = 0
     *         and read = 1 to get all messages.<br/>
     */
    public Messages getMessages(int useridto, int useridfrom, int read) {
        Messages mMessages = null;
        String format = MoodleFunctions.RESPONSE_FORMAT;
        String function = MoodleFunctions.FUNCTION_GET_MESSAGES;

        try {
            // Adding all parameters.
            String params = "";

            params += "&useridto="
                    + URLEncoder.encode(String.valueOf(useridto), "UTF-8");
            params += "&useridfrom="
                    + URLEncoder.encode(String.valueOf(useridfrom), "UTF-8");
            params += "&read="
                    + URLEncoder.encode(String.valueOf(read), "UTF-8");

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
            mMessages = gson.fromJson(reader, Messages.class);
            Log.d("size","break");
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "URL encoding failed");
            e.printStackTrace();
        }

        return mMessages;
    }
}
