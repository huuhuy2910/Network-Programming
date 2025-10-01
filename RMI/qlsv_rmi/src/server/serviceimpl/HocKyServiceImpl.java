package server.serviceimpl;

import common.dto.HocKy;
import common.service.HocKyService;
import server.dao.HocKyDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HocKyServiceImpl extends UnicastRemoteObject implements HocKyService {

    private final HocKyDAO dao;

    public HocKyServiceImpl() throws RemoteException {
        super();
        this.dao = new HocKyDAO();
    }

    @Override
    public List<HocKy> getAll() throws RemoteException {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public HocKy getById(String id) throws RemoteException {
        try {
            return dao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(HocKy hocKy) throws RemoteException {
        try {
            return dao.insert(hocKy);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(HocKy hocKy) throws RemoteException {
        try {
            return dao.update(hocKy);
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
    public List<HocKy> search(String keyword) throws RemoteException {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
