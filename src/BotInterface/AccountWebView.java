package BotInterface;

import BotInterface.POJO.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;


import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class AccountWebView {

    ArrayList<Runnable> chain = new ArrayList<Runnable>();
    ArrayList<Runnable> chainCode = new ArrayList<Runnable>();
    private String _number = "";
    private String _code = "";
    private boolean error_script = false;
    public WebEngine webEngine;
    public BooleanProperty ready = new SimpleBooleanProperty(false);
    public BooleanProperty codeRequired = new SimpleBooleanProperty(false);
    private final ReadOnlyObjectWrapper<JSObject> localStorage = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<JSObject> currentCustomerProperty() {
        return localStorage.getReadOnlyProperty();
    }

    final String ENTERNUMBERCOMAND = "$('input[name=\"phone_number\"]').trigger('focus').val(\"%s\").trigger('change').trigger('submit');";
    final String OKBUTTON = "$('button[ng-click=\"$close(data)\"]').click();";
    final String CHECKNUMBER = "if($('input[name=\"phone_code\"]').length > 0) {true} else {false};";
    final String ENTERCODE = "$('input').trigger('focus').val('%s').trigger('change');";
    final String CHECKLOGIN = "if($('div[ng-controller=\"AppImDialogsController\"]').length>0) {true} else {false}";
    @FXML
    public WebView webview;

    @FXML
    private void initialize() {
        webEngine = webview.getEngine();
        webEngine.load("https://web.telegram.org");
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    webEngine.executeScript("localStorage.clear();");
                    ready.set(true);
                }
            }
        });
    }

    public AccountWebView() {
        chain.add(() -> executeScript(String.format(ENTERNUMBERCOMAND, _number)));
        chain.add(() -> executeScript(OKBUTTON));
        chain.add(() -> executeScript(CHECKNUMBER));
        chainCode.add(() -> executeScript(String.format(ENTERCODE, _code)));
        chainCode.add(() -> executeScript(CHECKLOGIN));
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

    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> semaphore.release());
        semaphore.acquire();

    }

    public boolean doAuth(String number) {
        int delay = 2000;
        _number = number;
        try {
            JSObject storage = (JSObject) webEngine.executeScript("window.localStorage");
//            storage.
//            JSONObject jsonObject = new JSONObject(storage);
//            System.out.println(storage);
        }
        catch (Exception e){
            System.err.println(e);
        }

        if (number.length() == 100) {
            new Thread(() -> {
                try {
                    try {

                        for (Runnable e : chain) {
                            Thread.sleep(delay);
                            Platform.runLater(e);
                            waitForRunLater();
                            System.out.println("Running");
                            if (error_script) throw new Exception("FALSE returned");
                        }
                        if (!error_script)
                            codeRequired.set(true);
                    } catch (Exception e) {
                        System.err.println(e);
                        Stage stage = (Stage) webview.getScene().getWindow();
                        Platform.runLater(() -> stage.close());
                    }

                } catch (Exception e) {
                    System.err.println(e);
                }

            }).start();
            return true;
        } else return false;
    }

    public void sendCode(String code) {
        _code = code;
        int delay = 1000;
        if (code.length() > 0) {
            new Thread(() -> {
                try {
                    try {

                        for (Runnable e : chainCode) {
                            Thread.sleep(delay);
                            Platform.runLater(e);
                            waitForRunLater();
                            System.out.println("Running");
                            if (error_script) throw new Exception("Code incorrect returned");
                        }
                        codeRequired.set(false);
                        Platform.runLater(() -> {
                            localStorage.set ((JSObject) webEngine.executeScript("window.localStorage"));
                        });
                        System.out.println("Auth Done!");
//                        System.out.println((JSONObject) webEngine.executeScript("window.localStorage"));
                    } catch (Exception e) {

                        System.err.println(e);
                    }

                } catch (Exception e) {
                    System.err.println(e);
                }
            }).start();


        }
    }

    public synchronized void executeScript(String script) {
        System.out.println(script);
        try {
            error_script = !((boolean) webEngine.executeScript(script));
        } catch (Exception e) {

        }
    }
}
