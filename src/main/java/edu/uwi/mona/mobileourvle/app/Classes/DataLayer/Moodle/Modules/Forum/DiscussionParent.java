package edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.CourseModule;

/* ================ Helper class ================= */
public class DiscussionParent {
    private final CourseModule module;
    private final CourseForum forum;

    public DiscussionParent(final CourseModule parentModule) {
        module = parentModule;
        forum = null;
    }

    public DiscussionParent(final CourseForum parentForum) {
        module = null;
        forum = parentForum;
    }

    public boolean isForum() {
        return forum != null;
    }

    public boolean isModule() {
        return module != null;
    }

    public CourseForum getForum() {
        return forum;
    }

    public CourseModule getModule() {
        return module;
    }
}