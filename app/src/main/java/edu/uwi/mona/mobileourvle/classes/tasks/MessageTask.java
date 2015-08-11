package edu.uwi.mona.mobileourvle.classes.tasks;

import java.util.List;

import edu.uwi.mona.mobileourvle.classes.models.Message;
import edu.uwi.mona.mobileourvle.classes.models.Messages;
import edu.uwi.mona.mobileourvle.classes.models.SiteInfo;
import edu.uwi.mona.mobileourvle.classes.moodle.MoodleFunctions;
import edu.uwi.mona.mobileourvle.classes.moodle.MoodleRestMessage;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 8/4/15.
 */
public class MessageTask {

    String mUrl;
    String token;

    String error;

    /**
     *
     * @param token
     * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
     */
    public MessageTask(String token) {
        this.mUrl = MoodleFunctions.API_HOST;
        this.token = token;
    }


    /**
     * Sync all messages sent / received by a user (current user typically)
     *
     * @param userid
     *            userid of the current site user.
     * @return syncStatus
     *
     */
    public Boolean syncMessages(int userid) {
        MoodleRestMessage mrm = new MoodleRestMessage(mUrl, token);
        return saveMessages(mrm.getMessages(userid, 0, 0))
                && saveMessages(mrm.getMessages(userid, 0, 1))
                && saveMessages(mrm.getMessages(0, userid, 0))
                && saveMessages(mrm.getMessages(0, userid, 1));
    }

    private Boolean saveMessages(Messages Messages) {

        /** Error checking **/
        // Some network or encoding issue.
        if (Messages == null) {
            error = "Network issue!";
            return false;
        }

        // Moodle exception
        if (Messages.getErrorcode() != null) {
            error = Messages.getErrorcode();
            // No additional debug info as that needs context
            return false;
        }

        List<Message> mMessages = Messages.getMessages();
        // Warnings are not being handled
        List<Message> dbMessages;
        Message message = new Message();

        // Get site info - used for notification setting
        SiteInfo site = SiteInfo.listAll(SiteInfo.class).get(0);
        int currentUserid = (site != null) ? site.getUserid() : 0;

        if (mMessages != null)
            for (int i = 0; i < mMessages.size(); i++) {
                message = mMessages.get(i);
				/*
				 * -TODO- Improve this search with only Sql operation
				 */
                dbMessages = Message.find(Message.class,
                        "messageid = ?", message.getMessageid()
                                + "");
                if (dbMessages.size() > 0)
                    message.setId(dbMessages.get(0).getId());
                    // set notifications if enabled
                message.save();
            }

        return true;
    }

}
