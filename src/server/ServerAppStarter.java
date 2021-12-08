package server;

import bankService.Bank;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.database.Database;

public class ServerAppStarter extends Application {

    private Bank bank;
    private ViewController viewController;
    private Thread serverThread;
    private ServerSocketClass serverSocketClass;

    @Override
    public void start(Stage stage) throws Exception {
        Database database = new Database();
        bank = new Bank(database);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/MainAppView.fxml"));
        viewController = new ViewController(bank);
        loader.setController(viewController);

        serverSocketClass = new ServerSocketClass(viewController, database);
        serverThread = new Thread(serverSocketClass);
        serverThread.start();

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("server/css/server.css");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        serverSocketClass.stop();
        System.out.println("Stop the server");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
