/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Users;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.CourseManager;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseManagerDescriptor extends
	JSONObjectDescriptor<CourseManager> {

    @Override
    public JsonElement getJsonElement(final CourseManager arg0) {
	final JsonObject obj = (JsonObject) new MoodleUserDescriptor()
		.getJsonElement(arg0);

	obj.addProperty("roleid", arg0.getRoleId());
	obj.addProperty("rolename", arg0.getRoleName());

	return obj;
    }

    @Override
    public CourseManager getObjectFromJson(final JsonElement arg0) {
	final JsonObject jsonObject = (JsonObject) arg0;

	final MoodleUser userRecord = JSONDecoder.getObject(
		new MoodleUserDescriptor(), jsonObject);

	final String roleId = jsonObject.get("roleid").getAsString();
	final String roleName = jsonObject.get("rolename").getAsString();

	return new CourseManager(userRecord, roleId, roleName);
    }

}
