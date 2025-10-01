package common.service;

import common.dto.TaiKhoan;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface TaiKhoanService extends Remote {
    List<TaiKhoan> getAll() throws RemoteException;

    TaiKhoan getByUsername(String username) throws RemoteException;

    boolean insert(TaiKhoan taiKhoan) throws RemoteException;

    boolean update(TaiKhoan taiKhoan) throws RemoteException;

    boolean delete(String username) throws RemoteException;

    List<TaiKhoan> search(String keyword) throws RemoteException;

    TaiKhoan login(String username, String password) throws RemoteException;

    boolean changePassword(String username, String oldPassword, String newPassword) throws RemoteException;

    boolean resetPassword(String username, String newPassword) throws RemoteException;
}
