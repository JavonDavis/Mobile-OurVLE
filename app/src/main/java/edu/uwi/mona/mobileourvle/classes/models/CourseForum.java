package edu.uwi.mona.mobileourvle.classes.models;

import android.text.Html;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/8/15.
 */
public class CourseForum extends SugarRecord<CourseForum> {

    public CourseForum()
    {

    }
    // since id is a reserved field in SugarRecord
    @SerializedName("id")
    int forumid;

    @SerializedName("course")
    String courseid;

    @SerializedName("name")
    String name;

    @SerializedName("intro")
    String intro;

    @SerializedName("completiondiscussions")
    int completiondiscussions;

    @SerializedName("completionreplies")
    int completionreplies;

    @SerializedName("completionposts")
    int completionposts;

    @SerializedName("cmid")
    int cmid;

    // Errors. Not to be stored in sql db.
    @Ignore
    @SerializedName("exception")
    String exception;

    @Ignore
    @SerializedName("errorcode")
    String errorcode;

    @Ignore
    @SerializedName("message")
    String message;

    @Ignore
    @SerializedName("debuginfo")
    String debuginfo;

    // Relational and other fields
    long siteid;
    String coursename;

    /**
     * Get coursename of the forum
     *
     * @return
     */
    public String getCoursename() {
        return coursename;
    }

    /**
     * Set coursename of the forum
     *
     * @param coursename
     */
    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    /**
     * Get Forum id
     *
     * @return
     */
    public int getForumid() {
        return forumid;
    }

    /**
     * Get The forum type
     *
     * @return
     */
    public String getCourseid() {
        return courseid;
    }

    /**
     * Get Forum name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Get Forum intro
     *
     * @return
     */
    public String getIntro() {
        return Html.fromHtml(intro).toString();
    }

    /**
     * Get Student must create discussions
     *
     * @return
     */
    public int getCompletiondiscussions() {
        return completiondiscussions;
    }

    /**
     * Get Student must post replies
     *
     * @return
     */
    public int getCompletionreplies() {
        return completionreplies;
    }

    /**
     * Get Student must post discussions or replies
     *
     * @return
     */
    public int getCompletionposts() {
        return completionposts;
    }

    /**
     * Get Course module id
     *
     * @return
     */
    public int getCmid() {
        return cmid;
    }

    /**
     * Exception occurred while retrieving
     *
     * @return
     */
    public String getException() {
        return exception;
    }

    /**
     * Errorcode of error occurred while retrieving
     *
     * @return
     */
    public String getErrorcode() {
        return errorcode;
    }

    /**
     * Message of error occurred while retrieving
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Debug info on the error occurred
     *
     * @return
     */
    public String getDebuginfo() {
        return debuginfo;
    }
}
