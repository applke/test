package BotInterface;

import BotInterface.MainMenu.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu/test.fxml"));
        Parent root = (Parent)loader.load();
        Settings st = (Settings)loader.getController();
        st.setStage(primaryStage);
        primaryStage.setTitle("Telegram WebView");

        primaryStage.setResizable(false);

        primaryStage.setScene(new Scene(root, 1100, 700));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
