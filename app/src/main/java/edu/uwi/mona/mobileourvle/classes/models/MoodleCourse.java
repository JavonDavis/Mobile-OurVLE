package edu.uwi.mona.mobileourvle.classes.models;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;


/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/6/15.
 */
public class MoodleCourse extends SugarRecord<MoodleCourse> {

    // since id is a reserved field in SugarRecord
    @SerializedName("id")
    int courseid;

    @SerializedName("shortname")
    String shortname;


    @SerializedName("fullname")
    String fullname;

    @SerializedName("idnumber")
    String idnumber;

    @SerializedName("summary")
    String summary;

    @SerializedName("summaryformat")
    int summaryformat;

    @SerializedName("format")
    String format;

    @SerializedName("showgrades")
    int showgrades;

    @SerializedName("newsitems")
    int newsitems;

    @SerializedName("startdate")
    int startdate;

    @SerializedName("numsections")
    int numsections;

    @SerializedName("showreports")
    int showreports;

    @SerializedName("visible")
    int visible;

    @Ignore
    @SerializedName("courseformatoptions")
    ArrayList<MoodleCourseFormatOption> courseformatoptions;

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

    public int getCourseid()
    {
        return courseid;
    }

    public String getShortname() {
        return shortname;
    }

    public String getFullname() {
        return fullname;
    }
}
