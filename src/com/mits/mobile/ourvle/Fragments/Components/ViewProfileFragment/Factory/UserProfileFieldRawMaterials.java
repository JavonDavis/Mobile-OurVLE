/**
 * 
 */
package com.mits.mobile.ourvle.Fragments.Components.ViewProfileFragment.Factory;

/**
 * @author Aston Hamilton
 * 
 */
public class UserProfileFieldRawMaterials {
    private final String name;
    private final String value;

    /**
     * @param name
     * @param value
     */
    public UserProfileFieldRawMaterials(final String name, final String value) {
	super();
	this.name = name;
	this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @return the value
     */
    public String getValue() {
	return value;
    }
}
