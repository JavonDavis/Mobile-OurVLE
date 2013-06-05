/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseModule {
    private final long id;
    private final String label;
    private final String name;

    /**
     * @param id
     * @param label
     * @param name
     */
    public CourseModule(final long id, final String label, final String name) {
	super();
	this.id = id;
	this.label = label;
	this.name = name;
    }

    /**
     * @return the id
     */
    public Long getId() {
	return id;
    }

    /**
     * @return the label
     */
    public String getLabel() {
	return label;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

}
