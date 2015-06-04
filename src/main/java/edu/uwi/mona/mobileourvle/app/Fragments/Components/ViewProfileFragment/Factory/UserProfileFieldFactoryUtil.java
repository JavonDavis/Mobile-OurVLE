/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Fragments.Components.ViewProfileFragment.Factory;

import java.util.HashMap;
import java.util.Iterator;

import org.sourceforge.ah.android.utilities.Widgets.Adapters.CompositeArrayAdapter.Partition;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.UserProfileField;

/**
 * @author Aston Hamilton
 * 
 */
public class UserProfileFieldFactoryUtil {

    public static void loadFieldsIntoPartitonFromJson(
	    final JsonObject profileJsonObject,
	    final Partition<String, UserProfileField> partition,
	    final HashMap<String, UserProfileField> mProfileFieldCollection,
	    final String... fields) {
	for (final String field : fields) {
	    if (!profileJsonObject.has(field))
		continue;

	    final String value = profileJsonObject.get(field).getAsString();
	    if (value.trim().length() == 0)
		continue;

	    final String name = field;
	    final UserProfileField profileField = UserProfileFieldFactory
		    .getUserProfile(new UserProfileFieldRawMaterials(name,
			    value));

	    partition.addItem(profileField);
	    mProfileFieldCollection.put(field, profileField);
	}
    }

    public static void loadCustomFieldsToPartitionFromJson(
	    final JsonArray customFieldJsonArray,
	    final Partition<String, UserProfileField> partition,
	    final HashMap<String, UserProfileField> mProfileFieldCollection) {
	if (customFieldJsonArray == null)
	    return;
	final Iterator<JsonElement> iter = customFieldJsonArray.iterator();
	while (iter.hasNext()) {
	    final JsonObject profileJsonObjct = iter.next().getAsJsonObject();

	    final String value = profileJsonObjct.get("value").getAsString();
	    if (value.trim().length() == 0)
		continue;

	    final String rawName = profileJsonObjct.get("name").getAsString();
	    String name = rawName;
	    if (name.trim().length() == 0)
		name = "NO NAME";

	    final UserProfileField profileField = UserProfileFieldFactory
		    .getUserProfile(new UserProfileFieldRawMaterials(name,
			    value));

	    partition.addItem(profileField);
	    mProfileFieldCollection.put("custom-" + rawName, profileField);
	}
    }

    public static void loadGroupsFieldsToPartitionFromJson(
	    final JsonArray customFieldJsonArray,
	    final Partition<String, UserProfileField> partition,
	    final HashMap<String, UserProfileField> mProfileFieldCollection) {
	if (customFieldJsonArray == null)
	    return;
	final Iterator<JsonElement> iter = customFieldJsonArray.iterator();
	while (iter.hasNext()) {
	    final JsonObject profileJsonObjct = iter.next().getAsJsonObject();

	    final String value = profileJsonObjct.get("name").getAsString();
	    if (value.trim().length() == 0)
		continue;

	    final String name = "GROUP";

	    final UserProfileField profileField = UserProfileFieldFactory
		    .getUserProfile(new UserProfileFieldRawMaterials(name,
			    value));

	    partition.addItem(profileField);
	    mProfileFieldCollection.put("group-" + value, profileField);
	}
    }

    public static void loadRoleFieldsToPartitionFromJson(
	    final JsonArray customFieldJsonArray,
	    final Partition<String, UserProfileField> partition,
	    final HashMap<String, UserProfileField> mProfileFieldCollection) {
	if (customFieldJsonArray == null)
	    return;
	final Iterator<JsonElement> iter = customFieldJsonArray.iterator();
	while (iter.hasNext()) {
	    final JsonObject profileJsonObjct = iter.next().getAsJsonObject();

	    final String value = profileJsonObjct.get("name").getAsString();
	    if (value.trim().length() == 0)
		continue;

	    final String name = "ROLE";

	    final UserProfileField profileField = UserProfileFieldFactory
		    .getUserProfile(new UserProfileFieldRawMaterials(name,
			    value));

	    partition.addItem(profileField);
	    mProfileFieldCollection.put("role-" + value, profileField);
	}
    }

    public static void loadPreferenceFieldsToPartitionFromJson(
	    final JsonArray customFieldJsonArray,
	    final Partition<String, UserProfileField> partition,
	    final HashMap<String, UserProfileField> mProfileFieldCollection) {
	if (customFieldJsonArray == null)
	    return;
	final Iterator<JsonElement> iter = customFieldJsonArray.iterator();
	while (iter.hasNext()) {
	    final JsonObject profileJsonObjct = iter.next().getAsJsonObject();

	    final String value = profileJsonObjct.get("value").getAsString();
	    if (value.trim().length() == 0)
		continue;

	    String name = profileJsonObjct.get("name").getAsString()
		    .toUpperCase();
	    if (name.trim().length() == 0)
		name = "NO NAME";

	    final UserProfileField profileField = UserProfileFieldFactory
		    .getUserProfile(new UserProfileFieldRawMaterials(name,
			    value));

	    partition.addItem(profileField);
	    mProfileFieldCollection.put("pref-" + value, profileField);
	}
    }

    public static void loadCourseFieldsToPartitionFromJson(
	    final JsonArray customFieldJsonArray,
	    final Partition<String, UserProfileField> partition,
	    final HashMap<String, UserProfileField> mProfileFieldCollection) {
	if (customFieldJsonArray == null)
	    return;
	final Iterator<JsonElement> iter = customFieldJsonArray.iterator();
	while (iter.hasNext()) {
	    final JsonObject profileJsonObjct = iter.next().getAsJsonObject();

	    final String value = profileJsonObjct.get("fullname").getAsString();
	    if (value.trim().length() == 0)
		continue;

	    final String name = profileJsonObjct.get("shortname").getAsString();

	    final UserProfileField profileField = UserProfileFieldFactory
		    .getUserProfile(new UserProfileFieldRawMaterials(name,
			    value));

	    partition.addItem(profileField);
	    mProfileFieldCollection.put("course-" + name, profileField);
	}
    }
}
