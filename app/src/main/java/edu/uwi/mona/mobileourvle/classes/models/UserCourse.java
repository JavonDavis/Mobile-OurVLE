package edu.uwi.mona.mobileourvle.classes.models;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 8/3/15.
 */
public class UserCourse extends SugarRecord {

    @SerializedName("id")
    int courseid;

    @SerializedName("fullname")
    String fullname;

    @SerializedName("shortname")
    String shortname;

    // Relational fields - for less logout complexity
    long siteid;
    int userid; // Moodle userid

    /**
     * Get id of the course
     *
     * @return
     */
    public int getCourseid() {
        return courseid;
    }

    /**
     * Get course fullname
     *
     * @return
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Get course shortname
     *
     * @return
     */
    public String getShortname() {
        return shortname;
    }

    /**
     * Get siteid of this record
     *
     * @return
     */
    public long getSiteid() {
        return siteid;
    }

    /**
     * Userid of user to whom this course belongs to
     *
     * @return
     */
    public long getUserid() {
        return userid;
    }

    /**
     * Set siteid of this record
     *
     * @return
     */
    public void setSiteid(long siteid) {
        this.siteid = siteid;
    }

    /**
     * Userid of user to whom this course belongs to
     *
     * @return
     */
    public void setUserid(int userid) {
        this.userid = userid;
    }
}
