package bankService;

import java.io.Serializable;

public class TransactionHistory implements Serializable{
    private String transaction;
    private String type;
    private String amount;
    private String status;
    private String date;

    public TransactionHistory(String transaction, String type, String amount, String status, String date) {
        this.transaction = transaction;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.date = date;
    }

    public TransactionHistory() {
        
    }
    
    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
    
}
