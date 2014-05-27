/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Modules;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;

/**
 * @author Aston Hamilton
 */
public class CourseModuleDescriptor extends
        JSONObjectDescriptor<CourseModule> {

    String mCourseId;

    public CourseModuleDescriptor(final String pCourseId) {
        mCourseId = pCourseId;
    }

    @Override
    public JsonElement getJsonElement(final CourseModule object) {
        final JsonObject obj = new JsonObject();
        obj.addProperty("id", object.getId());
        obj.addProperty("name", object.getLabel());
        obj.addProperty("modname", object.getName());
        obj.addProperty("courseid", object.getCourseId());
        return obj;
    }

    @Override
    public CourseModule getObjectFromJson(final JsonElement json) {
        final JsonObject moduleJson = (JsonObject) json;

        final long id = moduleJson.get("id").getAsLong();
        final String label = moduleJson.get("name").getAsString();
        final String moduleName = moduleJson.get("modname").getAsString();

        if (moduleJson.has("courseid")) {
            mCourseId = moduleJson.get("courseid").getAsString();
        }

        final CourseModule m = new CourseModule(id, mCourseId, label, moduleName);
        if ("resource".equalsIgnoreCase(moduleName) && moduleJson.has("contents")) {
            final JsonArray contentList = moduleJson.get("contents").getAsJsonArray();

            final JsonObject content = contentList.get(0).getAsJsonObject();
            m.setFileName(content.get("filename").getAsString());
            m.setFileUrl(content.get("fileurl").getAsString());
        }

        if ("page".equalsIgnoreCase(moduleName) && moduleJson.has("url")) {
            m.setFileUrl(moduleJson.get("url").getAsString());
        }

        return m;
    }
}
