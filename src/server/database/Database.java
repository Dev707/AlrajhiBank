package server.database;

import bankService.Account;
import bankService.Customer;
import bankService.TransactionHistory;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    Connection con;
    Statement stmt;
    ResultSet rs;

    //the connection to the database here when making a new object of 
    public Database() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            stmt = con.createStatement();
            rs = null;
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * This method takes username and password strings to check from the
     * database if they're correct and returns an Account object if it is, a
     * null if it isn't
     *
     * @param receivedUsername
     * @param receivedPassword
     * @return Account Object or a Null
     */
    public Account checkSignInRequest(String receivedUsername, String receivedPassword) {
        try {
            Statement stmtAccount = con.createStatement();
            ResultSet rsAccount = stmtAccount.executeQuery("select * from account where username = \'" + receivedUsername + "\'");
            if (rsAccount.next()) {
                if (rsAccount.getString("username").equals(receivedUsername)) {
                    Statement stmtCustomer = con.createStatement();
                    ResultSet rsCustomer = stmtCustomer.executeQuery("select * from customer where id = " + rsAccount.getInt("customer_id"));
                    if (rsAccount.getString("password").equals(receivedPassword)) {
                        if (rsCustomer.next()) {
                            return new Account(rsAccount.getInt("id"),
                                    rsAccount.getString("username"),
                                    rsAccount.getString("password"),
                                    Double.parseDouble(rsAccount.getString("balance")),
                                    rsAccount.getString("status"),
                                    rsAccount.getString("role"), rsAccount.getString("create_date"),
                                    false, rsAccount.getInt("customer_id"), rsCustomer.getString("fname"),
                                    rsCustomer.getString("lname"), rsCustomer.getString("national_ID"),
                                    rsCustomer.getDate("dob"), rsCustomer.getString("email"), rsCustomer.getString("phoneNumber"));
                        }
                    } else {
                        //if client username is correct but password isn't
                        if (rsCustomer.next()) {
                            return new Account(rsAccount.getString("username"), null);
                        }
                    }
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * This method takes two parameters, id of some account, new password for
     * that account, and it will update the password of that account
     *
     * @param id
     * @param password
     */
    public void resetPassword(int id, String password) {
        PreparedStatement stmt;
        try {
            stmt = con.prepareStatement("UPDATE account SET password = \'" + password + "\' WHERE account.id = " + id + ";");
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * This method takes two parameters, id of some account, new status for that
     * account, and it will update the status of that account
     *
     * @param id
     * @param status
     */
    public void changeStatus(int id, String status) {
        PreparedStatement stmt;
        try {
            stmt = con.prepareStatement("UPDATE account SET status = \'" + status + "\' WHERE account.id = " + id + ";");
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * This method is used to search the database for accounts associated with a
     * certain national id number and returns an Account Object or Null
     *
     * @param national_id
     * @return Account Object or Null
     */
    public Account getAccount(String national_id) {
        try {
            Statement stmtCustomer = con.createStatement();
            ResultSet rsCustomer = stmtCustomer.executeQuery("select * from customer where national_id = " + national_id);
            if (rsCustomer.next()) {
                Statement stmtAccount = con.createStatement();
                ResultSet rsAccount = stmtAccount.executeQuery("select * from account where customer_id = " + rsCustomer.getInt("id"));
                if (rsAccount.next()) {
                    return new Account(rsAccount.getInt("id"),
                            rsAccount.getString("username"),
                            rsAccount.getString("password"),
                            Double.parseDouble(rsAccount.getString("balance")),
                            rsAccount.getString("status"),
                            rsAccount.getString("role"), rsAccount.getString("create_date"),
                            false, rsAccount.getInt("customer_id"), rsCustomer.getString("fname"),
                            rsCustomer.getString("lname"), rsCustomer.getString("national_ID"),
                            rsCustomer.getDate("dob"), rsCustomer.getString("email"), rsCustomer.getString("phoneNumber"));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * This method is used to search the database for accounts associated with a
     * certain username and returns an Account Object or Null
     *
     * @param username
     * @return Account Object or Null
     */
    public Account getAccount(StringBuilder username) {
        try {
            Statement stmtAccount = con.createStatement();
            ResultSet rsAccount = stmtAccount.executeQuery("select * from account where username = \'" + username.toString() + "\'");
            if (rsAccount.next()) {
                Statement stmtCustomer = con.createStatement();
                ResultSet rsCustomer = stmtCustomer.executeQuery("select * from customer where id = " + rsAccount.getInt("customer_id"));
                if (rsCustomer.next()) {
                    return new Account(rsAccount.getInt("id"),
                            rsAccount.getString("username"),
                            rsAccount.getString("password"),
                            Double.parseDouble(rsAccount.getString("balance")),
                            rsAccount.getString("status"),
                            rsAccount.getString("role"), rsAccount.getString("create_date"),
                            false, rsAccount.getInt("customer_id"), rsCustomer.getString("fname"),
                            rsCustomer.getString("lname"), rsCustomer.getString("national_ID"),
                            rsCustomer.getDate("dob"), rsCustomer.getString("email"), rsCustomer.getString("phoneNumber"));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * This method is used to check if a customer exists based on national id
     * number and returns a String of a national id of the customer indicating
     * that it has been found or an empty String indicating that it hasn't been
     * found
     *
     * @param national_id
     * @return national id of a customer, or an empty String
     */
    public String customerExists(String national_id) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from customer");
            while (rs.next()) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    if (rs.getString("national_ID").equalsIgnoreCase(national_id)) {
                        return rs.getString("id");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }

    /**
     * This method adds a customer to the database and its account just by
     * taking an Account object
     *
     * @param accountRequest
     */
    public void addCustomer(Account accountRequest) {
        PreparedStatement stmt = null;
        try {
            String customerID_primaryKey = customerExists(accountRequest.getNationalID());
            if (customerID_primaryKey.equalsIgnoreCase("")) {
                stmt = con.prepareStatement("insert into customer(fname, lname, dob, email, national_ID, phoneNumber, create_date) values(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, accountRequest.getFirstName());
                stmt.setString(2, accountRequest.getLastName());
                stmt.setDate(3, new java.sql.Date(accountRequest.getDob().getTime()));
                stmt.setString(4, accountRequest.getEmail());
                stmt.setString(5, accountRequest.getNationalID());
                stmt.setString(6, accountRequest.getPhoneNumber());
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String create_date = sdf.format(new Date());
                accountRequest.setCreate_date(create_date);
                stmt.setString(7, accountRequest.getCreate_date());
                stmt.execute();
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int primaryKey = generatedKeys.getInt(1);
                        addAccount(accountRequest, primaryKey);

                    }
                }
            } else {
                int primaryKey = Integer.parseInt(customerID_primaryKey);
                addAccount(accountRequest, primaryKey);
            }
        } catch (Exception e) {
            System.out.println("Dunno");
            System.out.println(e);
        }
    }

    /**
     * This method is used to check if an account exists based on username and
     * returns a String of a username of the account indicating that it has been
     * found or an empty String indicating that it hasn't been found
     *
     * @param username
     * @return username of an account, or an empty String
     */
    public String usernameExists(String username) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from account");
            while (rs.next()) {
                if (rs.getString("username").equalsIgnoreCase(username)) {
                    return rs.getString("username");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }

    /**
     * This method will help addCustomer method by adding the new account of the
     * customer to the database
     *
     * @param accountRequest
     * @param primaryKey
     */
    public void addAccount(Account accountRequest, int primaryKey) {
        PreparedStatement stmt = null;
        try {
            String username = usernameExists(accountRequest.getUsername());
            if (username.equalsIgnoreCase("")) {
                stmt = con.prepareStatement("insert into account(customer_id, username, password, balance, status, role, create_date) values(?, ?, ?, ?, ?, ?, ?)");
                stmt.setInt(1, primaryKey);
                stmt.setString(2, accountRequest.getUsername());
                stmt.setString(3, accountRequest.getPassword());
                stmt.setString(4, Double.toString(accountRequest.getBalance()));
                stmt.setString(5, accountRequest.getStatus());
                stmt.setString(6, accountRequest.getRole());
                stmt.setString(7, accountRequest.getCreate_date());
                stmt.execute();
            } else {
                System.out.println("Username Exists!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method will add a papertransaction record to the database either a
     * withdraw or a deposit
     *
     * @param account
     * @param type
     * @param amount
     * @param status
     */
    public void addPapertransaction(Account account, String type, String amount, String status) {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT INTO papertransactions(account, type, amount, status, create_date) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, account.getUsername());
            stmt.setString(2, type);
            stmt.setString(3, amount);
            stmt.setString(4, status);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String create_date = sdf.format(new Date());
            stmt.setString(5, create_date);
            stmt.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method will update the balance of the given id account to the new
     * balance
     *
     * @param id
     * @param balance
     */
    public void updateBalance(int id, String balance) {
        PreparedStatement stmt;
        try {
            stmt = con.prepareStatement("UPDATE account SET balance = \'" + balance + "\' WHERE account.id = " + id + ";");
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * This method will carry out the procedure of adding a beneficiary record
     * to the database to some account
     *
     * @param loggedAccount
     * @param beneficiaryAccount
     * @return a String of different conditions (i.e. Accepted, Rejected,
     * DatabaseError)
     */
    public String addBeneficiary(String loggedAccount, String beneficiaryAccount) {
        if (!usernameExists(beneficiaryAccount).equalsIgnoreCase("")) {
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement("INSERT INTO beneficiaries(firstAccount, secAccount, create_date) VALUES (?, ?, ?)");
                stmt.setString(1, loggedAccount);
                stmt.setString(2, beneficiaryAccount);
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String create_date = sdf.format(new Date());
                stmt.setString(3, create_date);
                stmt.execute();
            } catch (Exception e) {
                System.out.println(e);
                return "DatabaseError";
            }
            return "Accepted";
        } else {
            return "Rejected";
        }
    }

    /**
     * This method will handle the process of fetching all beneficiaries
     * accounts of some account from the the database
     *
     * @param username
     * @return ArrayList<Account> of all beneficiaries accounts
     */
    public ArrayList<Account> getBeneficiaries(String username) {
        ArrayList<Account> allBeneficiaries = new ArrayList();
        try {
            Statement stmtBeneficiary = con.createStatement();
            ResultSet rsBeneficiary = stmtBeneficiary.executeQuery("select * from beneficiaries where firstAccount = \'" + username + "\'");
            while (rsBeneficiary.next()) {
                Statement stmtAccount = con.createStatement();
                ResultSet rsAccount = stmtAccount.executeQuery("select * from account where username = \'" + rsBeneficiary.getString("secAccount") + "\'");
                if (rsAccount.next()) {
                    Statement stmtCustomer = con.createStatement();
                    ResultSet rsCustomer = stmtCustomer.executeQuery("select * from customer where id = " + rsAccount.getInt("customer_id"));
                    if (rsCustomer.next()) {
                        Date dob = new Date(rsCustomer.getDate("dob").getTime());
                        System.out.println(allBeneficiaries.size());
                        allBeneficiaries.add(new Account(rsAccount.getInt("id"),
                                rsAccount.getString("username"),
                                rsAccount.getString("password"),
                                Double.parseDouble(rsAccount.getString("balance")),
                                rsAccount.getString("status"),
                                rsAccount.getString("role"), rsAccount.getString("create_date"),
                                false, rsAccount.getInt("customer_id"), rsCustomer.getString("fname"),
                                rsCustomer.getString("lname"), rsCustomer.getString("national_ID"),
                                dob, rsCustomer.getString("email"), rsCustomer.getString("phoneNumber")));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return allBeneficiaries;
    }

    /**
     * This method will handle the process of transferring money from and to
     * accounts
     *
     * @param transferRequest
     * @param beneficiaryAccount
     * @param amountString
     * @return a String of different conditions (i.e. Accepted, Rejected,
     * DatabaseError, usernameNotFound)
     */
    public String transferMoney(Account transferRequest, String beneficiaryAccount, String amountString) {
        double amount = Double.parseDouble(amountString);
        if (amount <= 75000.0 && amount <= transferRequest.getBalance()) {
            if (!usernameExists(beneficiaryAccount).equalsIgnoreCase("")) {
                PreparedStatement stmt = null;
                try {
                    stmt = con.prepareStatement("INSERT INTO transfertransactions(fromAccount, toAccount, transaction_amount, status, create_date) VALUES (?, ?, ?, ?, ?)");
                    stmt.setString(1, transferRequest.getUsername());
                    stmt.setString(2, beneficiaryAccount);
                    stmt.setString(3, amountString);
                    stmt.setString(4, "Accepted");
                    SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String create_date = sdf.format(new Date());
                    stmt.setString(5, create_date);
                    stmt.execute();
                } catch (Exception e) {
                    System.out.println(e);
                    return "DatabaseError";
                }
                return "Accepted";
            }
        } else {
            if (!usernameExists(beneficiaryAccount).equalsIgnoreCase("")) {
                PreparedStatement stmt = null;
                try {
                    stmt = con.prepareStatement("INSERT INTO transfertransactions(fromAccount, toAccount, transaction_amount, status, create_date) VALUES (?, ?, ?, ?, ?)");
                    stmt.setString(1, transferRequest.getUsername());
                    stmt.setString(2, beneficiaryAccount);
                    stmt.setString(3, amountString);
                    stmt.setString(4, "Rejected");
                    SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String create_date = sdf.format(new Date());
                    stmt.setString(5, create_date);
                    stmt.execute();
                } catch (Exception e) {
                    System.out.println(e);
                    return "DatabaseError";
                }
                return "Rejected";
            }
        }
        return "usernameNotFound";
    }

    /**
     * This method will handle the process of fetching all transactions made on
     * a certain account
     *
     * @param username
     * @return ArrayList<TransactionHistory> of all transactions history
     */
    public ArrayList<TransactionHistory> getTransactions(String username) {
        ArrayList<TransactionHistory> allTransactions = new ArrayList();
        try {
            Statement stmtPaper = con.createStatement();
            ResultSet rsPaper = stmtPaper.executeQuery("select * from papertransactions where account = \'" + username + "\'");
            while (rsPaper.next()) {
                String type = rsPaper.getString("type");
                String amount = rsPaper.getString("amount");
                String status = rsPaper.getString("status");
                String date = rsPaper.getString("create_date");
                allTransactions.add(new TransactionHistory((type.equalsIgnoreCase("withdraw")) ? "Cash out Account" : "Cash in Account", type, amount, status, date));
            }
            Statement stmtTransferFrom = con.createStatement();
            ResultSet rsTransferFrom = stmtTransferFrom.executeQuery("select * from transfertransactions where fromAccount = \'" + username + "\'");
            while (rsTransferFrom.next()) {
                String toAccount = rsTransferFrom.getString("toAccount");
                String type = "Transfer";
                String amount = rsTransferFrom.getString("transaction_amount");
                String status = rsTransferFrom.getString("status");
                String date = rsTransferFrom.getString("create_date");
                allTransactions.add(new TransactionHistory("Transfer to: " + toAccount, type, amount, status, date));
            }
            Statement stmtTransferTo = con.createStatement();
            ResultSet rsTransferTo = stmtTransferTo.executeQuery("select * from transfertransactions where toAccount = \'" + username + "\'");
            while (rsTransferTo.next()) {
                String fromAccount = rsTransferTo.getString("fromAccount");
                String type = "Transfer";
                String amount = rsTransferTo.getString("transaction_amount");
                String status = rsTransferTo.getString("status");
                String date = rsTransferTo.getString("create_date");
                allTransactions.add(new TransactionHistory("Transfer from: " + fromAccount, type, amount, status, date));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return allTransactions;
    }

    /**
     * This method will fetch all customers and their accounts from the database
     *
     * @return ArrayList<Account> of all customer's accounts
     */
    public ArrayList<Account> getAccounts() {
        ArrayList<Account> allAccounts = new ArrayList();
        try {
            Statement stmtAccount = con.createStatement();
            ResultSet rsAccount = stmtAccount.executeQuery("SELECT * FROM account");
            while (rsAccount.next()) {
                Statement stmtCustomer = con.createStatement();
                ResultSet rsCustomer = stmtCustomer.executeQuery("select * from customer");
                if (rsCustomer.next()) {
                    Date dob = new Date(rsCustomer.getDate("dob").getTime());
                    System.out.println(allAccounts.size());
                    allAccounts.add(new Account(rsAccount.getInt("id"),
                            rsAccount.getString("username"),
                            rsAccount.getString("password"),
                            Double.parseDouble(rsAccount.getString("balance")),
                            rsAccount.getString("status"),
                            rsAccount.getString("role"),
                            rsAccount.getString("create_date"),
                            false,
                            rsAccount.getInt("customer_id"),
                            rsCustomer.getString("fname"),
                            rsCustomer.getString("lname"),
                            rsCustomer.getString("national_ID"),
                            dob, rsCustomer.getString("email"),
                            rsCustomer.getString("phoneNumber")));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return allAccounts;

    }

    /**
     * This method handle the process of fetching all accounts of a certain
     * customer
     *
     * @param id
     * @return ArrayList<Account> of all accounts that belong to a certain
     * customer
     */
    public ArrayList<Account> getAccountsByCustomer(int id) {
        ArrayList<Account> allAccounts = new ArrayList();
        try {
            Statement stmtAccount = con.createStatement();
            ResultSet rsAccount = stmtAccount.executeQuery("SELECT * FROM account WHERE account.customer_id = " + id + ";");
            while (rsAccount.next()) {
                allAccounts.add(new Account(rsAccount.getInt("id"),
                        rsAccount.getString("username"),
                        rsAccount.getString("password"),
                        Double.parseDouble(rsAccount.getString("balance")),
                        rsAccount.getString("status"),
                        rsAccount.getString("role"),
                        rsAccount.getString("create_date")
                ));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return allAccounts;
    }

    /**
     * This method handle the process of fetching all customers in the database
     *
     * @return ArrayList<Customer> of all customers in the database
     */
    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> allCustomers = new ArrayList();
        try {
            Statement stmtCustomer = con.createStatement();
            ResultSet rsCustomer = stmtCustomer.executeQuery("SELECT * FROM customer");
            while (rsCustomer.next()) {
                Date dob = new Date(rsCustomer.getDate("dob").getTime());
                allCustomers.add(new Customer(rsCustomer.getInt("id"), rsCustomer.getString("fname"), rsCustomer.getString("lname"), rsCustomer.getString("national_ID"), dob, rsCustomer.getString("email"), rsCustomer.getString("phoneNumber"), getNumberOfAccountsByCus(rsCustomer.getInt("id"))));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return allCustomers;

    }

    /**
     * This method will handle the process of fetching all the number of a
     * certain customer accounts
     *
     * @param id
     * @return String of 0 if none, or the number of accounts
     */
    public String getNumberOfAccountsByCus(int id) {
        try {
            Statement stmt = con.createStatement();
            ResultSet numOfAccount = stmt.executeQuery("SELECT COUNT(*) FROM account WHERE account.customer_id = " + id + ";");
            String a = "";

            if (numOfAccount.next()) {
                a = numOfAccount.getString("COUNT(*)") + "";
            } else {
                a = "0";
            }
            return a;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * This method will handle the process of updating all account information
     * from which: Role and Status
     *
     * @param editRequest
     * @param status
     * @param role
     */
    public void updateAccount(Account editRequest, String status, String role) {
        PreparedStatement stmt;
        try {
            String sql = "UPDATE customer SET fname = \'" + editRequest.getFirstName() + "\', lname = \'" + editRequest.getLastName() + "\', dob = \'" + editRequest.getDob() + "\', email = \'" + editRequest.getEmail() + "\', national_ID = \'" + editRequest.getNationalID() + "\', phoneNumber = \'" + editRequest.getPhoneNumber() + "\' WHERE customer.id = " + editRequest.getCusID() + ";";
            stmt = con.prepareStatement(sql);
            stmt.execute();

            stmt = con.prepareStatement("UPDATE account SET status = \'" + status + "\' WHERE account.id = " + editRequest.getId() + ";");
            stmt.execute();
            stmt = con.prepareStatement("UPDATE account SET role = \'" + role + "\' WHERE account.id = " + editRequest.getId() + ";");
            stmt.execute();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    /**
     * This method will handle the process of updating all customer information
     *
     * @param id
     * @param fname
     * @param lname
     * @param email
     * @param naID
     * @param phoneNum
     * @param dob
     */
    public void updateCustomer(String id, String fname, String lname, String email, String naID, String phoneNum, Date dob) {
        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
        String stringDate = DateFor.format(dob);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(stringDate, formatter);
        PreparedStatement stmt;
        try {
            String sql = "UPDATE customer SET fname = \'" + fname + "\', lname = \'" + lname + "\', dob = \'" + localDate + "\', email = \'" + email + "\', national_ID = \'" + naID + "\', phoneNumber = \'" + phoneNum + "\' WHERE customer.id = " + id + ";";

            stmt = con.prepareStatement(sql);
            System.out.println(sql);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    /**
     * This method will handle the process of counting all customers in the bank
     * 
     * @return String of 0 if none, or the number of accounts
     */
    public String getNumberOfCustomers() {
        try {
            Statement stmt = con.createStatement();
            ResultSet numOfAccount = stmt.executeQuery("SELECT COUNT(*) FROM customer");
            String a = "";
            
            if (numOfAccount.next()) {
                a = numOfAccount.getString("COUNT(*)") + "";
            } else {
                a = "0";
            }
            return a;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

}
