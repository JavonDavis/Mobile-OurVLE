/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users;

/**
 * @author Aston Hamilton
 * 
 */
public class UserProfileField {
    private final String rawName;
    private final String cleanedName;
    private final String value;

    /**
     * @param name
     * @param value
     */
    public UserProfileField(final String rawName, final String cleanedName,
	    final String value) {
	super();
	this.rawName = rawName;
	this.cleanedName = cleanedName;
	this.value = value;
    }

    /**
     * @return the name
     */
    public String getRawName() {
	return rawName;
    }

    /**
     * @return the name
     */
    public String getCleanedName() {
	return cleanedName;
    }

    /**
     * @return the value
     */
    public String getValue() {
	return value;
    }

}
