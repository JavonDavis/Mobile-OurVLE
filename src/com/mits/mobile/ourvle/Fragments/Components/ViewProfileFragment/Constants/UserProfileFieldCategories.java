/**
 * 
 */
package com.mits.mobile.ourvle.Fragments.Components.ViewProfileFragment.Constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sourceforge.ah.android.utilities.Widgets.Adapters.CompositeArrayAdapter.Partition;

import android.provider.ContactsContract;

import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.UserProfileField;

/**
 * @author Aston Hamilton
 * 
 *         THis factory and its related components is privy to the concept of a
 *         Partition since its main purpose is to help Fragments populate a
 *         PinnedHeaderList with profilefields, organised in to categories.
 * 
 */
public final class UserProfileFieldCategories {
    public static enum Category {
	INFO, PHONE, EMAIL, SOCIAL, OTHER,
	GROUPS, ROLES, PERFERENCES, COURSES
    };

    private static final Map<String, Integer> mIMMap;
    static {
	final HashMap<String, Integer> aMap = new HashMap<String, Integer>();
	aMap.put("icq", ContactsContract.CommonDataKinds.Im.PROTOCOL_ICQ);
	aMap.put("skype", ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE);
	aMap.put("yahoo", ContactsContract.CommonDataKinds.Im.PROTOCOL_YAHOO);
	aMap.put("aim", ContactsContract.CommonDataKinds.Im.PROTOCOL_AIM);
	aMap.put("msn", ContactsContract.CommonDataKinds.Im.PROTOCOL_MSN);

	mIMMap = Collections.unmodifiableMap(aMap);
    }

    private static final Map<String, Integer> mIconMap;
    static {
	final HashMap<String, Integer> aMap = new HashMap<String, Integer>();
	aMap.put("url", R.drawable.ic_website);
	aMap.put("address", R.drawable.ic_location);
	aMap.put("city", R.drawable.ic_location);
	aMap.put("country", R.drawable.ic_location);
	aMap.put("firstaccess", R.drawable.ic_time);
	aMap.put("lastaccess", R.drawable.ic_time);
	aMap.put("idnumber", R.drawable.ic_info);
	aMap.put("department", R.drawable.ic_info);
	aMap.put("institution", R.drawable.ic_info);
	aMap.put("interests", R.drawable.ic_info);
	aMap.put("description", R.drawable.ic_info);

	mIconMap = Collections.unmodifiableMap(aMap);
    }

    /*
     * These fields were originally intended to be removed form the JSON
     * response and then the rest itereated over adding then sequentially to the
     * profilefield list but I choose to explicitly specify the fields for
     * certain groups so I no longer need this list but I'm leaveing it anyways
     * in case I need it later
     */
    public static final String[] NON_PROFILE_FIELDS = {
	    "id", "fullname", "firstname", "lastname", "username",
	    "descriptionformat"
    };

    public static final String[] INFO_PROFILE_FIELDS = {
	    "idnumber", "department", "institution", "interests",
	    "description", "address", "city", "country", "firstaccess",
	    "lastaccess", "url"
    };
    public static final String[] PHONE_PROFILE_FIELDS = {
	    "phone1", "phone2"
    };
    public static final String[] EMAIL_PROFILE_FIELDS = {
	    "email"
    };
    public static final String[] SOCIAL_PROFILE_FIELDS = {
	    "icq", "skype", "yahoo", "aim", "msn"
    };
    public static final String[] OTHER_PROFILE_FIELDS = {};

    public static final void setPartitionCategory(
	    final Partition<String, UserProfileField> partiton,
	    final Category cat) {
	partiton.setTag(cat);
    }

    public static final Category getPartitionCategory(
	    final Partition<String, UserProfileField> partiton) {
	return (Category) partiton.getTag();
    }

    public static int getImProtocol(final String imField) {
	if (UserProfileFieldCategories.mIMMap.containsKey(imField))
	    return UserProfileFieldCategories.mIMMap.get(imField);

	return ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM;
    }

    public static int getFieldIconResoource(final String field) {
	if (Arrays.asList(UserProfileFieldCategories.SOCIAL_PROFILE_FIELDS)
		.contains(field))
	    return R.drawable.chat_icon;

	if (UserProfileFieldCategories.mIconMap.containsKey(field))
	    return UserProfileFieldCategories.mIconMap.get(field);

	return R.drawable.ic_info;
    }
}
