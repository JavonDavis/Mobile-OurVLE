/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseEvent {
    private final long eventId;
    private final String eventName;
    private final String eventDesc;
    private final DateTime eventStart;
    private final Duration eventDuration;
    private final long userId;
    private final MoodleCourse course;

    private DateTime eventEndDateTimeCache = null;

    /**
     * @param eventId
     * @param eventName
     * @param eventDesc
     * @param eventStart
     * @param eventDuration
     * @param userId
     * @param course
     */
    public CourseEvent(final long eventId, final String eventName,
	    final String eventDesc,
	    final DateTime eventStart, final Duration eventDuration,
	    final long userId,
	    final MoodleCourse course) {
	super();
	this.eventId = eventId;
	this.eventName = eventName;
	this.eventDesc = eventDesc;
	this.eventStart = eventStart;
	this.eventDuration = eventDuration;
	this.userId = userId;
	this.course = course;
    }

    /**
     * @return the eventId
     */
    public Long getEventId() {
	return eventId;
    }

    /**
     * @return the eventName
     */
    public String getEventName() {
	return eventName;
    }

    /**
     * @return the eventDesc
     */
    public String getEventDesc() {
	return eventDesc;
    }

    /**
     * @return the eventStart
     */
    public DateTime getEventStartDateTime() {
	return eventStart;
    }

    /**
     * @return the eventDuration
     */
    public Duration getEventDuration() {
	return eventDuration;
    }

    public DateTime getEventEndDateTime() {
	if (eventEndDateTimeCache == null)
	    if (eventStart != null)
		eventEndDateTimeCache = eventStart.plus(eventDuration);

	return eventEndDateTimeCache;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
	return userId;
    }

    /**
     * @return the course
     */
    public MoodleCourse getCourse() {
	return course;
    }

}
