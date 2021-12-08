package client.Controller;

import bankService.Account;
import bankService.Bank;
import javafx.scene.control.DatePicker;
import server.database.Database;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class RegisterViewController extends ClientAppStarter implements Initializable {

    @FXML
    private TextField EmailTextField;

    @FXML
    private TextField NIDTextField;

    @FXML
    public TextField AuthTextField;

    @FXML
    private TextField PhoneNumberTextField;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private TextField firstNameTextField;

    @FXML
    public Button goBackButton;

    @FXML
    public Button AuthenticateButton;

    @FXML
    public Button LaterButton;

    @FXML
    private Button ResendCode;

    @FXML
    private TextField lastNameTextField;

    @FXML
    public TextArea logTextArea;

    @FXML
    private TextField passwordTextField;

    @FXML
    public Button signUpButton;

    @FXML
    private TextField usernameTextField;

    private ClientSocketClass socket;
    private LoginViewController loginViewController;
    private Database database;
    private Account find;
    private String code;
    private String national_id;
    private boolean nextClicked = false;

    public RegisterViewController(ClientSocketClass socket) {
        try {
            this.database = new Database();
        } catch (Exception e) {
            System.out.println(e);
        }
        this.socket = socket;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Window: Register");
    }

    public void goBack(ActionEvent event) throws IOException {
        if (nextClicked) {
            nextClicked = false;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/RegisterAppViewNID.fxml"));
            loader.setController(this);

            socket.loadController(this);

            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("client/css/register.css");
            Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
            stage.setScene(scene);
            stage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/LoginAppView.fxml"));
            loginViewController = new LoginViewController(socket);
            loader.setController(loginViewController);

            socket.loadController(loginViewController);

            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("client/css/logged.css");
            Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
            stage.setScene(scene);
            stage.show();
        }
    }

    public void signUp(ActionEvent event) throws IOException {
        boolean somethingNotRight = false;
        find = database.getAccount(national_id);
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if (database.usernameExists(username).equalsIgnoreCase("")) {
            if (find == null) {
                if (firstNameTextField.getText().length() == 0) {
                    somethingNotRight = true;
                    firstNameTextField.requestFocus();
                } else if (lastNameTextField.getText().length() == 0) {
                    somethingNotRight = true;
                    lastNameTextField.requestFocus();
                } else if (dobDatePicker.getValue() == null) {
                    somethingNotRight = true;
                    dobDatePicker.requestFocus();
                } else if (PhoneNumberTextField.getText().length() == 0) {
                    somethingNotRight = true;
                    PhoneNumberTextField.requestFocus();
                } else if (EmailTextField.getText().length() == 0) {
                    somethingNotRight = true;
                    EmailTextField.requestFocus();
                } else if (usernameTextField.getText().length() == 0) {
                    somethingNotRight = true;
                    usernameTextField.requestFocus();
                } else if (passwordTextField.getText().length() == 0) {
                    somethingNotRight = true;
                    passwordTextField.requestFocus();
                } else {
                    double balance = 0.0;
                    String status = Bank.AUTH;
                    String role = Bank.CLIENT;
                    boolean isLogged = false;
                    String fname = firstNameTextField.getText();
                    String lname = lastNameTextField.getText();
                    ZoneId zi = ZoneId.systemDefault();
                    LocalDate ld = dobDatePicker.getValue();
                    Date dob = Date.from(ld.atStartOfDay(zi).toInstant());
                    String email = EmailTextField.getText();
                    String phoneNumber = PhoneNumberTextField.getText();
                    if (phoneNumber.length() != 10 && phoneNumber.charAt(0) != '0') {
                        somethingNotRight = true;
                        PhoneNumberTextField.setText("");
                        PhoneNumberTextField.requestFocus();
                    } else {
                        find = new Account(username, password, balance, status, role, isLogged, fname, lname, national_id, dob, email, phoneNumber);
                    }
                }
            } else {
                if (usernameTextField.getText().length() == 0) {
                    somethingNotRight = true;
                    usernameTextField.requestFocus();
                } else if (passwordTextField.getText().length() == 0) {
                    somethingNotRight = true;
                    passwordTextField.requestFocus();
                } else {
                    find.setUsername(username);
                    find.setPassword(password);
                    find.setStatus(Bank.AUTH);
                    find.setBalance(0.0);
                    find.setRole(Bank.CLIENT);
                }
            }
        } else {
            usernameTextField.setText("");
            usernameTextField.setPromptText("Username exists, try another username!");
            usernameTextField.requestFocus();
            somethingNotRight = true;
        }
        if (!somethingNotRight) {
            new Thread(() -> {
                code = Bank.authenticationCode(find.getPhoneNumber());
            }).start();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/RegisterAppViewAuth.fxml"));
            loader.setController(this);

            socket.loadController(this);

            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("client/css/register.css");
            Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
            stage.setScene(scene);
            stage.show();
        }
    }
    
    @FXML
    public void next(ActionEvent event) throws IOException {
        if (NIDTextField.getText().length() == 10) {
            if (NIDTextField.getText().matches("^[0-9]+$")) {
                national_id = NIDTextField.getText();
                find = database.getAccount(NIDTextField.getText());
                FXMLLoader loader = null;
                nextClicked = true;
                if (find == null) { 
                    loader = new FXMLLoader(getClass().getResource("../View/RegisterAppView.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("../View/RegisterAppViewOLDCUS.fxml"));
                }
                loader.setController(this);

                socket.loadController(this);

                Parent root = loader.load();
                Scene scene = new Scene(root);
                scene.getStylesheets().add("client/css/register.css");
                Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
                stage.setScene(scene);
                stage.show();
            } else {
                NIDTextField.setText("");
                NIDTextField.setPromptText("You must use numbers only");
            }
        } else {
            NIDTextField.setText("");
            NIDTextField.setPromptText("You must enter 10 characters");
        }
    }

    @FXML
    public void Authenticate(ActionEvent event) throws IOException {
        AuthTextField.setText(code);
        if (AuthTextField.getText().length() == 4) {
            if (AuthTextField.getText().equalsIgnoreCase(code)) {
                find.setStatus(Bank.ACTIVE);
                socket.sendObject("register");
                socket.sendObject(find);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/LoginAppView.fxml"));
                loginViewController = new LoginViewController(socket);
                loader.setController(loginViewController);

                socket.loadController(loginViewController);

                Parent root = loader.load();
                Scene scene = new Scene(root);
                scene.getStylesheets().add("client/css/logged.css");
                Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
                stage.setScene(scene);
                stage.show();
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
    void Resend(ActionEvent event) {
        ResendCode.setVisible(false);
        new Thread(() -> {
            code = Bank.authenticationCode(find.getPhoneNumber());
        }).start();
    }

    @FXML
    public void Later(ActionEvent event) throws IOException {
        socket.sendObject("register");
        socket.sendObject(find);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/LoginAppView.fxml"));
        loginViewController = new LoginViewController(socket);
        loader.setController(loginViewController);

        socket.loadController(loginViewController);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/logged.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();

    }

}
