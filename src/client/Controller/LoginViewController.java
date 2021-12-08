package client.Controller;

import bankService.Account;
import bankService.Bank;
import client.ClientAppStarter;
import client.ClientSocketClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import server.database.Database;

public class LoginViewController extends ClientAppStarter implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    public Button signInButton;
    @FXML
    public Hyperlink signUpHyperlink;
    @FXML
    public TextArea logTextArea;
    @FXML
    private TextField AuthTextField;
    @FXML
    private Button ResendCode;
    @FXML
    private Hyperlink forgotHyperlink;
    @FXML
    private Pane codePane;
    @FXML
    private Pane newPasswordPane;
    @FXML
    private PasswordField newPasswordTextField;
    @FXML
    private Button sendCodeButton;
    @FXML
    private Button verifyButton;
    @FXML
    private Label errormessage;

    private ClientSocketClass socket;
    private LoggedViewController loggedViewController;
    private RegisterViewController registerViewController;
    private boolean isAuthenticated = false;
    private String code;
    private Account find;

    public LoginViewController(ClientSocketClass socket) {
        this.socket = socket;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Window: Login");
    }

    public void signIn(ActionEvent event) throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if (username.isEmpty()) {
            usernameTextField.requestFocus();
        } else if (password.isEmpty()) {
            passwordTextField.requestFocus();
        } else {
            createSignInRequest(new Account(username, password), event);
        }
    }

    public void createSignInRequest(Account signInRequest, ActionEvent event) throws IOException {
        socket.sendObject("signIn");
        socket.sendObject(signInRequest);
        find = (Account) socket.receiveObjectData();
        if (find != null && find.getPassword() != null) {
            forgotHyperlink.setVisible(false);
            new Thread(() -> {
                code = Bank.authenticationCode(find.getPhoneNumber());
            }).start();
            moveToAuthenticateScreen(event);
        } else if (find != null && find.getPassword() == null) {
            forgotHyperlink.setVisible(true);
            passwordTextField.setText("");
            passwordTextField.requestFocus();
        } else {
            forgotHyperlink.setVisible(false);
            usernameTextField.setText("");
            passwordTextField.setText("");
            usernameTextField.requestFocus();
        }
    }

    public void switchToLoggedClientScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/LoggedAppView.fxml"));
        loggedViewController = new LoggedViewController(socket);
        loader.setController(loggedViewController);

        socket.loadController(loggedViewController);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/logged.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();

    }

    public void moveToSignUpScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/RegisterAppViewNID.fxml"));
        registerViewController = new RegisterViewController(socket);
        loader.setController(registerViewController);

        socket.loadController(registerViewController);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/register.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void moveToAuthenticateScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/LoginAppViewAuth.fxml"));
        loader.setController(this);

        socket.loadController(this);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/login.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Authenticate(ActionEvent event) throws IOException {
        AuthTextField.setText(code);
        if (AuthTextField.getText().length() == 4) {
            if (AuthTextField.getText().equalsIgnoreCase(code)) {
                if (find.getStatus().equalsIgnoreCase(Bank.AUTH) && !find.getStatus().equalsIgnoreCase(Bank.FREEZED)) {
                    find.setStatus(Bank.ACTIVE);
                    socket.sendObject("status");
                    socket.sendObject(find);
                    socket.loadLoggedClient(find);
                    try {
                        switchToLoggedClientScreen(event);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                } else if (find.getStatus().equalsIgnoreCase(Bank.ACTIVE)) {
                    socket.loadLoggedClient(find);
                    try {
                        switchToLoggedClientScreen(event);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }else{
                    // label showing that the account is freezed
                    errormessage.setTextFill(Color.web("#bf0013"));
                    errormessage.setVisible(true);
                    try {
                        switchToLoggedClientScreen(event);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            } else {
                AuthTextField.setText("");
                AuthTextField.setPromptText("Wrong code!");
                ResendCode.setVisible(true);
            }
        } else {
            AuthTextField.setText("");
            AuthTextField.requestFocus();
        }
    }

    @FXML
    public void Resend(ActionEvent event) {
        ResendCode.setVisible(false);
        new Thread(() -> {
            code = Bank.authenticationCode(find.getPhoneNumber());
        }).start();
    }

    @FXML
    public void cancel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/LoginAppView.fxml"));
        loader.setController(this);

        socket.loadController(this);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/login.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void moveToForgotScreen(ActionEvent event) throws IOException {
        forgotHyperlink.setVisible(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/LoginAppViewReset.fxml"));
        loader.setController(this);

        socket.loadController(this);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/login.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void sendCode(ActionEvent event) throws IOException {
        socket.sendObject("reset");
        Account account = new Account(usernameTextField.getText(), null);
        socket.sendObject(account);
        find = (Account) socket.receiveObjectData();
        if (find != null) {
            usernameTextField.setEditable(false);
            sendCodeButton.setVisible(false);
            codePane.setVisible(true);
            AuthTextField.requestFocus();
            new Thread(() -> {
                code = Bank.authenticationCode(find.getPhoneNumber());
            }).start();
        } else {
            usernameTextField.setText("");
            usernameTextField.requestFocus();
        }
    }

    @FXML
    public void verify(ActionEvent event) {
        if (AuthTextField.getText().equalsIgnoreCase(code)) {
            AuthTextField.setEditable(false);
            verifyButton.setVisible(false);
            newPasswordPane.setVisible(true);
            newPasswordTextField.requestFocus();
        } else {
            AuthTextField.setText("");
            AuthTextField.requestFocus();
        }
    }

    @FXML
    public void resetPassword(ActionEvent event) throws IOException {
        if (!newPasswordTextField.getText().isEmpty()) {
            find.setPassword(newPasswordTextField.getText());
            socket.sendObject("reset");
            socket.sendObject(find);
            cancel(event);
        } else {
            newPasswordTextField.setText("");
            newPasswordTextField.requestFocus();
        }
    }

}
