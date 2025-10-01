package server.serviceimpl;

import common.dto.Diem;
import common.service.DiemService;
import server.dao.DiemDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiemServiceImpl extends UnicastRemoteObject implements DiemService {

    private final DiemDAO dao;

    public DiemServiceImpl() throws RemoteException {
        super();
        this.dao = new DiemDAO();
    }

    @Override
    public List<Diem> getAll() throws RemoteException {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Diem getById(long id) throws RemoteException {
        try {
            return dao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(Diem diem) throws RemoteException {
        try {
            diem.recalculateTongKet();
            return dao.insert(diem);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Diem diem) throws RemoteException {
        try {
            diem.recalculateTongKet();
            return dao.update(diem);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(long id) throws RemoteException {
        try {
            return dao.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Diem> search(String keyword) throws RemoteException {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Diem> getBySinhVien(String svId) throws RemoteException {
        try {
            return dao.getBySinhVien(svId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public double calculateGpa(String svId) throws RemoteException {
        try {
            return dao.calculateGpa(svId);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
