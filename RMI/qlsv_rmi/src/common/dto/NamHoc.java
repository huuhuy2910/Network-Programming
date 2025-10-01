package common.dto;

import java.io.Serializable;
import java.util.Objects;

public class NamHoc implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maNamHoc;
    private String tenNamHoc;

    public NamHoc() {
    }

    public NamHoc(String maNamHoc, String tenNamHoc) {
        this.maNamHoc = maNamHoc;
        this.tenNamHoc = tenNamHoc;
    }

    public String getMaNamHoc() {
        return maNamHoc;
    }

    public void setMaNamHoc(String maNamHoc) {
        this.maNamHoc = maNamHoc;
    }

    public String getTenNamHoc() {
        return tenNamHoc;
    }

    public void setTenNamHoc(String tenNamHoc) {
        this.tenNamHoc = tenNamHoc;
    }

    @Override
    public String toString() {
        return tenNamHoc != null ? tenNamHoc : maNamHoc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamHoc namHoc = (NamHoc) o;
        return Objects.equals(maNamHoc, namHoc.maNamHoc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNamHoc);
    }
}
