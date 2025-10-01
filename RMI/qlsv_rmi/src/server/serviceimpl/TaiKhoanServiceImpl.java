package server.serviceimpl;

import common.dto.TaiKhoan;
import common.service.TaiKhoanService;
import server.dao.TaiKhoanDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanServiceImpl extends UnicastRemoteObject implements TaiKhoanService {

    private final TaiKhoanDAO dao;

    public TaiKhoanServiceImpl() throws RemoteException {
        super();
        this.dao = new TaiKhoanDAO();
    }

    @Override
    public List<TaiKhoan> getAll() throws RemoteException {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public TaiKhoan getByUsername(String username) throws RemoteException {
        try {
            return dao.getByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insert(TaiKhoan taiKhoan) throws RemoteException {
        try {
            return dao.insert(taiKhoan);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(TaiKhoan taiKhoan) throws RemoteException {
        try {
            return dao.update(taiKhoan);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String username) throws RemoteException {
        try {
            return dao.delete(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<TaiKhoan> search(String keyword) throws RemoteException {
        try {
            return dao.search(keyword);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public TaiKhoan login(String username, String password) throws RemoteException {
        try {
            return dao.login(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) throws RemoteException {
        try {
            return dao.changePassword(username, oldPassword, newPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean resetPassword(String username, String newPassword) throws RemoteException {
        try {
            return dao.resetPassword(username, newPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
