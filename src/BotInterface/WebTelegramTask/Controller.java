package BotInterface.WebTelegramTask;


import BotInterface.POJO.User;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {
    @FXML
    public WebView WebView;

    private WebEngine webEngine;
    final String SETLOCALSTORAGE = "!function(e){window.localStorage.clear();for(var a=JSON.parse(e),o=Object.keys(a),r=0;r<o.length;r++){var t=o[r];window.localStorage.setItem(t,a[t])}}(window.str1);";
    //    final String OPENCONTACTS = "$('a[ng-click=\"openContacts()\"]').trigger('click');";
//    final String FINDUSER = "$('.contacts_modal_search input').val(\"%s\").trigger(\"change\");";
//    final String SELECTUSER = "if($('a[ng-click=\"contactSelect(contact.userID)\"]').length > 0) {$('a[ng-click=\"contactSelect(contact.userID)\"]').trigger('click')} else false";
    final String FINDUSER = "window.location = \"/#/im?p=%s\"";
    final String SELECTUSER = "if($('button span[my-i18n=\"modal_ok\"]').length>0){$(\".md_simple_modal_footer button\").trigger(\"click\"); false} else {true}";
    final String ENTERTEXT = "$('.composer_rich_textarea').html(\"%s\");";
    final String SENDMESSAGE = "$('button[type=\"submit\"]').trigger(\"mousedown\");";
    final String CHECKBLOCK = "if($('button span[my-i18n=\"modal_ok\"]').length>0){false} else {true}";

    private ArrayList<Runnable> chain = new ArrayList<Runnable>();

    public Controller() {
//        chain.add(() -> executeScript(OPENCONTACTS));
        chain.add((() -> executeScript(String.format(FINDUSER, this._username))));
        chain.add((() -> executeScript(SELECTUSER)));
        chain.add((() -> executeScript(String.format(ENTERTEXT, this._messageTxt))));
        chain.add((() -> executeScript(SENDMESSAGE)));
        chain.add((() -> executeScript(CHECKBLOCK)));
    }

    private String _username;
    private String _messageTxt;
    public BooleanProperty ready = new SimpleBooleanProperty(false);
    Boolean error_script = false;
    Boolean isBlocked = false;
    Thread work;

    public void sendMessageText(ObservableList<User> user, String text, int delay, int timeDelayMessage, final int messageCount) {
        error_script = false;
        _messageTxt = text;
        System.out.println(delay);
        System.out.println(timeDelayMessage);
        if (work == null) {
            work = new Thread(() -> {
                try {
                    int counter = messageCount;
                    for (User u : user) {
                        if (isBlocked)
                            break;
                        if (work.isInterrupted())
                            throw new InterruptedException();
                        if (u.getUsername() != null) {
                            _username = "@" + u.getUsername();
                            Thread.sleep(delay * 2);
                            try {
                                for (Runnable e : chain) {
                                    Platform.runLater(e);
                                    waitForRunLater();
                                    Thread.sleep(delay);
                                    counter--;
                                    if (counter == 0)
                                        break;
                                    if (error_script)
                                        throw new Exception("FALSE returned");
                                    if (work.isInterrupted())
                                        throw new InterruptedException();
                                    error_script = false;
                                }
                            } catch (Exception e) {

                            }
                            //end of user message SEND
//                            user.removeAll(u);//Удаляем
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

    public void stopThread() {
        if (work != null) {
            work.interrupt();
        }
    }

    public synchronized void executeScript(String script) {
        System.out.println(script);
        try {
            error_script = !((boolean) webEngine.executeScript(script));
            if (script == CHECKBLOCK && error_script == false) {
                isBlocked = true;
                System.out.println("Account Suspended!");
            }
        } catch (Exception e) {

        }
    }

    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> semaphore.release());
        semaphore.acquire();

    }

    @FXML
    private void initialize() {

        webEngine = WebView.getEngine();
        webEngine.load("https://web.telegram.org");
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    ready.set(true);
                }
            }
        });
    }

    public void writeLocalStorage(String str) {
        JSObject win = (JSObject) webEngine.executeScript("window");
        win.setMember("str1", str);
        System.out.println(str);
        try {
            webEngine.executeScript(SETLOCALSTORAGE);

        } catch (Exception e) {
            System.out.println(e);
        }
        webEngine.reload();
    }

    public void sendMessageTest(ObservableList<User> users, String message, int timeDelay, int timeDelayMessage, int messageCount) {

        sendMessageText(users, message, timeDelay * 1000, timeDelayMessage * 1000, messageCount);

    }
}
