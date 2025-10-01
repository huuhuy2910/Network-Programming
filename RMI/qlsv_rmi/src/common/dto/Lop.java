package common.dto;

import java.io.Serializable;
import java.util.Objects;

public class Lop implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maLop;
    private String tenLop;
    private Nganh nganh;
    private Khoa khoa;

    public Lop() {
    }

    public Lop(String maLop, String tenLop, Nganh nganh, Khoa khoa) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.nganh = nganh;
        this.khoa = khoa;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public Nganh getNganh() {
        return nganh;
    }

    public void setNganh(Nganh nganh) {
        this.nganh = nganh;
    }

    public Khoa getKhoa() {
        return khoa;
    }

    public void setKhoa(Khoa khoa) {
        this.khoa = khoa;
    }

    @Override
    public String toString() {
        return tenLop != null ? tenLop : maLop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lop lop = (Lop) o;
        return Objects.equals(maLop, lop.maLop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maLop);
    }
}
