/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Courses;

/**
 * @author Aston Hamilton
 * 
 */
public class MoodleCourse {
    private final long id;
    private final String name;

    /**
     * @param id
     * @param name
     */
    public MoodleCourse(final Long id, final String name) {
	super();
	this.id = id;
	this.name = name;
    }

    /**
     * @return the id
     */
    public Long getId() {
	return id;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

}
