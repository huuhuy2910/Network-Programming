package common.dto;

import java.io.Serializable;
import java.util.Objects;

public class Nganh implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maNganh;
    private String tenNganh;
    private Khoa khoa;

    public Nganh() {
    }

    public Nganh(String maNganh, String tenNganh, Khoa khoa) {
        this.maNganh = maNganh;
        this.tenNganh = tenNganh;
        this.khoa = khoa;
    }

    public String getMaNganh() {
        return maNganh;
    }

    public void setMaNganh(String maNganh) {
        this.maNganh = maNganh;
    }

    public String getTenNganh() {
        return tenNganh;
    }

    public void setTenNganh(String tenNganh) {
        this.tenNganh = tenNganh;
    }

    public Khoa getKhoa() {
        return khoa;
    }

    public void setKhoa(Khoa khoa) {
        this.khoa = khoa;
    }

    @Override
    public String toString() {
        return tenNganh != null ? tenNganh : maNganh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nganh nganh = (Nganh) o;
        return Objects.equals(maNganh, nganh.maNganh);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNganh);
    }
}
