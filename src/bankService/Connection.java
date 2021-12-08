package bankService;

public class Connection {
    private String username;
    private String ip;
    private String port;
    private String date;

    public Connection(String username, String ip, String port, String date) {
        this.username = username;
        this.ip = ip;
        this.port = port;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
    
}
