package BotInterface;


import BotInterface.POJO.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Settings extends Application {


    public ObservableList<User> usersData = FXCollections.observableArrayList();

    @FXML
    private TableView<User> tableUsers;
    @FXML
    public TextField userAdd;
    @FXML
    public TextArea testMessage;
    @FXML
    public Slider timeDelay;
    @FXML
    public Slider timeDelayMessage;
    @FXML
    public void AddUser() {
        usersData.add(new User(0, this.userAdd.getText(), 0, "0", "0"));
    }


    // инициализируем форму данными
    @FXML
    private void initialize() {
        initData();
        TableColumn id = new TableColumn("ID");
        TableColumn username = new TableColumn("username");
        TableColumn access_hash = new TableColumn("access_hash");
        TableColumn last_name = new TableColumn("last_name");
        TableColumn first_name = new TableColumn("first_name");
        id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        access_hash.setCellValueFactory(new PropertyValueFactory<User, String>("access_hash"));
        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        last_name.setCellValueFactory(new PropertyValueFactory<User, String>("last_name"));
        first_name.setCellValueFactory(new PropertyValueFactory<User, String>("first_name"));
        tableUsers.getColumns().setAll(id, access_hash, username, last_name, first_name);
        tableUsers.setItems(usersData);
    }

    private void initData() {
        usersData.add(new User(1, "mami322", 0, "123130", "2131230"));
        usersData.add(new User(1, "mami322", 0, "123130", "2131230"));
        usersData.add(new User(1, "mami322", 0, "123130", "2131230"));
        usersData.add(new User(1, "mami322", 0, "123130", "2131230"));
        usersData.add(new User(1, "mami322", 0, "123130", "2131230"));
        usersData.add(new User(1, "mami322", 0, "123130", "2131230"));
        usersData.add(new User(1, "mami322", 0, "123130", "2131230"));
        usersData.add(new User(1, "mami322", 0, "123130", "2131230"));
//        usersData.add(new User(1, "@lafiaba", "123210", "1232130", "0123"));

    }

    @FXML
    private void loadFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home") + "/Desktop")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            openJson(file);
        }
    }

    private void openJson(File file)    {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser
                    .parse(new FileReader(file));


            JSONArray users = (JSONArray) object;

            int i = 0;
            for (Object user : users) {
                i++;
                long id, access_hash = 0;
                String username, first_name, last_name = "";
                try {
                    id = (long) ((JSONObject) user).get("id");
                    username = (String) ((JSONObject) user).get("username");
                    access_hash = (long) ((JSONObject) user).get("access_hash");
                    last_name = (String) ((JSONObject) user).get("first_name");
                    first_name = (String) ((JSONObject) user).get("last_name");
                    usersData.add(new User(id, username, access_hash, first_name, last_name));
                } catch (Exception e) {
                }

                if (i > 100)
                    break;
            }
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }

    Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
    }
}
