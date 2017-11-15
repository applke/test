package BotInterface;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.IOException;

public class NewAccountController extends Application {

    public NewAccountController() {

    }


    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    @FXML
    public void initialize() {

    }

    @FXML
    public TextField phone;

    @FXML
    public TextField code;

    private AccountWebView account;

    public void openAccountWebView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("accountWebView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        account = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1, 1000, 600));
        stage.setResizable(false);
        stage.show();
        Platform.setImplicitExit(false);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("Close!");
                event.consume();
            }
        });
        stage.setOnHiding(event -> {
            doAuthBtn.setDisable(false);
            doAuthBtn1.setDisable(true);
        });
    }

    @FXML
    public Button doAuthBtn;
    @FXML
    public Button doAuthBtn1;

    @FXML
    public void doAuth() {

        if (phone.getText().length() == 9) {
            try {
                openAccountWebView();
                doAuthBtn.setDisable(true);

                account.ready.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        // Only if completed
                        if (newValue) {
                            if (account.doAuth(phone.getText())) {
                                System.out.println("Auth success!");
                            }
                        }
                    }
                });
                account.codeRequired.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        // Only if completed
                        if (newValue) {
                            code.setDisable(false);
                            doAuthBtn1.setDisable(false);
                        }
                    }
                });
                account.currentCustomerProperty().addListener((obs, o, l) -> {
                   System.out.println(l);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            System.out.println("Phone is incorrect!");
        }
    }

    @FXML
    public void sendCode() {
        account.sendCode(code.getText());
    }
}
