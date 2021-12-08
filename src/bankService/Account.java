package bankService;

import java.io.Serializable;
import java.util.Date;

public class Account extends Customer implements Serializable {

    private int id;
    private String username;
    private String password;
    private double balance;
    private double lastAmount;

    public double getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(double lastAmount) {
        this.lastAmount = lastAmount;
    }
    private String status;
    private String role;
    private String create_date;
    private boolean isLogged;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account(String username, String password, double balance, String status, String role, boolean isLogged, String firstName, String lastName, String nationalID, Date dob, String email, String phoneNumber) {
        super(firstName, lastName, nationalID, dob, email, phoneNumber);
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.status = status;
        this.role = role;
        this.isLogged = false;
    }

    public Account(int id, String username, String password, double balance, String status, String role, String create_date, boolean isLogged, int cusID, String firstName, String lastName, String nationalID, Date dob, String email, String phoneNumber) {
        super(cusID, firstName, lastName, nationalID, dob, email, phoneNumber);
        this.id = id;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.status = status;
        this.role = role;
        this.create_date = create_date;
        this.isLogged = false;
    }

    public Account(int id, String username, String password, double balance, String status, String role, String create_date) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.status = status;
        this.role = role;
        this.create_date = create_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public boolean isIsLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

}
