package bankService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import server.database.Database;

public class Bank implements Serializable {

    private Database database;
    public static final String ACTIVE = "Active";
    public static final String AUTH = "Require-Authentication";
    public static final String FREEZED = "Freezed";
    public static final String ADMIN = "Admin";
    public static final String CLIENT = "Client";

    public Bank(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
    
    public static String authenticationCode(String phoneNumber){
        try {
            ProcessBuilder builder = new ProcessBuilder("python", System.getProperty("user.dir") + "\\authenticator.py", phoneNumber);
            Process process = builder.start();
            BufferedReader readOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String o = readOutput.readLine();
            System.out.println(o);
            return o;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
}
