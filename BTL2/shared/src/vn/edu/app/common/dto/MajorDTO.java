package vn.edu.app.common.dto;

import java.io.Serializable;

public class MajorDTO implements Serializable {
    private String majorId;
    private String majorName;

    public MajorDTO() {}

    public MajorDTO(String majorId, String majorName) {
        this.majorId = majorId;
        this.majorName = majorName;
    }

    public String getMajorId() { return majorId; }
    public void setMajorId(String majorId) { this.majorId = majorId; }

    public String getMajorName() { return majorName; }
    public void setMajorName(String majorName) { this.majorName = majorName; }

    @Override
    public String toString() {
        return majorName + " (" + majorId + ")";
    }
}
