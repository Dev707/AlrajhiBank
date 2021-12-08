package client;

import client.Controller.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientAppStarter extends Application {

    private ClientSocketClass socket;

    //Login Controller
    private LoginViewController loginViewController;

    //Starting method loads up the fxml first and then assign the created controller with the socket param
    @Override
    public void start(Stage stage) throws IOException {
        //Starting the client socket and applying the view
        socket = new ClientSocketClass();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/LoginAppView.fxml"));
        loginViewController = new LoginViewController(socket);
        loader.setController(loginViewController);

        //Controller is
        Thread th = new Thread(socket);
        th.start();
        socket.loadController(loginViewController);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/login.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        socket.stop();
        System.out.println("Stop the Client");
    }

}
