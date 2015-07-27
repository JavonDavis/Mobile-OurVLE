package edu.uwi.mona.mobileourvle.classes.models;

import android.text.Html;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/8/15.
 */
public class DiscussionPost extends SugarRecord<DiscussionPost>{

    // since id is a reserved field in SugarRecord
    @SerializedName("id")
    int postid;

    @SerializedName("discussion")
    int discussionid;

    @SerializedName("parent")
    int parentid;

    @SerializedName("userid")
    int userid;

    @SerializedName("created")
    int created;

    @SerializedName("modified")
    int modified;

    @SerializedName("mailed")
    int mailed;

    @SerializedName("subject")
    String subject;

    @SerializedName("message")
    String message;


    @SerializedName("attachment")
    String attachment;

    /*
     * The below two fields - canreply and postread are indicated as int in the
     * moodle Web services API Documentation but however the json response is
     * given as Boolean for both.
     */
    @SerializedName("canreply")
    Boolean canreply;

    @SerializedName("postread")
    Boolean postread;

    @SerializedName("userfullname")
    String userfullname;

    /**
     * Post id
     *
     * @return
     */
    public int getPostid() {
        return postid;
    }

    /**
     * Discussion id
     *
     * @return
     */
    public int getDiscussionid() {
        return discussionid;
    }

    /**
     * Parent id
     *
     * @return
     */
    public int getParentid() {
        return parentid;
    }

    /**
     * User id
     *
     * @return
     */
    public int getUserid() {
        return userid;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    /**
     * Creation time
     *
     * @return
     */
    public int getCreated() {
        return created;
    }

    /**
     * Time modified
     *
     * @return
     */
    public int getModified() {
        return modified;
    }

    /**
     * Mailed?
     *
     * @return
     */
    public int getMailed() {
        return mailed;
    }

    /**
     * The post subject
     *
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     * The post message
     *
     * @return
     */
    public String getMessage() {
        return Html.fromHtml(message).toString();
    }

    /**
     * The user can reply to posts?
     *
     * @return
     */
    public Boolean getCanreply() {
        return canreply;
    }

    /**
     * The post was read
     *
     * @return
     */
    public Boolean getPostread() {
        return postread;
    }

    /**
     * Post author full name
     *
     * @return
     */
    public String getUserfullname() {
        return userfullname;
    }
}
