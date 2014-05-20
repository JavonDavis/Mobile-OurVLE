/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Formatters.TextFormatter;

import android.text.Html;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 * 
 */
public class DiscussionPost {
    private final long id;
    private final String subject;
    private final String message;
    private final String discussionId;
    private final long parentId;
    private final DateTime dateCreaded;
    private final DateTime dateLastModified;
    private final boolean hasAttachment;
    private final MoodleUser poster;

    /**
     * @param id
     * @param subject
     * @param message
     * @param pDiscussionId
     * @param parentId
     * @param dateCreaded
     * @param dateLastModified
     * @param hasAttachment
     * @param poster
     */
    public DiscussionPost(final long id, final String subject,
                          final String message, final String pDiscussionId, final long parentId,
                          final DateTime dateCreaded, final DateTime dateLastModified,
                          final boolean hasAttachment, final MoodleUser poster) {
	super();
	this.id = id;
	this.subject = subject;
	this.message = message;
        discussionId = pDiscussionId;
        this.parentId = parentId;
	this.dateCreaded = dateCreaded;
	this.dateLastModified = dateLastModified;
	this.hasAttachment = hasAttachment;
	this.poster = poster;
    }

    /**
     * @return the id
     */
    public long getId() {
	return id;
    }

    public String getDiscussionId() {
        return discussionId;
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

    public CharSequence getFormattedMessage() {
	return TextFormatter.trimSpannedText(Html.fromHtml(message));
    }

    /**
     * @return the parentId
     */
    public long getParentId() {
	return parentId;
    }

    /**
     * @return the dateCreaded
     */
    public DateTime getDateCreaded() {
	return dateCreaded;
    }

    /**
     * @return the dateLastModified
     */
    public DateTime getDateLastModified() {
	return dateLastModified;
    }

    /**
     * @return the hasAttachment
     */
    public boolean isHasAttachment() {
	return hasAttachment;
    }

    /**
     * @return the poster
     */
    public MoodleUser getPoster() {
	return poster;
    }

}
