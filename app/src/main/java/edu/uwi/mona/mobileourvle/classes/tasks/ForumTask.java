package edu.uwi.mona.mobileourvle.classes.tasks;

import java.util.ArrayList;
import java.util.List;

import edu.uwi.mona.mobileourvle.classes.models.CourseForum;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;
import edu.uwi.mona.mobileourvle.classes.moodle.CourseRestForum;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/8/15.
 */
public class ForumTask {
    String token;

    String error;
    Boolean notification;
    int notificationcount;

    /**
     *
     * @param token
     * @author Javon Davis
     */
    public ForumTask(String token) {
        this.token = token;
        this.notification = false;
        this.notificationcount = 0;
    }

    /**
     *
     * @param token
     * @param notification
     *            If true, sets notifications for new contents
     *
     * @author Javon Davis
     */
    public ForumTask(String token,Boolean notification) {
        this.token = token;
        this.notification = notification;
        this.notificationcount = 0;
    }

    /**
     * Get the notifications count. Notifications should be enabled during
     * Object instantiation.
     *
     * @return notificationcount
     */
    public int getNotificationcount() {
        return notificationcount;
    }

    /**
     * Sync all the events of a course. This will also sync user and site events
     * whose scope is outside course.
     *
     * @return syncStatus
     *
     * @author Javon Davis
     */
    public Boolean syncForums(int courseid) {
        ArrayList<String> courseids = new ArrayList<String>();
        courseids.add(courseid + "");
        return syncForums(courseids);
    }

    /**
     * Sync all the forums in the list of courses.
     *
     * @return syncStatus
     *
     * @author Javon Davis
     */
    public Boolean syncForums(ArrayList<String> courseids) {
        CourseRestForum courseRestForum = new CourseRestForum(token);
        ArrayList<CourseForum> mForums = courseRestForum.getForums(courseids);

        /** Error checking **/
        // Some network or encoding issue.
        if (mForums == null) {
            error = "Network issue!";
            return false;
        }

        // Moodle exception
        if (mForums.size() == 0) {
            error = "No data received";
            // No additional debug info as that needs context
            return false;
        }

        List<CourseForum> dbForums;
        List<MoodleCourse> dbCourses;

        for (CourseForum forum:mForums) {
			/*
			 * -TODO- Improve this search with only Sql operation
			 */
            dbForums = CourseForum.find(CourseForum.class,
                    "forumid = ?", forum.getForumid() + "");
            dbCourses = MoodleCourse.find(MoodleCourse.class,
                    "courseid = ?", forum.getCourseid() + "");
            if (dbCourses.size() > 0)
                forum.setCoursename(dbCourses.get(0).getShortname());
            if (dbForums.size() > 0)
                forum.setId(dbForums.get(0).getId());
                // set notifications if enabled
            else if (notification) {
                //TODO - notify
                notificationcount++;
            }
            forum.save();
        }

        return true;
    }
}
