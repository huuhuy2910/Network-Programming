package vn.edu.app.common.dto;

import java.io.Serializable;

public class StudentDTO implements Serializable {
    private String studentId;
    private String fullName;
    private String gender;     // "M" hoặc "F"
    private String classId;
    private String phone;
    private String email;
    private String address;
    private String hometown;
    private String status;     // ACTIVE, GRADUATED, DROPPED, SUSPENDED

    public StudentDTO() {}

    public StudentDTO(String studentId, String fullName, String gender, String classId,
                      String phone, String email, String address, String hometown, String status) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.gender = gender;
        this.classId = classId;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.hometown = hometown;
        this.status = status;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getHometown() { return hometown; }
    public void setHometown(String hometown) { this.hometown = hometown; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return studentId + " - " + fullName;
    }
}
