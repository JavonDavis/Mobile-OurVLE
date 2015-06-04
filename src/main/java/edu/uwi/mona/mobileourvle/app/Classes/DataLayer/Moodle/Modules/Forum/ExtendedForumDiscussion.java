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
public class ExtendedForumDiscussion extends ForumDiscussion {
    private final long lastPostId;
    private final String lastPortMessage;

    /**
     * @param id
     * @param name
     * @param creator
     * @param lastModified
     * @param lastPostId
     * @param lastPortMessage
     */
    public ExtendedForumDiscussion(final long id, final String name,
	    final MoodleUser creator, final DateTime lastModified,
	    final long lastPostId, final String lastPortMessage,
	    final DiscussionParent parent) {
	super(id, name, creator, lastModified, parent);
	this.lastPostId = lastPostId;
	this.lastPortMessage = lastPortMessage;
    }

    public ExtendedForumDiscussion(final ForumDiscussion discussion,
	    final int lastPostId, final String lastPortMessage,
	    final DiscussionParent parent) {
	super(discussion.getId(), discussion.getName(),
		discussion.getCreator(), discussion.getLastModified(), parent);
	this.lastPostId = lastPostId;
	this.lastPortMessage = lastPortMessage;
    }

    /**
     * @return the lastPostId
     */
    public Long getLastPostId() {
	return lastPostId;
    }

    /**
     * @return the lastPortMessage
     */
    public String getLastPortMessage() {
	return lastPortMessage;
    }

    public CharSequence getFormattedLastPostMessage() {
	return TextFormatter.trimSpannedText(
		Html.fromHtml(lastPortMessage));
    }

}
