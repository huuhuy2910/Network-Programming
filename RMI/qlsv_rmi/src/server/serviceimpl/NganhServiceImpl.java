package server.serviceimpl;

import common.dto.Nganh;
import common.service.NganhService;
import server.dao.NganhDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NganhServiceImpl extends UnicastRemoteObject implements NganhService {

    private final NganhDAO dao;

    public NganhServiceImpl() throws RemoteException {
        super();
        this.dao = new NganhDAO();
    }

    @Override
    public List<Nganh> getAll() throws RemoteException {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Nganh getById(String id) throws RemoteException {
        try {
            return dao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(Nganh nganh) throws RemoteException {
        try {
            return dao.insert(nganh);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Nganh nganh) throws RemoteException {
        try {
            return dao.update(nganh);
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
    public List<Nganh> search(String keyword) throws RemoteException {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
