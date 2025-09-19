package vn.edu.app.common.dto;

import java.io.Serializable;

public class GradeDTO implements Serializable {
    private int id;
    private String studentId;
    private String courseId;
    private float score;
    private String gradeNote;

    public GradeDTO() {}

    public GradeDTO(int id, String studentId, String courseId, float score, String gradeNote) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.score = score;
        this.gradeNote = gradeNote;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public float getScore() { return score; }
    public void setScore(float score) { this.score = score; }

    public String getGradeNote() { return gradeNote; }
    public void setGradeNote(String gradeNote) { this.gradeNote = gradeNote; }
}
