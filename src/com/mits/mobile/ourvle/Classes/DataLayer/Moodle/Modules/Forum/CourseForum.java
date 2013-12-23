/**
 *
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum;

import org.joda.time.DateTime;

/**
 * @author Aston Hamilton
 */
public class CourseForum {
    private final long forumid;
    private final String moduleId;
    private final String name;
    private final String intro;
    private final DateTime lastModified;

    /**
     * @param forumid
     * @param pModuleId
     * @param name
     * @param intro
     * @param lastModified
     */
    public CourseForum(final Long forumid, final String pModuleId, final String name,
                       final String intro,
                       final DateTime lastModified) {
        super();
        this.forumid = forumid;
        moduleId = pModuleId;
        this.name = name;
        this.intro = intro;
        this.lastModified = lastModified;
    }

    /**
     * @return the forumid
     */
    public Long getForumid() {
        return forumid;
    }

    public String getModuleId() {
        return moduleId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the intro
     */
    public String getIntro() {
        return intro;
    }

    /**
     * @return the lastModified
     */
    public DateTime getLastModified() {
        return lastModified;
    }

}
