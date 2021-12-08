package server;

import bankService.Account;
import bankService.Bank;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import server.database.Database;

public class ViewController extends ServerAppStarter implements Initializable {

    @FXML
    TextArea serverRespondArea;
 

    private Bank bank; 
    private Database database;

    public ViewController(Bank bank) {
        this.bank = bank;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.database = bank.getDatabase();
//        System.out.println(Arrays.toString(operationCustomerList.toArray()));
    }

    public void addRespondToTextArea(String respond) {
        serverRespondArea.setText(respond);
    }
}
