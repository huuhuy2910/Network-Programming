package common.dto;

import java.io.Serializable;
import java.util.Objects;

public class KhoaHoc implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maKhoaHoc;
    private String tenKhoaHoc;

    public KhoaHoc() {
    }

    public KhoaHoc(String maKhoaHoc, String tenKhoaHoc) {
        this.maKhoaHoc = maKhoaHoc;
        this.tenKhoaHoc = tenKhoaHoc;
    }

    public String getMaKhoaHoc() {
        return maKhoaHoc;
    }

    public void setMaKhoaHoc(String maKhoaHoc) {
        this.maKhoaHoc = maKhoaHoc;
    }

    public String getTenKhoaHoc() {
        return tenKhoaHoc;
    }

    public void setTenKhoaHoc(String tenKhoaHoc) {
        this.tenKhoaHoc = tenKhoaHoc;
    }

    @Override
    public String toString() {
        return tenKhoaHoc != null ? tenKhoaHoc : maKhoaHoc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhoaHoc khoaHoc = (KhoaHoc) o;
        return Objects.equals(maKhoaHoc, khoaHoc.maKhoaHoc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKhoaHoc);
    }
}
