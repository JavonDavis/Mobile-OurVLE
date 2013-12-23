/**
 *
 */
package com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules;

/**
 * @author Aston Hamilton
 */
public class CourseModule {
    private final long id;
    private final String courseId;
    private final String label;
    private final String name;

    private String fileUrl;
    private String fileName;

    /**
     * @param id
     * @param courseId
     * @param label
     * @param name
     */
    public CourseModule(final long id, final String courseId, final String label, final String name) {
        super();
        this.id = id;
        this.courseId = courseId;
        this.label = label;
        this.name = name;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    public String getCourseId() {
        return courseId;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(final String pFileUrl) {
        fileUrl = pFileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String pFileName) {
        fileName = pFileName;
    }
}
