package BotInterface;


import BotInterface.POJO.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {
    @FXML
    public WebView WebView;
    @FXML
    public TextField textField;
    @FXML
    public Label label;
    public TextArea textArea;
    public TextField username;
    public TextField messageTxt;


    private WebEngine webEngine;


    final String OPENCONTACTS = "$('a[ng-click=\"openContacts()\"]').trigger('click');";
    final String FINDUSER = "$('.contacts_modal_search input').val(\"%s\").trigger(\"change\");";
    final String SELECTUSER = "if($('a[ng-click=\"contactSelect(contact.userID)\"]').length > 0) {$('a[ng-click=\"contactSelect(contact.userID)\"]').trigger('click')} else false";
    final String ENTERTEXT = "$('.composer_rich_textarea').html(\"%s\");";
    final String SENDMESSAGE = "$('button[type=\"submit\"]').trigger(\"mousedown\");";


    private ArrayList<Runnable> chain = new ArrayList<Runnable>();

    public Controller() {
        chain.add(() -> executeScript(OPENCONTACTS));
        chain.add((() -> executeScript(String.format(FINDUSER, this._username))));
        chain.add((() -> executeScript(SELECTUSER)));
        chain.add((() -> executeScript(String.format(ENTERTEXT, this._messageTxt))));
        chain.add((() -> executeScript(SENDMESSAGE)));


    }

    private String _username;
    private String _messageTxt;

    Boolean error_script = false;
    Thread work;

    public void sendMessageText(ObservableList<User> user, String text, int delay, int timeDelayMessage) {
        error_script = false;
        _messageTxt = text;

        if (work == null) {
            work = new Thread(() -> {
                try {
                    Platform.runLater(() -> label.setText("Started!"));
                    for (User u : user) {
                        if (work.isInterrupted())
                            throw new InterruptedException();
                        if ( u.getUsername() != null) {
                            _username = "@" + u.getUsername();
                            Thread.sleep(delay);
                            try {
                                for (Runnable e : chain) {
                                    Platform.runLater(e);
                                    Thread.sleep(delay);
                                    System.out.println("Running");
                                    if (error_script) throw new Exception("FALSE returned");
                                    if (work.isInterrupted())
                                        throw new InterruptedException();

                                }
                            } catch (Exception e) {

                            }
                            //end of user message SEND
                            user.removeAll(u);//Удаляем
                            Thread.sleep(timeDelayMessage);
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            });
            work.start();
        }
    }

    @FXML
    public void stopThread() {
        if (work != null) {
            label.setText("Thread is stopped!");
            work.interrupt();
            work = null;
        }
    }

    @FXML
    public void pauseThread() {
    }

    public synchronized void executeScript(String script) {

        try {
            error_script = !((boolean) webEngine.executeScript(script));
        } catch (Exception e) {

        }
    }

    @FXML
    private void initialize() {

        webEngine = WebView.getEngine();
        webEngine.load("https://web.telegram.org");
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    runScripts();
                }
            }
        });
    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                Platform.runLater(runnable);
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    public Settings settings;

    public void runScripts() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("test.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            settings = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1, 1000, 600));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        webEngine.executeScript("localStorage.clear();");
        label.setText("WebView Loaded");

//        setTimeout(() -> webEngine.executeScript(String.format(ENTERNUMBERCOMAND, "0689643710")), 2000);
        label.setText("Number Entered!");
//        setTimeout(() -> webEngine.executeScript(OKBUTTON), 4000);
        label.setText("OK Button ENTERED");
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

    public void sendMessageTest() {

        sendMessageText(settings.usersData, escape(settings.testMessage.getText()), (int) settings.timeDelay.getValue() * 1000, (int) settings.timeDelayMessage.getValue() * 1000);


    }
}
