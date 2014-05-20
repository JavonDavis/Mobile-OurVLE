/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses;

import java.util.List;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseSection {
    private final String name;
    private final List<CourseModule> moduleList;

    /**
     * @param name
     * @param moduleList
     */
    public CourseSection(final String name, final List<CourseModule> moduleList) {
	super();
	this.name = name;
	this.moduleList = moduleList;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @return the moduleList
     */
    public List<CourseModule> getModuleList() {
	return moduleList;
    }

    @Override
    public String toString() {
	return name;
    }
}
