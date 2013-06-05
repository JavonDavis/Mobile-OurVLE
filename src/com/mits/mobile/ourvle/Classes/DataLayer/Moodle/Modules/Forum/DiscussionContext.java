package com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum;

public class DiscussionContext {
    private final boolean mUserCanReply;

    /**
     * @param mUserCanReply
     */
    public DiscussionContext(final boolean mUserCanReply) {
	super();
	this.mUserCanReply = mUserCanReply;
    }

    /**
     * @return the mUserCanReply
     */
    public boolean isUserCanReply() {
	return mUserCanReply;
    }

}