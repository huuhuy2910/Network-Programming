package server.serviceimpl;

import common.dto.KhoaHoc;
import common.service.KhoaHocService;
import server.dao.KhoaHocDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhoaHocServiceImpl extends UnicastRemoteObject implements KhoaHocService {

    private final KhoaHocDAO dao;

    public KhoaHocServiceImpl() throws RemoteException {
        super();
        this.dao = new KhoaHocDAO();
    }

    @Override
    public List<KhoaHoc> getAll() throws RemoteException {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public KhoaHoc getById(String id) throws RemoteException {
        try {
            return dao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(KhoaHoc khoaHoc) throws RemoteException {
        try {
            return dao.insert(khoaHoc);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(KhoaHoc khoaHoc) throws RemoteException {
        try {
            return dao.update(khoaHoc);
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
    public List<KhoaHoc> search(String keyword) throws RemoteException {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
