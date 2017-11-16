package BotInterface.POJO;

public class Account {

    public String number;
    public String localStorage;
    public boolean active;

    public Account(String number, String localStorage,boolean active) {
        this.number = number;
        this.localStorage = localStorage;
        this.active = active;
    }

    public void setLocalStorage(String localStorage) {
        this.localStorage = localStorage;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLocalStorage() {
        return localStorage;
    }

    public String getNumber() {
        return number;
    }

    public String getActive() {
        return active ? "+" : "-";
    }

    public void setActive(boolean a) {
        this.active = a;
    }
}
