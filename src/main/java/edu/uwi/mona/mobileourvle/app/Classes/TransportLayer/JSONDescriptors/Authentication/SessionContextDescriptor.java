/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Authentication;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SessionContext;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SiteInfo;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Users.MoodleUserDescriptor;

/**
 * @author Aston Hamilton
 */
public class SessionContextDescriptor extends
        JSONObjectDescriptor<SessionContext> {

    @Override
    public JsonElement getJsonElement(final SessionContext arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SessionContext getObjectFromJson(final JsonElement arg0) {
        final JsonObject jsonObject = (JsonObject) arg0;


        final MoodleUser currentUser = (MoodleUser) JSONDecoder.getObject(
                new MoodleUserDescriptor(), jsonObject);

        final SiteInfo siteInfo = (SiteInfo) JSONDecoder.getObject(
                new SiteInfoDescriptor(), jsonObject);

        return new SessionContext(siteInfo, currentUser);
    }

}
