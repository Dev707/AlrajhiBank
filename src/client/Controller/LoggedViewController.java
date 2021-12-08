package client.Controller;

import bankService.Account;
import bankService.Bank;
import bankService.Customer;
import bankService.TransactionHistory;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LoggedViewController extends ClientAppStarter implements Initializable {

    @FXML
    private TableView<TransactionHistory> historyTable;
    @FXML
    private TableColumn<TransactionHistory, String> amountColumn;
    @FXML
    private TableColumn<TransactionHistory, String> dateColumn;
    @FXML
    private TableColumn<TransactionHistory, String> statusColumn;
    @FXML
    private TableColumn<TransactionHistory, String> transactionColumn;
    @FXML
    private TableColumn<TransactionHistory, String> typeColumn;
    @FXML
    private TableView<Account> transferTableView;
    @FXML
    private TableColumn<Account, String> firstNameColumn;
    @FXML
    private TableColumn<Account, String> lastNameColumn;
    @FXML
    private TableColumn<Account, String> userNameColumn;
    @FXML
    private Label depositErrorLabel;
    @FXML
    private Label transferCurrentBalanceLabel;
    @FXML
    private TableView<Customer> tableViewAdmin;
    @FXML
    private TableColumn<?, ?> C1;
    @FXML
    private TableColumn<?, ?> C2;
    @FXML
    private TableColumn<?, ?> C3;
    @FXML
    private Label adminNameLabel;
    @FXML
    private Button adminPage;
    @FXML
    private Label depositCurrentBalanceLabel;
    @FXML
    private TextField enteredDepositTextField;
    @FXML
    public Button logoutButton;
    @FXML
    public TextArea logTextArea;
    @FXML
    public Label nameLabel;
    @FXML
    public Label balanceLabel;
    @FXML
    public Label connectionNameLabel;
    @FXML
    private Label completeLabel;
    @FXML
    private TextField amountTextField;
    @FXML
    private Label currentBalanceLabel;
    @FXML
    private Button goBackButton;
    @FXML
    private Button withdrawButton;
    @FXML
    private Label withdrawLabel;
    @FXML
    private Label withdrawError;
    @FXML
    private Label transErrorLabel;
    @FXML
    private Label depsitNameLabel;
    @FXML
    private Button transferButton;
    @FXML
    private Button addBeneficiaryButton;
    @FXML
    private RadioButton beneficiariesRadioButton;
    @FXML
    private RadioButton quickTransferRadioButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button editStatusButton;
    @FXML
    private TableColumn<?, ?> idAdminView;
    @FXML
    private TableColumn<?, ?> nameAdminView;
    @FXML
    private TableColumn<?, ?> noaAdminView;
    @FXML
    private TableView<Account> tableAccount;
    @FXML
    private Label returnedMessage;
    @FXML
    private Label editViewCustomerIDLabel;
    @FXML
    private Label editViewCDLabel;
    @FXML
    private Label numberOfCustomers;
    @FXML
    private TextField usernameTextFieldAdminPage;
    @FXML
    private TitledPane premField;
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textFname;
    @FXML
    private Label histBalanceLabel;
    @FXML
    private TextField textLname;
    @FXML
    private TextField textNationalID;
    @FXML
    private TextField textPhoneNum;
    @FXML
    private RadioButton AdminRadioButton;
    @FXML
    private RadioButton ClientRadioButton;
    @FXML
    private RadioButton FreezeRadioButton;
    @FXML
    private Button langB;
    @FXML
    private Button TransferButton;
    @FXML
    private Button depositButton;
    @FXML
    private Button transactionHistoryButton;
    @FXML
    private Label Client;
    @FXML
    private Label clientBalance;
    @FXML
    private Label title;
    @FXML
    private DatePicker textDob;
    @FXML
    private RadioButton ActiveRadioButton;
    private Account loggedAccount;
    private ClientSocketClass socket;
    Connection con;
    PreparedStatement stmt;
    private LoginViewController loginViewController;
    private ArrayList<Account> allAccounts;
    private ArrayList<Customer> allCustomers;

    public LoggedViewController(ClientSocketClass socket) {
        this.socket = socket;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedAccount = (Account) socket.receiveLoggedClient();
        nameLabel.setText(loggedAccount.getFirstName() + "  " + loggedAccount.getLastName());
        balanceLabel.setText(loggedAccount.getBalance() + "");
        if (!loggedAccount.getRole().equalsIgnoreCase(Bank.ADMIN)) {
            TransferButton.setLayoutX(depositButton.getLayoutX());
            adminPage.setVisible(false);
        }
    }

    public void goBack(ActionEvent event) throws IOException {
        socket.loadLoggedClient(null);
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

    public void switchToWithDrawScreen(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/WithdrawAppView.fxml"));
        loader.setController(this);
        socket.loadController(this);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/logged.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        currentBalanceLabel.setText(loggedAccount.getBalance() + "");
        stage.show();

    }

    public void withdraw(ActionEvent event) throws IOException {

        try {
            currentBalanceLabel.setText(loggedAccount.getBalance() + "");
            double amount = Double.parseDouble(amountTextField.getText());
            double balance = loggedAccount.getBalance();
            if (amount <= balance) {
                double updatedBalance = balance - amount;
                String format = String.format("%.2f", updatedBalance);
                loggedAccount.setBalance(Double.parseDouble(format));
                socket.sendObject("withdraw");
                socket.sendObject(amount + "");
                socket.sendObject("Accepted");
                socket.sendObject(loggedAccount);

                withdrawLabel.setVisible(false);
                amountTextField.setVisible(false);
                withdrawButton.setVisible(false);
                goBackButton.setVisible(false);
                completeLabel.setVisible(true);
                loggedClientScreen(event);
            } else {
                socket.sendObject("withdraw");
                socket.sendObject(amount + "");
                socket.sendObject("Rejected");
                socket.sendObject(loggedAccount);
                withdrawError.setTextFill(Color.web("#bf0013"));
                withdrawError.setVisible(true);
            }

        } catch (Exception e) {

            e.printStackTrace();
            e.getLocalizedMessage();
        }
    }

    @FXML
    void lang(ActionEvent event) {
        String language;
        String country;
        if (langB.getText().equalsIgnoreCase("EN")) {
            language = "en";
            country = "US";
            Client.setLayoutX(27);
            Client.setAlignment(Pos.BASELINE_RIGHT);
            nameLabel.setLayoutX(311);
            nameLabel.setAlignment(Pos.BASELINE_LEFT);
            clientBalance.setLayoutX(27);
            clientBalance.setAlignment(Pos.BASELINE_RIGHT);
            balanceLabel.setLayoutX(311);
            balanceLabel.setAlignment(Pos.BASELINE_LEFT);
            langB.setText("ع");
        } else {
            language = "ar";
            country = "SA";
            Client.setLayoutX(593);
            Client.setAlignment(Pos.BASELINE_LEFT);
            nameLabel.setLayoutX(110);
            nameLabel.setAlignment(Pos.BASELINE_RIGHT);
            clientBalance.setLayoutX(593);
            clientBalance.setAlignment(Pos.BASELINE_LEFT);
            balanceLabel.setLayoutX(110);
            balanceLabel.setAlignment(Pos.BASELINE_RIGHT);
            langB.setText("EN");
        }

        Locale currentLocale;
        ResourceBundle messages;
        currentLocale = new Locale(language, country);
        messages = ResourceBundle.getBundle("client/resource/MessagesBundle", currentLocale);
        depositButton.setText(messages.getString("deposit"));
        adminPage.setText(messages.getString("adminpanel"));
        transactionHistoryButton.setText(messages.getString("transactionHistory"));
        TransferButton.setText(messages.getString("transfer"));
        Client.setText(messages.getString(Bank.CLIENT));
        clientBalance.setText(messages.getString("clientBalance"));
        withdrawButton.setText(messages.getString("withdraw"));
        title.setText(messages.getString("banktitle"));
        logoutButton.setText(messages.getString("logout"));
    }

    public void switchToAdminScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AdminAppView.fxml"));
        loader.setController(this);

        socket.loadController(this);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/logged.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        adminNameLabel.setText(loggedAccount.getFirstName() + "  " + loggedAccount.getLastName());
        socket.sendObject("AllCustomers");
        allCustomers = (ArrayList<Customer>) socket.receiveObjectData();
        ObservableList<Customer> observableList = FXCollections.observableArrayList(allCustomers);
        idAdminView.setCellValueFactory(new PropertyValueFactory<>("cusID"));
        nameAdminView.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        noaAdminView.setCellValueFactory(new PropertyValueFactory<>("noa"));

        tableViewAdmin.setItems(observableList);
        tableViewAdmin.setOnMouseClicked(e -> {
            selectCustomer();
        });
        getNumOFAccAndCus();
        stage.show();
    }

    public void getNumOFAccAndCus() throws IOException {
        socket.sendObject("getNumbers");
        String num2 = (String) socket.receiveObjectData();
        numberOfCustomers.setText(num2);
    }

    public void editStatusMethod(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/EditAccount.fxml"));
        loader.setController(this);

        socket.loadController(this);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/logged.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        String id = usernameTextFieldAdminPage.getText();
        Customer c = getCustomer(id);
        editViewCustomerIDLabel.setText(id);

        textEmail.setText(c.getEmail());
        textFname.setText(c.getFirstName());
        textLname.setText(c.getLastName());
        textNationalID.setText(c.getNationalID());
        textPhoneNum.setText(c.getPhoneNumber());
        Date date = c.getDob();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        String stringDate = DateFor.format(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(stringDate, formatter);
        textDob.setValue(localDate);
        socket.sendObject("AllAccounts");
        socket.sendObject(id);
        allAccounts = (ArrayList<Account>) socket.receiveObjectData();
        ObservableList<Account> observableList = FXCollections.observableArrayList(allAccounts);
        C1.setCellValueFactory(new PropertyValueFactory<>("id"));
        C2.setCellValueFactory(new PropertyValueFactory<>("username"));
        C3.setCellValueFactory(new PropertyValueFactory<>("create_date"));

        tableAccount.setItems(observableList);
        tableAccount.setOnMouseClicked(e -> {
            selectAccount();
        });
        stage.show();
    }

    public Customer getCustomer(String id) {
        int i = 0;
        int t = Integer.parseInt(id);
        for (Customer n : allCustomers) {
            if (n.getCusID() == t) {
                break;
            }
            i++;
        }
        return allCustomers.get(i);
    }

    public void selectAccount() {

        for (Account selectedAccount : tableAccount.getSelectionModel().getSelectedItems()) {

            if (selectedAccount.getId() == loggedAccount.getId()) {
                premField.setVisible(false);
                returnedMessage.setText("You can't edit your account!");
                returnedMessage.setTextFill(Color.web("#bf0013"));
                returnedMessage.setVisible(true);
            } else if (selectedAccount.getId() != loggedAccount.getId()) {
                premField.setVisible(true);
                returnedMessage.setVisible(false);
                if (selectedAccount.getStatus().equalsIgnoreCase(Bank.ACTIVE)) {
                    ActiveRadioButton.setSelected(true);
                    FreezeRadioButton.setSelected(false);
                } else {
                    FreezeRadioButton.setSelected(true);
                    ActiveRadioButton.setSelected(false);
                }
                if (selectedAccount.getRole().equalsIgnoreCase(Bank.ADMIN)) {
                    AdminRadioButton.setSelected(true);
                    ClientRadioButton.setSelected(false);
                } else {
                    AdminRadioButton.setSelected(false);
                    ClientRadioButton.setSelected(true);
                }
            } else {
                returnedMessage.setText("You can't edit your account!");
                returnedMessage.setTextFill(Color.web("#bf0013"));
                returnedMessage.setVisible(true);
            }
        }

    }

    public void saveEditedAccount(ActionEvent event) throws IOException {
        Account selectedAccount = null;

        for (Account selectedItem : tableAccount.getSelectionModel().getSelectedItems()) {
            selectedAccount = selectedItem;
        }

        ZoneId zi = ZoneId.systemDefault();
        LocalDate ld = textDob.getValue();
        Date dob = Date.from(ld.atStartOfDay(zi).toInstant());
        socket.sendObject("editCustomer");
        socket.sendObject(usernameTextFieldAdminPage.getText());
        socket.sendObject(textFname.getText());
        socket.sendObject(textLname.getText());
        socket.sendObject(textEmail.getText());
        socket.sendObject(textNationalID.getText());
        socket.sendObject(textPhoneNum.getText());
        socket.sendObject(dob);

        if (selectedAccount != null) {
            if (selectedAccount.getId() != loggedAccount.getId()) {

                if (ActiveRadioButton.isSelected() && ClientRadioButton.isSelected()) {
                    socket.sendObject("editAccounts");
                    socket.sendObject(selectedAccount);
                    socket.sendObject(Bank.ACTIVE);
                    selectedAccount.setStatus(Bank.ACTIVE);
                    socket.sendObject(Bank.CLIENT);
                    selectedAccount.setRole(Bank.CLIENT);
                } else if (ActiveRadioButton.isSelected() && AdminRadioButton.isSelected()) {
                    socket.sendObject("editAccounts");
                    socket.sendObject(selectedAccount);
                    socket.sendObject(Bank.ACTIVE);
                    selectedAccount.setStatus(Bank.ACTIVE);
                    socket.sendObject(Bank.ADMIN);
                    selectedAccount.setRole(Bank.ADMIN);
                } else if (FreezeRadioButton.isSelected() && ClientRadioButton.isSelected()) {
                    socket.sendObject("editAccounts");
                    socket.sendObject(selectedAccount);
                    socket.sendObject(Bank.FREEZED);
                    selectedAccount.setStatus(Bank.FREEZED);
                    socket.sendObject(Bank.CLIENT);
                    selectedAccount.setRole(Bank.CLIENT);
                } else if (FreezeRadioButton.isSelected() && AdminRadioButton.isSelected()) {
                    socket.sendObject("editAccounts");
                    socket.sendObject(selectedAccount);
                    socket.sendObject(Bank.FREEZED);
                    selectedAccount.setStatus(Bank.FREEZED);
                    socket.sendObject(Bank.ADMIN);
                    selectedAccount.setRole(Bank.ADMIN);
                } else {

                }
            }
        }

        returnedMessage.setText("Update has been saved!");
        returnedMessage.setTextFill(Color.web("#00a832"));
        returnedMessage.setVisible(true);
    }

    public void selectCustomer() {
        for (Customer selectedCustomer : tableViewAdmin.getSelectionModel().getSelectedItems()) {
            usernameTextFieldAdminPage.setText(selectedCustomer.getCusID() + "");
            editStatusButton.setVisible(true);
        }
    }

    @FXML
    public void accountScreenMethodAdmin(ActionEvent event) throws IOException {
        switchToAdminScreen(event);
    }

    @FXML
    public void accountScreenMethodEdit(ActionEvent event) throws IOException {
        editStatusMethod(event);
    }

    public void switchToDepositScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/depositAppView.fxml"));
        loader.setController(this);

        socket.loadController(this);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/logged.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        depsitNameLabel.setText(loggedAccount.getFirstName() + "  " + loggedAccount.getLastName());;
        depositCurrentBalanceLabel.setText(loggedAccount.getBalance() + "");
        stage.show();
    }

    public void deposit(ActionEvent event) throws IOException {
        try {
            depositCurrentBalanceLabel.setText(loggedAccount.getBalance() + "");
            double amount = Double.parseDouble(enteredDepositTextField.getText());
            if (amount <= 5000) {

                double balance = loggedAccount.getBalance();
                double updatedBalance = balance + amount;
                String format = String.format("%.2f", updatedBalance);
                loggedAccount.setBalance(Double.parseDouble(format));
                socket.sendObject("deposit");
                socket.sendObject(amount + "");
                socket.sendObject("Accepted");
                socket.sendObject(loggedAccount);
                loggedClientScreen(event);
            } else {
                socket.sendObject("deposit");
                socket.sendObject(amount + "");
                socket.sendObject("Rejected");
                socket.sendObject(loggedAccount);
                depositErrorLabel.setTextFill(Color.web("#bf0013"));
                depositErrorLabel.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getLocalizedMessage();
        }

    }

    public void loggedClientScreen(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/LoggedAppView.fxml"));
        loader.setController(this);

        socket.loadController(this);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/logged.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToTransferScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/TransferAppView.fxml"));
        loader.setController(this);

        socket.loadController(this);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("client/css/logged.css");
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        transferCurrentBalanceLabel.setText(loggedAccount.getBalance() + "");
        stage.show();
        addBeneficiaryButton.setDisable(true);
        transferButton.setDisable(true);
        usernameTextField.setEditable(false);
        amountTextField.setEditable(false);
    }

    @FXML
    public void addBeneficiaryMethod(ActionEvent event) throws IOException {
        if (!usernameTextField.getText().isEmpty()) {
            if (!usernameTextField.getText().equalsIgnoreCase(loggedAccount.getUsername())) {
                transErrorLabel.setVisible(false);
                for (Account item : transferTableView.getItems()) {
                    if (item.getUsername().equalsIgnoreCase(usernameTextField.getText())) {
                        usernameTextField.setText("");
                        return;
                    }
                }
                socket.sendObject("addBeneficiary");
                socket.sendObject(loggedAccount.getUsername());
                socket.sendObject(usernameTextField.getText());
                String status = (String) socket.receiveObjectData();
                if (status.equalsIgnoreCase("Accepted")) {
                    getAllBeneficiaries();
                } else {
                    usernameTextField.setText("");
                }
            } else {
                transErrorLabel.setTextFill(Color.web("#bf0013"));
                transErrorLabel.setVisible(true);
            }
        }
    }

    @FXML
    public void beneficiariesMethod(ActionEvent event) throws IOException {
        if (beneficiariesRadioButton.isSelected()) {
            addBeneficiaryButton.setDisable(false);
            transferButton.setDisable(false);
            usernameTextField.setEditable(true);
            usernameTextField.setText("");
            amountTextField.setEditable(true);
            getAllBeneficiaries();
            transferTableView.setOnMouseClicked(e -> {
                selectBeneficiary();
                amountTextField.requestFocus();
            });
        }
    }

    public void getAllBeneficiaries() throws IOException {
        socket.sendObject("AllBeneficiaries");
        socket.sendObject(loggedAccount.getUsername());
        ArrayList<Account> allBeneficiaries = (ArrayList<Account>) socket.receiveObjectData();
        ObservableList<Account> observableList = FXCollections.observableArrayList(allBeneficiaries);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        transferTableView.setItems(observableList);
    }

    public void selectBeneficiary() {
        for (Account selectedAccount : transferTableView.getSelectionModel().getSelectedItems()) {
            usernameTextField.setText(selectedAccount.getUsername());
        }
    }

    @FXML
    public void quickTransferMethod(ActionEvent event) {
        if (quickTransferRadioButton.isSelected()) {
            addBeneficiaryButton.setDisable(true);
            transferButton.setDisable(false);
            usernameTextField.setEditable(true);
            usernameTextField.setText("");
            amountTextField.setEditable(true);
            transferTableView.setItems(null);
        }
    }

    @FXML
    public void transferMethod(ActionEvent event) throws IOException {
        if (!usernameTextField.getText().isEmpty()) {
            if (!usernameTextField.getText().equalsIgnoreCase(loggedAccount.getUsername())) {
                if (!amountTextField.getText().isEmpty()) {
                    transErrorLabel.setVisible(false);
                    double amount = Double.parseDouble(amountTextField.getText());
                    double updatedBalance = loggedAccount.getBalance() - amount;
                    String format = String.format("%.2f", updatedBalance);
                    loggedAccount.setBalance(Double.parseDouble(format));
                    socket.sendObject("transfer");
                    socket.sendObject(loggedAccount);
                    socket.sendObject(usernameTextField.getText());
                    socket.sendObject(amountTextField.getText());
                    String status = (String) socket.receiveObjectData();
                    if (status.equalsIgnoreCase("Accepted")) {
                        loggedClientScreen(event);
                    } else {
                        usernameTextField.requestFocus();
                        amountTextField.setText("");
                        updatedBalance = loggedAccount.getBalance() + amount;
                        format = String.format("%.2f", updatedBalance);
                        loggedAccount.setBalance(Double.parseDouble(format));
                    }
                } else {
                    amountTextField.requestFocus();
                }
            } else {
                transErrorLabel.setTextFill(Color.web("#bf0013"));
                transErrorLabel.setVisible(true);
            }
        } else {
            usernameTextField.requestFocus();
        }
    }

    @FXML
    void switchToTransactionHistoryScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/HistoryAppView.fxml"));
            loader.setController(this);

            socket.loadController(this);

            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("client/css/logged.css");
            Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
            stage.setScene(scene);
            histBalanceLabel.setText(loggedAccount.getBalance() + "");
            stage.show();
            fillTransactionHistoryTable();
        } catch (IOException ex) {
            Logger.getLogger(LoggedViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fillTransactionHistoryTable() throws IOException {
        socket.sendObject("AllTransactions");
        socket.sendObject(loggedAccount.getUsername());
        ArrayList<TransactionHistory> allTransactions = (ArrayList<TransactionHistory>) socket.receiveObjectData();
        ObservableList<TransactionHistory> observableList = FXCollections.observableArrayList(allTransactions);
        transactionColumn.setCellValueFactory(new PropertyValueFactory<>("transaction"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        historyTable.setItems(observableList);
        dateColumn.setSortType(TableColumn.SortType.DESCENDING);
        historyTable.getSortOrder().add(dateColumn);
    }

}
