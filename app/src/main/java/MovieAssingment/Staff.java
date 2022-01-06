package MovieAssingment;

import java.util.ArrayList;

public class Staff {
    private String username;
    private String password;
    private boolean isManager;

    public Staff(String username, String password, boolean isManager){
        this.username = username;
        this.password = password;
        this.isManager = isManager;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public boolean login(String username, String password){
        if (this.username.equals(username) && this.password.equals(password)){
            return true;
        }
        else{
            return false;
        }
    }
}
