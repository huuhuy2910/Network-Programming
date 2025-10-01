package common.service;

import common.dto.DashboardStats;
import common.dto.SinhVien;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SinhVienService extends Remote {
    List<SinhVien> getAll() throws RemoteException;

    SinhVien getById(String id) throws RemoteException;

    boolean insert(SinhVien sv) throws RemoteException;

    boolean update(SinhVien sv) throws RemoteException;

    boolean delete(String id) throws RemoteException;

    List<SinhVien> search(String keyword) throws RemoteException;

    DashboardStats getDashboardStats(String khoaId, String nganhId, String khoaHocId) throws RemoteException;
}
