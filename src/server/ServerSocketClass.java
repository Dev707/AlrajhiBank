package server;

import javafx.application.Platform;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import server.database.Database;

public class ServerSocketClass extends ServerAppStarter implements Runnable {

    //Server variables
    private final int port = 1234;

    private ServerSocket server;
    private Socket clientSocket;
    private Database database;
    private ArrayList<ClientSocketHandler> clientSocketHandlerArrayList = new ArrayList<>();
    private ExecutorService pool = Executors.newWorkStealingPool();
    private ViewController viewController;

    public ServerSocketClass(ViewController viewController, Database database) {
        this.viewController = viewController;
        this.database = database;
    }

    @Override
    public void run() {
        startTheServer();
    }

    @Override
    public void stop() {
        Platform.exit();
        try {
            pool.shutdownNow();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startTheServer() {
        System.out.println("Starting the server... \n");
        try {
            server = new ServerSocket(port);
            while (true) {
                System.out.println("Searching for connections... ");
                clientSocket = server.accept();
                String clientConnectionName =  String.valueOf(clientSocket.getLocalAddress()).replaceAll("/", "") + "/" + clientSocket.getPort();
                viewController.serverRespondArea.appendText("Client with host/ip: "+clientConnectionName+" is Connected! "+getCurrentTime());
                ClientSocketHandler clientThread = new ClientSocketHandler(database, clientSocket, viewController, clientConnectionName);
                clientSocketHandlerArrayList.add(clientThread);
                pool.execute(clientThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while starting the server... ");
            stop();
        }
    }

    public String getCurrentTime() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return "[TIME] " + time.format(formatter) + "\n";
    }

    public void waitForSeconds(int s) {
        try {
            Thread.sleep(s * 1000);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

}
