package i03;

import java.io.IOException;
import java.net.*;
import java.rmi.Remote;
import java.util.HashMap;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ServerHandler implements RemoteInterface{

    private HashMap<String, String> database = new HashMap<String, String>();

    static String NOT_REGISTER = "-1";
    static String NOT_FOUND = "NOT_FOUND";

    public ServerHandler() {

    }

    public String register(String DNSName, String IPAddress) {

        System.out.print("REGISTER : " + DNSName + " " + IPAddress + " : ");

        if (!database.containsKey(DNSName)) {

            database.put(DNSName, IPAddress);

            System.out.println(database.size());

            return Integer.toString(database.size());

        }

        System.out.println(NOT_REGISTER);
        return NOT_REGISTER;

    }

    public String lookup(String DNSName) {


        System.out.print("LOOKUP : " + DNSName + " : ");

        if (database.containsKey(DNSName)) {

            System.out.println(database.get(DNSName));
            return database.get(DNSName);

        }

        System.out.println(NOT_FOUND);
        return NOT_FOUND;

    }

}