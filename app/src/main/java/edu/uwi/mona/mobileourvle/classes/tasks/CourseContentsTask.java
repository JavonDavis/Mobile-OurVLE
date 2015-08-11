package edu.uwi.mona.mobileourvle.classes.tasks;

import java.util.ArrayList;
import java.util.List;

import edu.uwi.mona.mobileourvle.classes.models.CourseModule;
import edu.uwi.mona.mobileourvle.classes.models.CourseSection;
import edu.uwi.mona.mobileourvle.classes.models.ModuleContent;
import edu.uwi.mona.mobileourvle.classes.models.MoodleCourse;
import edu.uwi.mona.mobileourvle.classes.moodle.CourseRestContent;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 7/7/15.
 */
public class CourseContentsTask {
    String token;

    MoodleCourse course;
    String error;
    Boolean notification;
    int notificationcount;

    /**
     * @param token
     */
    public CourseContentsTask(String token) {
        this.token = token;
        this.notification = false;
        this.notificationcount = 0;
    }

    /**
     *
     * @param mUrl
     * @param token
     * @param notification
     *            If true, sets notifications for new contents
     */
    public CourseContentsTask(String mUrl, String token,
                                 Boolean notification) {
        this.token = token;
        this.notification = notification;
        this.notificationcount = 0;
    }

    /**
     * Get the notifications count. Notifications should be enabled during
     * Object instantiation.
     *
     * @return notificationcount
     */
    public int getNotificationcount() {
        return notificationcount;
    }

    /**
     * Sync all sections in a course in the current site. <br/>
     * Note: This call has network activity and so, any calls made must be from
     * a background thread
     *
     * @param courseid
     *            Moodle courseid of the course
     * @return syncStatus
     *
     */
    public Boolean syncCourseContents(int courseid) {

        // Get the course from database for all future use
        List<MoodleCourse> dbCourses = MoodleCourse.find(MoodleCourse.class,
                "courseid = ?", courseid + "");
        if (dbCourses == null || dbCourses.size() == 0) {
            error = "Course not found in database!";
            return false;
        }
        course = dbCourses.get(0);

        CourseRestContent courseContent = new CourseRestContent(token);
        ArrayList<CourseSection> mSections = courseContent.getCourseContent(courseid + "");

        /** Error checking **/
        if (mSections == null) {
            error = "Network issue!";
            return false;
        }

        // Some network or encoding issue.
        if (mSections.size() == 0) {
            error = "No data received! Permissions issue?";
            return false;
        }

        // Add relational fields to all sections and update
        CourseSection section = new CourseSection();
        List<CourseSection> dbSections;
        for (int i = 0; i < mSections.size(); i++) {
            section = mSections.get(i);
            section.setCourseid(courseid);
            section.setParentid(course.getId());

            // Update or save in database
            dbSections =CourseSection.find(CourseSection.class,
                    "sectionid = ? ",
                    section.getSectionid() + "");
            if (dbSections.size() > 0)
                section.setId(dbSections.get(0).getId()); // updates on save()
            section.save();

            // Now loop all modules in this section
            syncModules(section.getModules(), section.getId(),
                    section.getSectionid());
        }

        return true;
    }

    /**
     *
     * @param modules
     *            ArrayList of modules to be synced
     * @param sectiondbid
     *            Database id of the section to which these modules belong
     * @param sectionid
     *            Moodle sectionid of the section to which these modules belong
     */
    private void syncModules(ArrayList<CourseModule> modules, Long sectiondbid,
                             int sectionid) {

        // No modules in section
        if (modules == null)
            return;

        List<CourseModule> dbModules;

        for (CourseModule module: modules) {

            if (!("label".equalsIgnoreCase(module.getModname()) || "forum".equalsIgnoreCase(module.getModname()))) {
                module.setCourseid(course.getCourseid());
                module.setParentid(sectiondbid);
                module.setSectionid(sectionid);

                // Update or save in database
                module.save();
                int count = CourseModule.listAll(CourseModule.class).size();

                // Now loop all Module contents in this module
                syncModuleContents(module.getContents(), module.getId(),
                        module.getModuleid(), sectionid);
            }
        }

    }

    /**
     *
     * @param contents
     *            ArrayList of modulecontent to be synced
     * @param moduledbid
     *            Database id of the module to which these contents belong
     * @param moduleid
     *            Moodle moduleid of the module to which these contents belong
     * @param sectionid
     *            Moodle sectionid of the section to which these modules belong
     *
     */
    private void syncModuleContents(ArrayList<ModuleContent> contents,
                                    Long moduledbid, int moduleid, int sectionid) {

        // No modules in section
        if (contents == null)
            return;


        List<ModuleContent> dbContents;

        //This is where the assumption that the size of contents will always be 1
        for (ModuleContent content:contents) {
            content.setCourseid(course.getCourseid());
            content.setParentid(moduledbid);
            content.setSectionid(sectionid);
            content.setModuleid(moduleid);

            // Update or save in database
            dbContents = ModuleContent.find(ModuleContent.class,
                    "parentid = ?", content.getParentid() + "");
            if (dbContents.size() > 0)
                content.setId(dbContents.get(0).getId()); // updates on save()
            content.save();
        }

    }

    /**
     * Get a list of all sections is a Course. <br/>
     * Note: Depending on the contents of a course, this could take some time as
     * it runs many sql queries. It is recommended that this method is called
     * from a background thread
     *
     * @param courseid
     *            Moodle courseid of the course
     * @return List of sections
     */
    public ArrayList<CourseSection> getCourseContents(int courseid) {
        List<CourseSection> sections = CourseSection.find(CourseSection.class,
                "courseid = ?", courseid + "");

        // Add modules to sections
        List<CourseModule> dbModules;
        List<ModuleContent> dbContents;
        for (int i = 0; i < sections.size(); i++) {
            dbModules = CourseModule.find(CourseModule.class, "parentid = ?",
                    sections.get(i).getId() + "");

            // Set module contents to modules
            for (int j = 0; j < dbModules.size(); j++) {
                dbContents = ModuleContent.find(
                        ModuleContent.class, "parentid = ?", dbModules
                                .get(j).getId() + "");
                dbModules.get(j).setContents(dbContents);
            }

            sections.get(i).setModules(dbModules);
        }

        return new ArrayList<CourseSection>(sections);
    }
}
