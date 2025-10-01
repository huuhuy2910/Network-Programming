package common.dto;

import java.io.Serializable;
import java.util.Objects;

public class Diem implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private SinhVien sinhVien;
    private HocPhan hocPhan;
    private double diemQuaTrinh;
    private double diemThi;
    private double diemTongKet;
    private HocKy hocKy;
    private NamHoc namHoc;

    public Diem() {
    }

    public Diem(long id, SinhVien sinhVien, HocPhan hocPhan, double diemQuaTrinh, double diemThi,
                double diemTongKet, HocKy hocKy, NamHoc namHoc) {
        this.id = id;
        this.sinhVien = sinhVien;
        this.hocPhan = hocPhan;
        this.diemQuaTrinh = diemQuaTrinh;
        this.diemThi = diemThi;
        this.diemTongKet = diemTongKet;
        this.hocKy = hocKy;
        this.namHoc = namHoc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    public HocPhan getHocPhan() {
        return hocPhan;
    }

    public void setHocPhan(HocPhan hocPhan) {
        this.hocPhan = hocPhan;
    }

    public double getDiemQuaTrinh() {
        return diemQuaTrinh;
    }

    public void setDiemQuaTrinh(double diemQuaTrinh) {
        this.diemQuaTrinh = diemQuaTrinh;
    }

    public double getDiemThi() {
        return diemThi;
    }

    public void setDiemThi(double diemThi) {
        this.diemThi = diemThi;
    }

    public double getDiemTongKet() {
        return diemTongKet;
    }

    public void setDiemTongKet(double diemTongKet) {
        this.diemTongKet = diemTongKet;
    }

    public HocKy getHocKy() {
        return hocKy;
    }

    public void setHocKy(HocKy hocKy) {
        this.hocKy = hocKy;
    }

    public NamHoc getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(NamHoc namHoc) {
        this.namHoc = namHoc;
    }

    public void recalculateTongKet() {
        this.diemTongKet = 0.4 * diemQuaTrinh + 0.6 * diemThi;
    }

    @Override
    public String toString() {
        return sinhVien + " - " + hocPhan + " (" + diemTongKet + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Diem diem = (Diem) o;
        return id == diem.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
