package com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum;

import org.joda.time.DateTime;

import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 * 
 */
public class ForumDiscussion {
    private final long id;
    private final String name;
    private final MoodleUser creator;
    private final DateTime lastModified;

    private final DiscussionParent parent;

    /**
     * @param id
     * @param name
     * @param creator
     * @param lastModified
     */
    public ForumDiscussion(final Long id, final String name,
	    final MoodleUser creator,
	    final DateTime lastModified, final DiscussionParent parent) {
	super();
	this.id = id;
	this.name = name;
	this.creator = creator;
	this.lastModified = lastModified;
	this.parent = parent;
    }

    /**
     * @return the id
     */
    public Long getId() {
	return id;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @return the creator
     */
    public MoodleUser getCreator() {
	return creator;
    }

    /**
     * @return the lastModified
     */
    public DateTime getLastModified() {
	return lastModified;
    }

    public DiscussionParent getParent() {
	return parent;
    }

}
