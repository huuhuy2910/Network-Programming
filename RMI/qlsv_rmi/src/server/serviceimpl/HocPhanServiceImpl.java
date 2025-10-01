package server.serviceimpl;

import common.dto.HocPhan;
import common.service.HocPhanService;
import server.dao.HocPhanDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HocPhanServiceImpl extends UnicastRemoteObject implements HocPhanService {

    private final HocPhanDAO dao;

    public HocPhanServiceImpl() throws RemoteException {
        super();
        this.dao = new HocPhanDAO();
    }

    @Override
    public List<HocPhan> getAll() throws RemoteException {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public HocPhan getById(String id) throws RemoteException {
        try {
            return dao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(HocPhan hocPhan) throws RemoteException {
        try {
            return dao.insert(hocPhan);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(HocPhan hocPhan) throws RemoteException {
        try {
            return dao.update(hocPhan);
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
    public List<HocPhan> search(String keyword) throws RemoteException {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
