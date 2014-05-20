/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Courses;

import java.util.ArrayList;
import java.util.List;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.CourseSection;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.CourseModuleDescriptor;

/**
 * @author Aston Hamilton
 */
public class CourseSectionDescriptor extends
        JSONObjectDescriptor<CourseSection> {

    final String mCourseId;

    public CourseSectionDescriptor(final String pCourseId) {
        mCourseId = pCourseId;
    }

    @Override
    public JsonElement getJsonElement(final CourseSection object) {
        return null;
    }

    @Override
    public CourseSection getObjectFromJson(final JsonElement json) {
        final JsonObject courseSectionJson = (JsonObject) json;

        final String sectionName = courseSectionJson.get("name").getAsString();

        final JsonArray modulesJson = courseSectionJson.get("modules")
                                                       .getAsJsonArray();

        final List<CourseModule> modules = JSONDecoder.getObjectList(
                new CourseModuleDescriptor(mCourseId), modulesJson);

        final ArrayList<CourseModule> filteredModules = new ArrayList<CourseModule>(modules.size());
        for (CourseModule m : modules) {
            if ("resource".equalsIgnoreCase(m.getName())) {
                filteredModules.add(m);
            }
        }
        return new CourseSection(sectionName, filteredModules);
    }
}
