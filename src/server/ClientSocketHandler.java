package server;

import bankService.Account;
import bankService.Customer;
import bankService.TransactionHistory;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.database.Database;

public class ClientSocketHandler implements Runnable {

    private ViewController controller;
    private Socket clientSocket;
    private String clientConnectionName;
    private String lookForRequest;
    private boolean isClientLogged = false;

    //to communicate with client
    private ObjectInputStream objectReader;
    private ObjectOutputStream objectWriter;

    private static Database database;

    //Fixing the sockets streams errors
    public ClientSocketHandler(Database database, Socket clientSocket, ViewController controller, String clientConnectionName) {
        ClientSocketHandler.database = database;
        this.controller = controller;
        this.clientSocket = clientSocket;
        this.clientConnectionName = clientConnectionName;
        try {
            //to write to the client
            objectWriter = new ObjectOutputStream(clientSocket.getOutputStream());
            //to read from the client
            objectReader = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("Listening to incoming requests...");
                try {
                    lookForRequest = (String) objectReader.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Reading request from client app and if matches to function name then its receiving and sending the object
                if (lookForRequest.contains("signIn")) {
                    authenticateSignInRequest();
                } else if (lookForRequest.contains("register")) {
                    authenticateRegisterRequest();
                } else if (lookForRequest.contains("reset")) {
                    carryOutResetRequest();
                } else if (lookForRequest.contains("status")) {
                    changeStatusRequest();
                } else if (lookForRequest.contains("withdraw")) {
                    withdrawRequest();
                } else if (lookForRequest.contains("deposit")) {
                    depositRequest();
                } else if (lookForRequest.contains("AllBeneficiaries")) {
                    getAllBeneficiariesRequest();
                } else if (lookForRequest.contains("addBeneficiary")) {
                    addBeneficiaryRequest();
                } else if (lookForRequest.contains("transfer")) {
                    transferRequest();
                } else if (lookForRequest.contains("AllAccounts")) {
                    getAllAccountsRequest();
                } else if (lookForRequest.contains("AllCustomers")) {
                    getAllCustomersRequest();
                } else if (lookForRequest.contains("editAccounts")) {
                    editAccountsRequest();
                } else if (lookForRequest.contains("editCustomer")) {
                    editCustomersRequest();
                } else if (lookForRequest.contains("getNumbers")) {
                    getNumOFAccAndCus();
                } else if (lookForRequest.contains("AllTransactions")) {
                    getAllTransactionsRequest();
                } else {
                    String request = lookForRequest + " ";
                    displayOnWindowConsole("says " + request);
                }

            }
        } catch (Exception e) {
            displayOnWindowConsole("Client with host/ip: "+clientConnectionName+" has been Disconnected! ");
        }
    }

    public void authenticateSignInRequest() throws IOException {
        try {
            Account signInRequest = (Account) objectReader.readObject();
            displayOnWindowConsole("Received a login request from host/ip: "+clientConnectionName+" , [PROCESSING] ");
            String receivedUsername = signInRequest.getUsername();
            String receivedPassword = signInRequest.getPassword();
            Account account = database.checkSignInRequest(receivedUsername, receivedPassword);
            if(account != null){
                displayOnWindowConsole("Login request has been approved for Client: " + account.toString() + ", [DONE] ");
            }
            sendObject(account);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            e.getLocalizedMessage();
        }
    }

    public void authenticateRegisterRequest() throws IOException {
        try {
            Account accountRequest = (Account) objectReader.readObject();
            database.addCustomer(accountRequest);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            e.getLocalizedMessage();
        }
    }

    private void carryOutResetRequest() throws IOException {
        try {
            Account resetRequest = (Account) objectReader.readObject();
            if (resetRequest.getPassword() == null) {
                String receivedUsername = resetRequest.getUsername();
                displayOnWindowConsole("Received an login request " + resetRequest.toString() + ", processing...");
                Account account = database.getAccount(new StringBuilder(receivedUsername));
                sendObject(account);
            } else {
                database.resetPassword(resetRequest.getId(), resetRequest.getPassword());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            e.getLocalizedMessage();
        }
    }

    private void changeStatusRequest() throws IOException {
        try {
            Account changeStatusRequest = (Account) objectReader.readObject();
            database.changeStatus(changeStatusRequest.getId(), changeStatusRequest.getStatus());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            e.getLocalizedMessage();
        }
    }

    public void sendObject(Object obj) throws IOException {
        objectWriter.writeObject(obj);
        objectWriter.flush();
        objectWriter.reset();
    }

    //Function which is looking for 1 from the client input stream to confirm that client is connected after received request
//    public void confirmConnection() throws IOException {
//        if (reader.read() != 1) {
//            flag = false;
//            clientSocket.close();
//            displayOnWindowConsole("is connected");
//        }
//    }
    public void displayOnWindowConsole(String text) {
        controller.serverRespondArea.appendText(text + getCurrentTime());
    }

    public String getCurrentTime() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return " [TIME] " + time.format(formatter) + "\n";
    }

    public void withdrawRequest() throws IOException {
        try {
            String amount = ((String) objectReader.readObject()).trim();
            String status = ((String) objectReader.readObject()).trim();
            System.out.println();
            Account withdrawRequest = (Account) objectReader.readObject();
            if (status.contains("Accepted")) {
                database.updateBalance(withdrawRequest.getId(), withdrawRequest.getBalance() + "");
            }
            database.addPapertransaction(withdrawRequest, "withdraw", amount, status);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            e.getLocalizedMessage();
        }
    }

    private void depositRequest() throws IOException {
        try {
            String amount = ((String) objectReader.readObject()).trim();
            String status = ((String) objectReader.readObject()).trim();
            Account depositRequest = (Account) objectReader.readObject();
            if (status.contains("Accepted")) {
                database.updateBalance(depositRequest.getId(), depositRequest.getBalance() + "");
            }
            database.addPapertransaction(depositRequest, "deposit", amount, status);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            e.getLocalizedMessage();
        }
    }

    public void getAllBeneficiariesRequest() throws ClassNotFoundException {
        try {
            String username = ((String) objectReader.readObject()).trim();
            ArrayList<Account> allBeneficiaries = database.getBeneficiaries(username);
            System.out.println(allBeneficiaries.isEmpty());
            sendObject(allBeneficiaries);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void addBeneficiaryRequest() throws ClassNotFoundException {
        try {
            String loggedAccount = ((String) objectReader.readObject()).trim();
            String beneficiaryAccount = ((String) objectReader.readObject()).trim();
            String status = database.addBeneficiary(loggedAccount, beneficiaryAccount);
            sendObject(status);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

//    public void getAllAccountsRequest() {
//        try {
//            ArrayList<Account> allAccounts = database.getAccounts();
//            System.out.println(allAccounts.isEmpty());
//            sendObject(allAccounts);
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//    }
    public void getAllAccountsRequest() throws ClassNotFoundException {
        try {
            int a = Integer.parseInt(((String) objectReader.readObject()).trim());
            System.out.println("===================");
            System.out.println(a);
            System.out.println("===================");
            ArrayList<Account> allAccounts = database.getAccountsByCustomer(a);
            sendObject(allAccounts);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void getAllCustomersRequest() {
        try {
            ArrayList<Customer> allCustomers = database.getCustomers();
            System.out.println(allCustomers.isEmpty());
            sendObject(allCustomers);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void editAccountsRequest() throws IOException {
        try {
            System.out.println();
            Account editRequest = (Account) objectReader.readObject();
            System.out.println("==============================+++++++++");
            System.out.println(editRequest.getLastName());
            String status = ((String) objectReader.readObject()).trim().replaceAll("y", "");
            String role = ((String) objectReader.readObject()).trim().replaceAll("y", "");
            database.updateAccount(editRequest, status, role);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getNumOFAccAndCus() throws IOException {
        try {
            String numOfCustomers = database.getNumberOfCustomers();
            sendObject(numOfCustomers);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void transferRequest() {
        try {
            Account transferRequest = (Account) objectReader.readObject();
            String beneficiaryAccount = ((String) objectReader.readObject()).trim().replaceAll("y", "");
            String amount = ((String) objectReader.readObject()).trim().replaceAll("y", "");
            String status = database.transferMoney(transferRequest, beneficiaryAccount, amount);
            if (status.equalsIgnoreCase("Accepted")) {
                database.updateBalance(transferRequest.getId(), transferRequest.getBalance() + "");
                Account beneficiary = database.getAccount(new StringBuilder(beneficiaryAccount));
                database.updateBalance(beneficiary.getId(), (beneficiary.getBalance() + Double.parseDouble(amount)) + "");
            }
            sendObject(status);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getAllTransactionsRequest() throws ClassNotFoundException {
        try {
            String username = ((String) objectReader.readObject()).trim().replaceAll("y", "");
            ArrayList<TransactionHistory> allTransactions = database.getTransactions(username);
            sendObject(allTransactions);
        } catch (IOException ex) {
            Logger.getLogger(ClientSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void editCustomersRequest() {
        try {

            String id = ((String) objectReader.readObject()).trim().replaceAll("y", "");
            String fname = ((String) objectReader.readObject()).trim().replaceAll("y", "");
            String lname = ((String) objectReader.readObject()).trim().replaceAll("y", "");
            String email = ((String) objectReader.readObject()).trim().replaceAll("y", "");
            String naID = ((String) objectReader.readObject()).trim().replaceAll("y", "");
            String PhoneNum = ((String) objectReader.readObject()).trim().replaceAll("y", "");
            Date dob = ((Date) objectReader.readObject());
            database.updateCustomer(id, fname, lname, email, naID, PhoneNum, dob);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
