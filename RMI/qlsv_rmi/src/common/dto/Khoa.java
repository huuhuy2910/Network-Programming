package common.dto;

import java.io.Serializable;
import java.util.Objects;

public class Khoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maKhoa;
    private String tenKhoa;
    public Khoa() {
    }

    public Khoa(String maKhoa, String tenKhoa) {
        this.maKhoa = maKhoa;
        this.tenKhoa = tenKhoa;
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }

    @Override
    public String toString() {
        return tenKhoa != null ? tenKhoa : maKhoa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Khoa khoa = (Khoa) o;
        return Objects.equals(maKhoa, khoa.maKhoa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKhoa);
    }
}
