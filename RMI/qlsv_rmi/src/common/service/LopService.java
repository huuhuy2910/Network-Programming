package common.service;

import common.dto.Lop;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LopService extends Remote {
    List<Lop> getAll() throws RemoteException;

    Lop getById(String id) throws RemoteException;

    boolean insert(Lop lop) throws RemoteException;

    boolean update(Lop lop) throws RemoteException;

    boolean delete(String id) throws RemoteException;

    List<Lop> search(String keyword) throws RemoteException;
}
