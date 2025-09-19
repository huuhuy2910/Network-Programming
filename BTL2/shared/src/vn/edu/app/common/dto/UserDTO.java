package vn.edu.app.common.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private String username;
    private String role;   // STUDENT / ADMIN
    private String studentId; // nếu role=STUDENT thì có link đến studentId

    public UserDTO() {}

    public UserDTO(String username, String role, String studentId) {
        this.username = username;
        this.role = role;
        this.studentId = studentId;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}
