/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.CourseModule;

/**
 * @author Aston Hamilton
 * 
 */
public class CourseModuleDescriptor extends
	JSONObjectDescriptor<CourseModule> {

    @Override
    public JsonElement getJsonElement(final CourseModule object) {
	final JsonObject obj = new JsonObject();
	obj.addProperty("id", object.getId());
	obj.addProperty("name", object.getLabel());
	obj.addProperty("modname", object.getName());
	return obj;
    }

    @Override
    public CourseModule getObjectFromJson(final JsonElement json) {
	final JsonObject moduleJson = (JsonObject) json;

	final long id = moduleJson.get("id").getAsLong();
	final String label = moduleJson.get("name").getAsString();
	final String moduleName = moduleJson.get("modname").getAsString();

	return new CourseModule(id, label, moduleName);
    }
}
