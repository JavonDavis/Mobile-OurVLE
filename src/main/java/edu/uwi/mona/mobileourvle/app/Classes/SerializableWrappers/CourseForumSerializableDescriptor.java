/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.SerializableWrappers;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;
import org.sourceforge.ah.android.utilities.Widgets.ObjectSerializer.ObjectSerializerDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;

/**
 * @author Aston Hamilton
 */
public class CourseForumSerializableDescriptor extends
        ObjectSerializerDescriptor<CourseForum> {

    @Override
    public JsonElement serialzeObject(final CourseForum unserializedObj) {
        final JsonObject serializedForum = new JsonObject();
        serializedForum.addProperty("id", unserializedObj.getForumid());
        serializedForum.addProperty("name", unserializedObj.getName());
        serializedForum.addProperty("intro", unserializedObj.getIntro());
        serializedForum.addProperty("modified",
                                    DateFormatter.getISODateString(unserializedObj
                                                                           .getLastModified()));
        serializedForum.addProperty("cmid", unserializedObj.getModuleId());

        return serializedForum;
    }

    @Override
    public CourseForum deserialzeObject(final JsonElement serializedObject) {
        final JsonObject serializedObjectObj = (JsonObject) serializedObject;
        final Long forumId = serializedObjectObj.get("id").getAsLong();
        final String name = serializedObjectObj.get("name").getAsString();
        final String intro = serializedObjectObj.get("intro").getAsString();
        final String lastModifiedString = serializedObjectObj.get("modified")
                                                             .getAsString();

        final String pModuleId = serializedObjectObj.get("cmid").getAsString();
        final DateTime lastModified = DateFormatter
                .getDateTimeFromISOString(lastModifiedString);
        return new CourseForum(forumId, pModuleId, name, intro, lastModified);
    }
}
