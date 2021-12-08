package bankService;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable {

    private int cusID;
    private String firstName;
    private String lastName;
    private String nationalID;
    private Date dob;
    private String email;
    private String phoneNumber;
    private String noa;

    public Customer() {

    }

    public Customer(String firstName, String lastName, String nationalID, Date dob, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalID = nationalID;
        this.dob = dob;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Customer(int cusID, String firstName, String lastName, String nationalID, Date dob, String email, String phoneNumber) {
        this.cusID = cusID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalID = nationalID;
        this.dob = dob;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Customer(int cusID, String firstName, String lastName, String nationalID, Date dob, String email, String phoneNumber, String noa) {
        this.cusID = cusID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalID = nationalID;
        this.dob = dob;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.noa = noa;
    }


    public int getCusID() {
        return cusID;
    }

    public void setCusID(int cusID) {
        this.cusID = cusID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNoa() {
        return noa;
    }

    public void setNoa(String noa) {
        this.noa = noa;
    }
    
    @Override
    public String toString() {
        return getFirstName() + " "+ getLastName();
    }
}
