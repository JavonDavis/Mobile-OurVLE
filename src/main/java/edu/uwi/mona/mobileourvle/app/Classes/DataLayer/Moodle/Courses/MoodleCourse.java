/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses;

/**
 * @author Aston Hamilton
 * 
 */
public class MoodleCourse {
    private final long id;
    private final String name;
    private final String shortName;

    /**
     * @param id
     * @param name
     * @param shortName
     */
    public MoodleCourse(final Long id, final String name, String shortName) {
	super();
	this.id = id;
	this.name = name;
        this.shortName = shortName;
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

    public String getShortName() {
        return shortName;
    }
}
