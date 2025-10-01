package common.service;

import common.dto.NamHoc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NamHocService extends Remote {
    List<NamHoc> getAll() throws RemoteException;

    NamHoc getById(String id) throws RemoteException;

    boolean insert(NamHoc namHoc) throws RemoteException;

    boolean update(NamHoc namHoc) throws RemoteException;

    boolean delete(String id) throws RemoteException;

    List<NamHoc> search(String keyword) throws RemoteException;
}
