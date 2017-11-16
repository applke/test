package BotInterface.POJO;

import javafx.collections.ObservableList;

public class Task {
    int count;
    int progress;
    Account account;
    String message;
    ObservableList<User> users;
    Settings settings;
    boolean isActive;

    public Task(Account account, String message, ObservableList<User> users, Settings settings, boolean isActive) {
        this.account = account;
        this.message = message;
        this.users = users;
        this.settings = settings;
        this.isActive = isActive;
        progress = 0;
        count = users.size();
    }

    public String getAccount() {
        return account.getNumber();
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void setUsers(ObservableList<User> users) {
        this.users = users;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
