package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 */
public class SessionContext {
    private final SiteInfo siteInfo;
    private final MoodleUser curretnUser;

    /**
     * @param siteInfo
     * @param curretnUser
     */
    public SessionContext(final SiteInfo siteInfo,
                          final MoodleUser curretnUser) {
        super();
        this.siteInfo = siteInfo;
        this.curretnUser = curretnUser;
    }

    /**
     * @return the siteInfo
     */
    public SiteInfo getSiteInfo() {
        return siteInfo;
    }

    /**
     * @return the curretnUser
     */
    public MoodleUser getCurretnUser() {
        return curretnUser;
    }
}
