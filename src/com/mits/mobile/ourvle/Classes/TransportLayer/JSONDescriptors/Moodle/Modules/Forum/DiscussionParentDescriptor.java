/**
 *
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONEncoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.CourseModuleDescriptor;

/**
 * @author aston
 */
public class DiscussionParentDescriptor extends
        JSONObjectDescriptor<DiscussionParent> {

    final String mCourseId;

    public DiscussionParentDescriptor(final String pCourseId) {
        mCourseId = pCourseId;
    }

    @Override
    public JsonElement getJsonElement(final DiscussionParent object) {
        final JsonObject serializedObject = new JsonObject();

        if (object.getModule() != null) {
            serializedObject.addProperty("t", 1);
            serializedObject.add("d", JSONEncoder.getEncodedObjectElement(
                    new CourseModuleDescriptor(mCourseId),
                    object.getModule()));
        } else if (object.getForum() != null) {
            serializedObject.addProperty("t", 2);
            serializedObject.add("d", JSONEncoder.getEncodedObjectElement(
                    new CourseForumDescriptior(),
                    object.getForum()));
        }

        return serializedObject;
    }

    @Override
    public DiscussionParent getObjectFromJson(final JsonElement json) {
        final JsonObject serializedObject = (JsonObject) json;
        if (serializedObject.get("t").getAsInt() == 1)
            return new DiscussionParent(
                    JSONDecoder.getObject(
                            new CourseModuleDescriptor(mCourseId),
                            serializedObject.get("d")));
        else if (serializedObject.get("t").getAsInt() == 2)
            return new DiscussionParent(
                    JSONDecoder.getObject(
                            new CourseForumDescriptior(),
                            serializedObject.get("d")));

        throw new IllegalArgumentException("Not a valid discussion parent.");
    }

}
