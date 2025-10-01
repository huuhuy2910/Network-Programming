package common.dto;

import java.io.Serializable;
import java.util.Objects;

public class HocKy implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maHocKy;
    private String tenHocKy;

    public HocKy() {
    }

    public HocKy(String maHocKy, String tenHocKy) {
        this.maHocKy = maHocKy;
        this.tenHocKy = tenHocKy;
    }

    public String getMaHocKy() {
        return maHocKy;
    }

    public void setMaHocKy(String maHocKy) {
        this.maHocKy = maHocKy;
    }

    public String getTenHocKy() {
        return tenHocKy;
    }

    public void setTenHocKy(String tenHocKy) {
        this.tenHocKy = tenHocKy;
    }

    @Override
    public String toString() {
        return tenHocKy != null ? tenHocKy : maHocKy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HocKy hocKy = (HocKy) o;
        return Objects.equals(maHocKy, hocKy.maHocKy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHocKy);
    }
}
