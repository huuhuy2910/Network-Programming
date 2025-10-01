package common.service;

import common.dto.KhoaHoc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface KhoaHocService extends Remote {
    List<KhoaHoc> getAll() throws RemoteException;

    KhoaHoc getById(String id) throws RemoteException;

    boolean insert(KhoaHoc khoaHoc) throws RemoteException;

    boolean update(KhoaHoc khoaHoc) throws RemoteException;

    boolean delete(String id) throws RemoteException;

    List<KhoaHoc> search(String keyword) throws RemoteException;
}
