package vn.edu.app.common.dto;

import java.io.Serializable;

public class ClassDTO implements Serializable {
    private String classId;
    private String className;
    private String course;    // Khóa học (K44, K45…)
    private String majorId;

    public ClassDTO() {}

    public ClassDTO(String classId, String className, String course, String majorId) {
        this.classId = classId;
        this.className = className;
        this.course = course;
        this.majorId = majorId;
    }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getMajorId() { return majorId; }
    public void setMajorId(String majorId) { this.majorId = majorId; }

    @Override
    public String toString() {
        return className + " - " + course;
    }
}
