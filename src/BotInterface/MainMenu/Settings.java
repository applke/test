package BotInterface.MainMenu;


import BotInterface.NewAccountAuth.NewAccountController;
import BotInterface.POJO.Account;
import BotInterface.POJO.Task;
import BotInterface.POJO.User;

import BotInterface.TaskManager.TaskManager;
import BotInterface.WebTelegramTask.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.Set;


public class Settings {

    String str = "{\"dc\":\"2\",\"dc1_auth_key\":\"\\\"6bc1b96919c4cfa7652d570f179c02687449d7b1b8036c63021417aa3cd72b19cb6ab98550d9cc17b8c199ba5af33b3543dc3ed23b599ca025750b05b7dd057c7c1bdf173a39f78921e55113a423ae2a1c3666c832994d76a3a891a4e9777500f43a51aa9459c636c4010bc9954b3b6b9dd324511bbf43ee9a59a7beb04ddb9f6a14f35733fc562eaee89cbc4f767961de7845ce659972cb96021afa5f27c21ea1c3559162b034168a404a2ad967cd3c5277e19737da83f4ce6ed62168f3fe53c9011cf2c10867e5f8c7f573ed708113d02249721895627bf0412a17bdb02571f9eb11f8d9239e2d023bc9d92d90bcf716a9c7d335d0eb81b4007f73442f795e\\\"\",\"dc2_auth_key\":\"\\\"b86612c7825d07e0098b27b58c861930f1c1d33c9355823c06e2ffa930f0709f8851f2972a9ec294fc8b2f4519888b3ba747de73d366ef12778ecf0d249f5aef9bd1f5ae092b7f5aff8a784d8af6b955894e97d70e3e3971ec80d7b44a42d413ae1b5eaa9c71488826b7f462c4dfe879ee7765df3d4705544d98eb826d39eb25b18d3635ccc821936cadb231359995d54a06768865d3e3f9d09f6234039c16ac5a5aadb9bf734bca3f6b6f132f7bf083abbeb5f0112d4e3b0e3e92df77a80adee30d7327d74e4e03aca7d1702f8e78652756d965bd4a37afd1a69e3678f28f1969be406b0bd8854bf3d35ddc3383c5a8279938cfb88a91b76ccb9f98cce989e0\\\"\",\"dc2_server_salt\":\"\\\"f9bc5006b0d1aa26\\\"\",\"dc4_auth_key\":\"\\\"baca0c7a5dd998931bfc4a8302eb2cae9a6465f295264a2f117aa31cd321199a2ee41708b9dea5a0907f0f11c1c380119ffcf9e321a51a1d1a755ddab350810a483a3a7d3a42a17bfb8d0f67205555fff932f8d472668dfcb7958a5bbe6c0fcf2b83301f396226251ca4648dbe14c1d99d109ddd18dfd2509129c7a0c06f8090b0ce05fe7f069a741adb428134e1fac89fd541675293d36e0f60594fb152e55a59d42329b6e967d70c3e120cfd94e1255bf22ae3bcf7ee63881babf33135aa00db9dc14d2316046c476db6846cf7e415f1a8fbd87947f584e842d5d682ddd374adbd32ec28d2895799d088eb77d8812df13331070eb385cd2272a82c6baf90f1\\\"\",\"dc4_server_salt\":\"\\\"4bc41c38ceaeb118\\\"\",\"last_version\":\"\\\"0.6.0\\\"\",\"max_seen_msg\":\"308\",\"tgme_sync\":\"{\\\"canRedirect\\\":true,\\\"ts\\\":1510698092}\",\"user_auth\":\"{\\\"dcID\\\":2,\\\"id\\\":428723126}\",\"xt_instance\":\"{\\\"id\\\":1428127730,\\\"idle\\\":false,\\\"time\\\":1510774767714}\"}";

    public ObservableList<User> usersData;
    public ObservableList<Account> accountsData = FXCollections.observableArrayList();
    public ObservableList<Task> tasksData;
    private TaskManager manager = new TaskManager();

    public Settings() {
        tasksData = manager.getTasksData();
        usersData = manager.getUsersData();
    }

    @FXML
    public TextField userAdd;
    @FXML
    public TextArea testMessage;
    @FXML
    public Slider timeDelay;
    @FXML
    public Slider timeDelayMessage;
    @FXML
    public Slider timeMessage;

    @FXML
    public void AddUser() {
        usersData.add(new User(0, this.userAdd.getText(), 0, "0", "0"));
    }


   Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        setVictimTable();
        setAccountTable();
        seTaskTable();

//        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            public void handle(WindowEvent we) {
//                saveUsers("users.json");
//            }
//        });


    }

    @FXML
    private TableView<User> tableUsers;
    @FXML
    private TableView<Account> accountTable;
    @FXML
    private TableView<Task> taskTableView;

    private void setVictimTable() {
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

    private void setAccountTable() {
        initDataAccount();
        TableColumn number = new TableColumn("number");
        TableColumn active = new TableColumn("active");

        number.setCellValueFactory(new PropertyValueFactory<User, Integer>("number"));
        active.setCellValueFactory(new PropertyValueFactory<User, String>("active"));
        accountTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        accountTable.getColumns().setAll(number, active);
        accountTable.setItems(accountsData);
    }

    private void seTaskTable() {
        initTaskAccount();
        TableColumn account = new TableColumn("Аккаунт");
        TableColumn count = new TableColumn("Количество");
        TableColumn progress = new TableColumn("Прогресс");

        account.setCellValueFactory(new PropertyValueFactory<User, String>("account"));
        count.setCellValueFactory(new PropertyValueFactory<User, Integer>("count"));
        progress.setCellValueFactory(new PropertyValueFactory<User, Integer>("progress"));
        accountTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        taskTableView.getColumns().setAll(account, count, progress);
        taskTableView.setItems(tasksData);
    }

    private void initTaskAccount() {

        tasksData.add(new Task(new Account("04", "04", true), "Message", usersData, new BotInterface.POJO.Settings(), true));
        tasksData.add(new Task(new Account("04", "04", true), "Message", usersData, new BotInterface.POJO.Settings(), true));
    }

    private void initDataAccount() {
        JSONParser parser = new JSONParser();
        try {
            File file = new File("accounts.json");
            Object object = parser
                    .parse(new FileReader(file));


            JSONArray users = (JSONArray) object;

            int i = 0;
            for (Object user : users) {
                i++;
                boolean active;
                String number, localStorage;
                try {
                    number = (String) ((JSONObject) user).get("number");
                    localStorage = (String) ((JSONObject) user).get("localStorage");
                    active = (boolean) ((JSONObject) user).get("active");

                    accountsData.add(new Account(number, localStorage, active));
                } catch (Exception e) {
                }

                if (i > 300)
                    break;
            }
            accountsData.addListener(new ListChangeListener<Account>() {
                @Override
                public void onChanged(Change<? extends Account> c) {
                    while (c.next()) {
                        if (c.wasAdded() || c.wasRemoved()) {
                            saveAccounts();
                        }
                    }
                }

            });
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }


    }

    private void initData() {
        openJson(new File("users.json"));
    }

    private void saveAccounts() {
        JSONArray list = new JSONArray();
        JSONObject obj;
        for (Account a : accountsData) {
            obj = new JSONObject();
            obj.put("number", a.number);
            obj.put("localStorage", a.localStorage);
            obj.put("active", a.active);

            list.add(obj);

        }

        try (FileWriter file = new FileWriter("accounts.json")) {

            file.write(list.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveUsers() {
        JSONArray list = new JSONArray();
        JSONObject obj;
        for (User a : usersData) {
            obj = new JSONObject();
            obj.put("id", a.getId());
            obj.put("username", a.getUsername());
            obj.put("access_hash", a.getAccess_hash());
            obj.put("first_name", a.getFirst_name());
            obj.put("last_name", a.getLast_name());
            list.add(obj);

        }
        String path;

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Base (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File fileNew = fileChooser.showSaveDialog(new Stage());
        path = fileNew.getPath();

        try (FileWriter file = new FileWriter(path)) {

            file.write(list.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers(String p) {
        JSONArray list = new JSONArray();
        JSONObject obj;
        for (User a : usersData) {
            obj = new JSONObject();
            obj.put("id", a.getId());
            obj.put("username", a.getUsername());
            obj.put("access_hash", a.getAccess_hash());
            obj.put("first_name", a.getFirst_name());
            obj.put("last_name", a.getLast_name());
            list.add(obj);

        }

        try (FileWriter file = new FileWriter(p)) {

            file.write(list.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void start() {
        if (accountTable.getSelectionModel().getSelectedItems().size() == 1)
            manager.createNewTask(new BotInterface.POJO.Settings(), testMessage.getText(), accountTable.getSelectionModel().getSelectedItem());
        else
            manager.createNewTask(new BotInterface.POJO.Settings(), testMessage.getText(), accountTable.getSelectionModel().getSelectedItems());
    }

    @FXML
    public void stop() {
            manager.stop();
    }

    @FXML
    private void loadFromFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home") + "/Desktop")
        );
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            openJson(file);
        }
    }

    private void openJson(File file) {
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

                if (i > 300)
                    break;
            }
            FXCollections.reverse(usersData);
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }


    @FXML
    public void addAccount() {
        try {
            opeNewAccount();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    NewAccountController newAccountController;
    boolean newAccountControllerisOpen = false;

    public void opeNewAccount() throws IOException {

        if (!newAccountControllerisOpen) {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/BotInterface/NewAccountAuth/newAccount.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            newAccountController = fxmlLoader.getController();
            Stage stageNewAccountController = new Stage();
            stageNewAccountController.setScene(new Scene(root1, 400, 500));
            stageNewAccountController.setResizable(false);
            stageNewAccountController.show();
            stageNewAccountController.setOnHiding(event -> {
                newAccountControllerisOpen = false;
            });
            newAccountControllerisOpen = true;
            newAccountController.getCurrentAcc().addListener(((observable, oldValue, newValue) -> {
                accountsData.add(newValue);
            }));

        }
    }

}

