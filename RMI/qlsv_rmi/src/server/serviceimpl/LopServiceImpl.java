package server.serviceimpl;

import common.dto.Lop;
import common.service.LopService;
import server.dao.LopDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LopServiceImpl extends UnicastRemoteObject implements LopService {

    private final LopDAO dao;

    public LopServiceImpl() throws RemoteException {
        super();
        this.dao = new LopDAO();
    }

    @Override
    public List<Lop> getAll() throws RemoteException {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Lop getById(String id) throws RemoteException {
        try {
            return dao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(Lop lop) throws RemoteException {
        try {
            return dao.insert(lop);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Lop lop) throws RemoteException {
        try {
            return dao.update(lop);
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
    public List<Lop> search(String keyword) throws RemoteException {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
