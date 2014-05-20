package edu.uwi.mona.mobileourvle.app.Fragments.Components.ViewProfileFragment.Factory;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.UserProfileField;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.ViewProfileFragment.Constants.UserProfileFieldCategories;
import edu.uwi.mona.mobileourvle.app.Fragments.Components.ViewProfileFragment.Constants.UserProfileStrings;

/**
 * @author Aston Hamilton
 * 
 */
public final class UserProfileFieldFactory {
    public static UserProfileField getUserProfile(
	    final UserProfileFieldRawMaterials rawMaterials) {
	String cleanedName = rawMaterials.getName();

	String cleanedValue = rawMaterials.getValue();

	if (Arrays.asList(UserProfileFieldCategories.SOCIAL_PROFILE_FIELDS)
		.contains(rawMaterials.getName()))
	    cleanedName = cleanedName.toUpperCase();

	if ("url".equals(cleanedName))
	    if (!cleanedValue.startsWith("http://"))
		cleanedValue = "http://" + cleanedValue;

	if ("firstaccess".equals(cleanedName)
		|| "lastaccess".equals(cleanedName)) {
	    final long unixSeconds = Long.parseLong(cleanedValue);
	    final DateTime accessDateTime = DateFormatter
		    .getDateTimeFromUnixSeconds(unixSeconds);
	    cleanedValue = DateFormatter.getShortDateTime(accessDateTime);
	}

	return new UserProfileField(
		rawMaterials.getName(),
		UserProfileStrings.getProfileTypeString(cleanedName),
		cleanedValue);
    }
}