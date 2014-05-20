/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Users;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;

import java.util.Arrays;

/**
 * @author Aston Hamilton
 */
public class MoodleUserDescriptor extends JSONObjectDescriptor<MoodleUser> {

    @Override
    public JsonElement getJsonElement(final MoodleUser object) {
        final JsonObject obj = new JsonObject();

        obj.addProperty("userid", object.getId());
        obj.addProperty("userpictureurl", object.getPictureUrl());

        obj.addProperty("firstname", object.getLastName());
        obj.addProperty("lastname", object.getFirstName());

        return obj;
    }

    @Override
    public MoodleUser getObjectFromJson(final JsonElement json) {
        final JsonObject jsonObject = (JsonObject) json;
        final String id = jsonObject.get("userid").getAsString();

        final String pictureUrl = jsonObject.get("userpictureurl")
                                            .getAsString();
        final String firstName;
        final String lastName;
        if (jsonObject.has("fullname")) {
            final String[] fullNameParts = jsonObject.get("fullname").getAsString().split(" ");


            firstName = fullNameParts[0];
            if (fullNameParts.length > 1) {
                final StringBuilder lastNameBuilder = new StringBuilder((2 * (fullNameParts.length - 1)) + 1);
                lastNameBuilder.append(fullNameParts[1]);

                if (fullNameParts.length > 2) {
                    for (final String lastNamePiece : Arrays.asList(fullNameParts).subList(2,
                                                                                           fullNameParts.length)) {
                        lastNameBuilder.append(' ');
                        lastNameBuilder.append(lastNamePiece);
                    }
                }

                lastName = lastNameBuilder.toString();
            } else {
                lastName = "";
            }
        } else if (jsonObject.has("firstname") && jsonObject.has("lastname")) {
            firstName = jsonObject.get("firstname").getAsString();
            lastName = jsonObject.get("lastname").getAsString();
        } else {
            firstName = "";
            lastName = "";
        }
        return new MoodleUser(id, firstName, lastName, pictureUrl);
    }
}
