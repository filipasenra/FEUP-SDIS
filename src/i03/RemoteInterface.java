package i03;

import java.rmi.*;

public interface RemoteInterface extends Remote {
   String register(String DNSName, String IPAddress) throws RemoteException;
   String lookup(String DNSName) throws RemoteException;
}