package common.dto;

import java.io.Serializable;
import java.util.Objects;

public class DashboardClassSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private String classId;
    private String className;
    private String advisorName;
    private int studentCount;

    public DashboardClassSummary() {
    }

    public DashboardClassSummary(String classId, String className, String advisorName, int studentCount) {
        this.classId = classId;
        this.className = className;
        this.advisorName = advisorName;
        this.studentCount = studentCount;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashboardClassSummary that = (DashboardClassSummary) o;
        return Objects.equals(classId, that.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId);
    }
}
