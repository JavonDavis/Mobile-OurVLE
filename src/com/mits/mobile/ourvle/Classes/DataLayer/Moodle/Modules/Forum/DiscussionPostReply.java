/**
 * 
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum;

import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 * 
 */
public class DiscussionPostReply {
    private final MoodleUser sender;
    private final DiscussionPost parent;
    private final String subject;
    private final String message;

    /**
     * @param sender
     * @param parent
     * @param subject
     * @param message
     */
    public DiscussionPostReply(final MoodleUser sender,
	    final DiscussionPost parent,
	    final String subject, final String message) {
	super();
	this.sender = sender;
	this.parent = parent;
	this.subject = subject;
	this.message = message;
    }

    /**
     * @return the sender
     */
    public MoodleUser getSender() {
	return sender;
    }

    /**
     * @return the parent
     */
    public DiscussionPost getParent() {
	return parent;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
	return subject;
    }

    /**
     * @return the message
     */
    public String getMessage() {
	return message;
    }

}
