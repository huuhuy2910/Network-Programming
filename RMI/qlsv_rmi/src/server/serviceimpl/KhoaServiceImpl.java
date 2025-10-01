package server.serviceimpl;

import common.dto.Khoa;
import common.service.KhoaService;
import server.dao.KhoaDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhoaServiceImpl extends UnicastRemoteObject implements KhoaService {

    private final KhoaDAO dao;

    public KhoaServiceImpl() throws RemoteException {
        super();
        this.dao = new KhoaDAO();
    }

    @Override
    public List<Khoa> getAll() throws RemoteException {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Khoa getById(String id) throws RemoteException {
        try {
            return dao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(Khoa khoa) throws RemoteException {
        try {
            return dao.insert(khoa);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Khoa khoa) throws RemoteException {
        try {
            return dao.update(khoa);
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
    public List<Khoa> search(String keyword) throws RemoteException {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
