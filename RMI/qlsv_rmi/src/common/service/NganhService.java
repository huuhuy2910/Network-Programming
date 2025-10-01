package common.service;

import common.dto.Nganh;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NganhService extends Remote {
    List<Nganh> getAll() throws RemoteException;

    Nganh getById(String id) throws RemoteException;

    boolean insert(Nganh nganh) throws RemoteException;

    boolean update(Nganh nganh) throws RemoteException;

    boolean delete(String id) throws RemoteException;

    List<Nganh> search(String keyword) throws RemoteException;
}
