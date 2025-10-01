package server.serviceimpl;

import common.dto.DashboardStats;
import common.dto.SinhVien;
import common.service.SinhVienService;
import server.dao.SinhVienDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SinhVienServiceImpl extends UnicastRemoteObject implements SinhVienService {

    private final SinhVienDAO dao;

    public SinhVienServiceImpl() throws RemoteException {
        super();
        this.dao = new SinhVienDAO();
    }

    @Override
    public List<SinhVien> getAll() throws RemoteException {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public SinhVien getById(String id) throws RemoteException {
        try {
            return dao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(SinhVien sv) throws RemoteException {
        try {
            return dao.insert(sv);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(SinhVien sv) throws RemoteException {
        try {
            return dao.update(sv);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        try {
            return dao.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<SinhVien> search(String keyword) throws RemoteException {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public DashboardStats getDashboardStats(String khoaId, String nganhId, String khoaHocId) throws RemoteException {
        try {
            return dao.getDashboardStats(khoaId, nganhId, khoaHocId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new DashboardStats();
        }
    }
}
