package BotInterface.TaskManager;

import BotInterface.POJO.Account;
import BotInterface.POJO.Settings;
import BotInterface.POJO.Task;
import BotInterface.POJO.User;
import BotInterface.WebTelegramTask.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class TaskManager {

    private Settings settings;

    private ObservableList<Task> tasksData = FXCollections.observableArrayList();

    public ObservableList<User> getUsersData() {
        return usersData;
    }

    private ObservableList<User> usersData = FXCollections.observableArrayList();

    public ObservableList<Task> getTasksData() {
        return tasksData;
    }

    public static String escape(String s) {
        StringBuilder builder = new StringBuilder();
        boolean previousWasASpace = false;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                if (previousWasASpace) {
                    builder.append("&nbsp;");
                    previousWasASpace = false;
                    continue;
                }
                previousWasASpace = true;
            } else {
                previousWasASpace = false;
            }
            switch (c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '&':
                    builder.append("&amp;");
                    break;
                case '"':
                    builder.append("&quot;");
                    break;
                case '\n':
                    builder.append("<br>");
                    break;
                // We need Tab support here, because we print StackTraces as HTML
                case '\t':
                    builder.append("&nbsp; &nbsp; &nbsp;");
                    break;
                default:
                    if (c < 128) {
                        builder.append(c);
                    } else {
                        builder.append("&#").append((int) c).append(";");
                    }
            }
        }
        return builder.toString();
    }

    public void stop() {
      
        for (StageControl c : controllers) {
            c.stage.hide();
        }
    }

    public void createNewTask(Settings settings, String message, Account account) {
        try {
            StageControl stageControl = openNewTask();
            controllers.add(stageControl);


            stageControl.controller.ready.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    // Only if completed
                    if (newValue) {

                        stageControl.controller.writeLocalStorage(account.getLocalStorage());
//                        controller.sendMessageText(usersData, escape(testMessage.getText()), (int) timeDelay.getValue() * 1000, (int) timeDelayMessage.getValue() * 1000, (int) timeDelayMessage.getValue());
                    }
                }
            });
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void createNewTask(Settings settings, String message, ObservableList<Account> accounts) {
        int i = 0;
        for (Account account : accounts) {
            try {

                StageControl stageControl = openNewTask();
                controllers.add(stageControl);
                int finalI = i;
                stageControl.controller.ready.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        // Only if completed
                        if (newValue) {
                            if (finalI == 0)
                                stageControl.controller.writeLocalStorage(account.getLocalStorage());
//                        controller.sendMessageText(usersData, escape(testMessage.getText()), (int) timeDelay.getValue() * 1000, (int) timeDelayMessage.getValue() * 1000, (int) timeDelayMessage.getValue());
                        }
                    }
                });
                i++;
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    ObservableList<StageControl> controllers = FXCollections.observableArrayList();

    public StageControl openNewTask() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BotInterface/WebTelegramTask/sample.fxml"));

        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(root1, 1000, 600));
        stage.setResizable(false);
        stage.show();
        return new StageControl(stage, fxmlLoader.getController());
    }
}

class StageControl {
    public Stage stage;

    public StageControl(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public Controller controller;
}