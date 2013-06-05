package com.mits.mobile.ourvle.Fragments.Components.ViewProfileFragment.Constants;

import java.util.HashMap;

public final class UserProfileStrings {

    private static final HashMap<String, String> mLanguageReplacements = new HashMap<String, String>() {
	/**
	 * 
	 */
	private static final long serialVersionUID = -722003420142655572L;

	{
	    put("id", "ID");
	    put("username", "USERNAME");
	    put("firstname", "FIRST NAME");
	    put("lastname", "LAST NAME");
	    put("fullname", "FULL NAME");
	    put("email", "EMAIL");
	    put("idnumber", "ID NUMBER");
	    put("department", "DEPARTMENT");
	    put("institution", "INSTITUTION");
	    put("url", "URL");
	    put("interests", "INTERESTS");
	    put("description", "DESCRIPTION");
	    put("descriptionformat", "DESCRIPTION FORMAT");
	    put("firstaccess", "FIRST ACCESS");
	    put("lastaccess", "LAST ACCESS");
	    put("city", "CITY");
	    put("address", "ADDRESS");
	    put("country", "COUNTRY");
	    put("phone1", "PHONE 1");
	    put("phone2", "PHONE 2");
	    put("icq", "ICQ");
	    put("skype", "SKYPE");
	    put("yahoo", "YAHOO");
	    put("aim", "AIM");
	    put("msn", "MSN");
	}
    };

    public static String getProfileTypeString(final String type) {
	if (UserProfileStrings.mLanguageReplacements.containsKey(type))
	    return UserProfileStrings.mLanguageReplacements.get(type);

	return type;
    }
}
