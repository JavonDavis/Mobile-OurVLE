/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users;

/**
 * @author Aston Hamilton
 * 
 */
public class MoodleUser {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String profilePictureUrl;

    /**
     * @param id
     * @param firstName
     * @param lastName
     * @param pictureUrl
     */
    public MoodleUser(final String id, final String firstName,
	    final String lastName,
	    final String pictureUrl) {
	super();
	this.id = id;
	this.firstName = firstName;
	this.lastName = lastName;
	profilePictureUrl = pictureUrl;
    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
	return firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
	return lastName;
    }

    /**
     * @return the pictureUrl
     */
    public String getPictureUrl() {
	return profilePictureUrl;
    }

    public String getFullName() {
	return firstName + " " + lastName;
    }
}
