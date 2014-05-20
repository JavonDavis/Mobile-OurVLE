package edu.uwi.mona.mobileourvle.app.Fragments.Components.ForumDiscussionPostListFragment.EntityWrappers;

import java.util.HashMap;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;

/**
 *
 * Extended posts is like a decorator that allows the posts to maintain
 * an indentation factor that lets them know their relative position to
 * their ancestors, but uses composition to avoid recratinf the
 * properties of the posts.
 *
 * It also allows them to be sorted in the order in which they would be
 * expected to be read, based on their posted date and ancestral
 * relationships.
 *
 * That with the indentation factor is used to present them in a layout
 * that is similar to the Moodle layout. i.e. indented by message parent
 * and ordered by date created
 */
public class ExtendedDiscussionPostWrapper {
    private Integer indentationFactor;
    private final DiscussionPost post;

    /**
     * @param post
     */
    public ExtendedDiscussionPostWrapper(final DiscussionPost post) {
        super();
        indentationFactor = 0;
        this.post = post;
    }

    /**
     * @return the indentationFactor
     */
    public Integer getIndentationFactor() {
        return indentationFactor;
    }

    /**
     * @param indentationFactor the indentationFactor to set
     */
    public void setIndentationFactor(final int indentationFactor) {
        this.indentationFactor = indentationFactor;
    }

    /**
     * @return the post
     */
    public DiscussionPost getPost() {
        return post;
    }
}