package edu.uwi.mona.mobileourvle.classes.tasks;

import java.util.List;

import edu.uwi.mona.mobileourvle.classes.models.CourseParticipant;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;
import edu.uwi.mona.mobileourvle.classes.moodle.CourseRestParticipant;
import edu.uwi.mona.mobileourvle.classes.moodle.MoodleFunctions;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 8/3/15.
 */
public class ParticipantTask {
    String mUrl = MoodleFunctions.API_HOST;
    String token;

    String error;
    /**
     *
     * @param token
     */
    public ParticipantTask(String token) {
        this.mUrl = mUrl;
        this.token = token;
    }

    /**
     * Sync all topics in a discussion.
     *
     * @return syncStatus
     */
    public Boolean syncUsers(int courseid) {
        CourseRestParticipant mru = new CourseRestParticipant(token);
        List<CourseParticipant> mUsers = mru.getUsers(courseid);

        /** Error checking **/
        // Some network or encoding issue.
        if (mUsers == null || mUsers.size() == 0) {
            error = "No users found!";
            return false;
        }

        List<CourseParticipant> dbUsers;
        CourseParticipant mUser;
        for (int i = 0; i < mUsers.size(); i++) {
            mUser = mUsers.get(i);
            mUser.setCourseid(courseid);

            dbUsers = CourseParticipant.find(CourseParticipant.class,
                    "userid = ? and courseid = ?",
                    mUser.getUserid() + "", courseid + "");
            if (dbUsers.size() > 0)
                mUser.setId(dbUsers.get(0).getId());

            mUser.save();
        }

        return true;
    }
}
