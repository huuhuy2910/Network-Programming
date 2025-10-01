package common.dto;

import java.io.Serializable;
import java.util.Objects;

public class HocPhan implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maHocPhan;
    private String tenHocPhan;
    private int soTinChi;

    public HocPhan() {
    }

    public HocPhan(String maHocPhan, String tenHocPhan, int soTinChi) {
        this.maHocPhan = maHocPhan;
        this.tenHocPhan = tenHocPhan;
        this.soTinChi = soTinChi;
    }

    public String getMaHocPhan() {
        return maHocPhan;
    }

    public void setMaHocPhan(String maHocPhan) {
        this.maHocPhan = maHocPhan;
    }

    public String getTenHocPhan() {
        return tenHocPhan;
    }

    public void setTenHocPhan(String tenHocPhan) {
        this.tenHocPhan = tenHocPhan;
    }

    public int getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    @Override
    public String toString() {
        return tenHocPhan != null ? tenHocPhan : maHocPhan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HocPhan hocPhan = (HocPhan) o;
        return Objects.equals(maHocPhan, hocPhan.maHocPhan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHocPhan);
    }
}
