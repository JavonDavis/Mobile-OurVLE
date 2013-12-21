/**
 *
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Courses;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import android.text.Html;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.CourseEvent;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses.MoodleCourse;

/**
 * @author Aston Hamilton
 */
public class CourseEventDescriptor extends
        JSONObjectDescriptor<CourseEvent> {

    private final MoodleCourse mCourse;

    public CourseEventDescriptor(final MoodleCourse course) {
        mCourse = course;
    }

    @Override
    public JsonElement getJsonElement(final CourseEvent object) {
        return null;
    }

    @Override
    public CourseEvent getObjectFromJson(final JsonElement json) {
        final JsonObject courseSectionJson = (JsonObject) json;

        final long eventId = courseSectionJson.get("eventId").getAsLong();
        final String eventName = courseSectionJson.get("eventName")
                                                  .getAsString();

        final String eventDescHtmlString = courseSectionJson.get("eventDesc")
                                                            .getAsString();
        final String eventDesc = Html.fromHtml(eventDescHtmlString).toString()
                                     .trim();

        final long eventStartMilis = courseSectionJson.get("eventStart")
                                                      .getAsLong();
        final DateTime eventStart = new DateTime(eventStartMilis);

        final long eventDurationMilis = courseSectionJson.get("eventDuration")
                                                         .getAsLong();
        final Duration eventDuration = new Duration(eventDurationMilis);

        final long userId = courseSectionJson.get("userId").getAsLong();

        return new CourseEvent(eventId, eventName, eventDesc,
                               eventStart, eventDuration, userId, mCourse);
    }
}
