package vn.edu.app.common.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import vn.edu.app.common.dto.UserDTO;

public interface AuthService extends Remote {
    UserDTO login(String username, String password) throws RemoteException;
    boolean changePassword(String username, String oldPass, String newPass) throws RemoteException;
}
