package vn.edu.app.common.dto;

import java.io.Serializable;

public class CourseDTO implements Serializable {
    private String courseId;
    private String courseName;
    private int credits;
    private String semester;

    public CourseDTO() {}

    public CourseDTO(String courseId, String courseName, int credits, String semester) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.semester = semester;
    }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    @Override
    public String toString() {
        return courseName + " (" + credits + " tín chỉ)";
    }
}
