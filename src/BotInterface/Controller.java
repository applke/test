package BotInterface;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class Controller {
    @FXML
    public WebView WebView;
    @FXML
    public TextArea textArea;

    private WebEngine webEngine;
    private Entities entities;
    public String ENTERNUMBERCOMAND = "$('input[name=\"phone_number\"]').val(\"0633451371\");";
    public String NEXTBUTTON = "$('a.login_head_submit_btn').click();";
    public String OKBUTTON = "$('button.btn-md-primar').click();";

    public Controller() {
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

    public void runScripts() {

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Run First Script!");
        Object a = webEngine.executeScript(ENTERNUMBERCOMAND);

//        try{
//            Thread.sleep(1000);
//        }catch(InterruptedException e){
//            e.printStackTrace();
//        }
//        webEngine.executeScript(NEXTBUTTON);
    }

    public void getTelegram() {
        webEngine.executeScript(ENTERNUMBERCOMAND);
    }
}
