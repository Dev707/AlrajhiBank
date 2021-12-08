package client;

import bankService.Account;
import bankService.Customer;
import client.Controller.LoggedViewController;
import client.Controller.LoginViewController;
import client.Controller.RegisterViewController;
import javafx.application.Platform;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSocketClass extends ClientAppStarter implements Runnable {

    //port
    private final int port = 1234;
    //host ip
    private final String host = "127.0.0.1";
    //how many times a client tries to establish connection with the server
    private final int connectionTries = 5;

    //client socket made by the connecting to the server host:ip
    private Socket clientSocket;
    //to load the logged account to the logged view page
    private Customer loggedAccount;
    private boolean isSocketCreated;
    private boolean isClientConnected = false;

    //to communicate with server
    private ObjectInputStream objectReader;
    private ObjectOutputStream objectWriter;

    //to switch between controllers
    private LoggedViewController controllerLogged;
    private LoginViewController controllerLogin;
    private RegisterViewController controllerRegister;

    @Override
    public void run() {
        startClient();
    }

    //At the start of the app tries to connect to the server, if failed - closing the app
    public void startClient() {
        for (int i = 0; i < connectionTries; i++) {
            waitForSeconds(1);
            startConnection();
            if (isClientConnected) {
                break;
            }
        }
        if (!isSocketCreated) {
            System.out.println("\nCouldn't connect to the server... closing the app ");
            waitForSeconds(5);
            stop();
        }
    }

    public void startConnection() {
        try {
            clientSocket = new Socket(host, port);
            System.out.println("Connected");
            
            //to write to the server
            objectWriter = new ObjectOutputStream(clientSocket.getOutputStream());
            //to read from the server
            objectReader = new ObjectInputStream(clientSocket.getInputStream());
            //means that client is connected
            isSocketCreated = true;
            isClientConnected = true;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("No server connection... Trying again ");
        }
    }

    //FUNCTIONS WHICH LOADS UP THE CONTROLLER TO THE SOCKET WHEN SWITCHING THE WINDOW
    public void loadController(LoggedViewController loggedViewController) {
        if (loggedViewController != null) {
            controllerLogin = null;
            controllerRegister = null;
            controllerLogged = loggedViewController;
            System.out.println("Logged controller set");
        } else {
            System.out.println("No controller");
        }
    }

    public void loadController(LoginViewController loginViewController) {
        if (loginViewController != null) {
            controllerLogged = null;
            controllerRegister = null;
            controllerLogin = loginViewController;
            System.out.println("Login controller set");
        } else {
            System.out.println("No controller");
        }
    }

    public void loadController(RegisterViewController registerViewController) {
        if (registerViewController != null) {
            controllerLogged = null;
            controllerLogin = null;
            controllerRegister = registerViewController;
            System.out.println("Register controller set");
        } else {
            System.out.println("No controller");
        }
    }

    public Object receiveObjectData() {
        try {
            return objectReader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendObject(Object obj) {
        try {
            objectWriter.writeObject(obj);
            objectWriter.flush();
            objectWriter.reset();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void loadLoggedClient(Account receivedAccount) {
        loggedAccount = receivedAccount;
    }

    public Customer receiveLoggedClient() {
        return loggedAccount;
    }

    public void closeSocket() {
        try {
            System.out.println("Closing the socket ");
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (Exception e) {
            System.out.println("Client Closed Successfully");;
        }
    }

    @Override
    public void stop() {
        Platform.exit();
        System.exit(1);
    }

    public void waitForSeconds(int s) {
        try {
            Thread.sleep(s * 1000);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

}
