/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;

/**
 * @author Aston Hamilton
 */
public class SiteInfo {
    private final String name;

    private final int frontPageId;
    private CourseForum newsForum;

    private final String url;

    /**
     * @param name
     * @param frontPageId
     * @param newsForum
     * @param url
     */
    public SiteInfo(final String name, final int frontPageId,
                    final CourseForum newsForum,
                    final String url) {
        super();
        this.name = name;
        this.frontPageId = frontPageId;
        this.newsForum = newsForum;
        this.url = url;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the frontPageId
     */
    public int getFrontPageId() {
        return frontPageId;
    }

    /**
     * @return the newsForum
     */
    public CourseForum getNewsForum() {
        return newsForum;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    public void setNewsForum(final CourseForum pNewsForum) {
        newsForum = pNewsForum;
    }
}
