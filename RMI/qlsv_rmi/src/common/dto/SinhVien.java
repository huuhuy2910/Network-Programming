package common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class SinhVien implements Serializable {
    private static final long serialVersionUID = 1L;

    private String svId;
    private String tenSv;
    private Date ngaySinh;
    private String gioiTinh;
    private String queQuan;
    private String sdt;
    private String email;
    private String diaChi;
    private String anh;
    private String status;
    private Double gpa;
    private String academicRank;
    private Lop lop;
    private KhoaHoc khoaHoc;
    private HocKy hocKyHienTai;
    private NamHoc namHocHienTai;
    private Date ngayTao;

    public SinhVien() {
    }

    public SinhVien(String svId, String tenSv, Date ngaySinh, String gioiTinh, String queQuan,
                     String sdt, String email, String diaChi, String anh, String status, Lop lop,
                     KhoaHoc khoaHoc, HocKy hocKyHienTai, NamHoc namHocHienTai, Date ngayTao) {
        this.svId = svId;
        this.tenSv = tenSv;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.queQuan = queQuan;
        this.sdt = sdt;
        this.email = email;
        this.diaChi = diaChi;
        this.anh = anh;
        this.status = status;
    this.gpa = null;
    this.academicRank = null;
        this.lop = lop;
        this.khoaHoc = khoaHoc;
        this.hocKyHienTai = hocKyHienTai;
        this.namHocHienTai = namHocHienTai;
        this.ngayTao = ngayTao;
    }

    public String getSvId() {
        return svId;
    }

    public void setSvId(String svId) {
        this.svId = svId;
    }

    public String getTenSv() {
        return tenSv;
    }

    public void setTenSv(String tenSv) {
        this.tenSv = tenSv;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public String getAcademicRank() {
        return academicRank;
    }

    public void setAcademicRank(String academicRank) {
        this.academicRank = academicRank;
    }

    public Lop getLop() {
        return lop;
    }

    public void setLop(Lop lop) {
        this.lop = lop;
    }

    public KhoaHoc getKhoaHoc() {
        return khoaHoc;
    }

    public void setKhoaHoc(KhoaHoc khoaHoc) {
        this.khoaHoc = khoaHoc;
    }

    public HocKy getHocKyHienTai() {
        return hocKyHienTai;
    }

    public void setHocKyHienTai(HocKy hocKyHienTai) {
        this.hocKyHienTai = hocKyHienTai;
    }

    public NamHoc getNamHocHienTai() {
        return namHocHienTai;
    }

    public void setNamHocHienTai(NamHoc namHocHienTai) {
        this.namHocHienTai = namHocHienTai;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    @Override
    public String toString() {
        return tenSv != null ? tenSv : svId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SinhVien sinhVien = (SinhVien) o;
        return Objects.equals(svId, sinhVien.svId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(svId);
    }
}
