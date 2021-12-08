package bankService;

import java.io.Serializable;

public class LoginRequest implements Serializable {

    private String username;
    private String password;

    public LoginRequest(String login, String password) {
        this.username = login;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login: " + this.username + ", password: " + this.password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
