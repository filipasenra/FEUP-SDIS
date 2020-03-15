package i03;

import java.io.IOException;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientHandler {

    private String host_name; // hostname
    private String remote_object_name;

    private TypeOper oper;
    private String DNSName;
    private String ipAddress;

    RemoteInterface stub;

    public ClientHandler() {

    }

    public void connectToServer() throws IOException {
        try {
            Registry registry = LocateRegistry.getRegistry(this.host_name);
            stub = (RemoteInterface) registry.lookup(this.remote_object_name);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

    }

    public void service() throws IOException {

        try {
            // display response
            final String received = this.getRequest();
            System.out.println("Client: " + received);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getRequest() throws RemoteException {

        if (this.oper == TypeOper.REGISTER) {

            return this.stub.register(this.DNSName, this.ipAddress);

        }

        return this.stub.lookup(this.DNSName);

    }

    public boolean setAttributes(String[] args) {

        this.host_name = args[0];
        this.remote_object_name = args[1];

        if (args[2].equals("register"))
            this.oper = TypeOper.REGISTER;
        else if (args[2].equals("lookup"))
            this.oper = TypeOper.LOOKUP;
        else {
            System.out.println("Usage: <oper> must be 'register' or 'lookup'");
            return false;
        }

        if (this.oper == TypeOper.REGISTER) {

            if (args.length != 5) {
                System.out.println("Usage: Client <host_name> <remote_object_name> register <DNS name> <IP address>");
                return false;
            }

            DNSName = args[3];
            ipAddress = args[4];

        } else {

            if (args.length != 4) {
                System.out.println("Usage: Client <host_name> <remote_object_name> lookup <DNS name>");
                return false;
            }

            DNSName = args[3];

        }

        return true;

    }
}
