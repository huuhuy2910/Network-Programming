package common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TaiKhoan implements Serializable {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_SINHVIEN = "SINHVIEN";

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String role;
    private String svId;
    private String displayName;
    private Date createdAt;
    private String status = "Hoạt động";

    public TaiKhoan() {
    }

    public TaiKhoan(String username, String password, String role, String svId, String displayName,
                    Date createdAt, String status) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.svId = svId;
        this.displayName = displayName;
        this.createdAt = createdAt;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSvId() {
        return svId;
    }

    public void setSvId(String svId) {
        this.svId = svId;
    }

    public String getDisplayName() {
        if (displayName == null || displayName.isBlank()) {
            return username;
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return "Hoạt động".equalsIgnoreCase(status);
    }

    public boolean isAdmin() {
        return ROLE_ADMIN.equalsIgnoreCase(role);
    }

    public boolean isSinhVien() {
        return ROLE_SINHVIEN.equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return displayName != null ? displayName : username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaiKhoan taiKhoan = (TaiKhoan) o;
        return Objects.equals(username, taiKhoan.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
