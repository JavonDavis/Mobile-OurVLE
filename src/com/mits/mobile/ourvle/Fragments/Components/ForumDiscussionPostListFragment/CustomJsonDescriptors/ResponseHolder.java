package com.mits.mobile.ourvle.Fragments.Components.ForumDiscussionPostListFragment.CustomJsonDescriptors;

import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionContext;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;

public class ResponseHolder {
    private final DiscussionContext context;
    private final DiscussionPost[] postList;

    /**
     * @param context
     * @param postList
     */
    public ResponseHolder(final DiscussionContext context,
    	final DiscussionPost[] postList) {
        super();
        this.context = context;
        this.postList = postList;
    }

    /**
     * @return the context
     */
    public DiscussionContext getContext() {
        return context;
    }

    /**
     * @return the postList
     */
    public DiscussionPost[] getPostList() {
        return postList;
    }
}