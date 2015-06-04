package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users;

public class CourseManager extends MoodleUser {
    private final String roleId;
    private final String roleName;

    /**
     * @param id
     * @param firstName
     * @param lastName
     * @param pictureUrl
     * @param roleId
     * @param roleName
     */
    public CourseManager(final String id, final String firstName,
	    final String lastName,
	    final String pictureUrl, final String roleId, final String roleName) {
	super(id, firstName, lastName, pictureUrl);
	this.roleId = roleId;
	this.roleName = roleName;
    }

    public CourseManager(final MoodleUser user, final String roleId,
	    final String roleName) {
	super(user.getId(), user.getFirstName(), user.getLastName(), user
		.getLastName());

	this.roleId = roleId;
	this.roleName = roleName;
    }

    /**
     * @return the roleId
     */
    public String getRoleId() {
	return roleId;
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
	return roleName;
    }

}
