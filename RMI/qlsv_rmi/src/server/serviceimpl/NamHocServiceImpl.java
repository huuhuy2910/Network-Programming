package server.serviceimpl;

import common.dto.NamHoc;
import common.service.NamHocService;
import server.dao.NamHocDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NamHocServiceImpl extends UnicastRemoteObject implements NamHocService {

    private final NamHocDAO dao;

    public NamHocServiceImpl() throws RemoteException {
        super();
        this.dao = new NamHocDAO();
    }

    @Override
    public List<NamHoc> getAll() throws RemoteException {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public NamHoc getById(String id) throws RemoteException {
        try {
            return dao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(NamHoc namHoc) throws RemoteException {
        try {
            return dao.insert(namHoc);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(NamHoc namHoc) throws RemoteException {
        try {
            return dao.update(namHoc);
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
    public List<NamHoc> search(String keyword) throws RemoteException {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
