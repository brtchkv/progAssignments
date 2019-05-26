package client.Controllers;

import client.BackTable;
import client.Login;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import shared.Response;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;


public class SkeletonMain implements Initializable {
    @FXML
    private Label collectionInfo;

    @FXML
    private Label nickname;

    @FXML
    private ComboBox<String> commandsList;

    @FXML
    private TextField humanName;

    @FXML
    private TextField humanAge;

    @FXML
    private TextField skillName;

    @FXML
    private TextField skillInfo;

    @FXML
    private TextField xCoordinate;

    @FXML
    private TextField yCoordinate;

    @FXML
    private Label lastCommand;

    @FXML
    private Label lastHumanName;

    @FXML
    private Slider slider;

    @FXML
    private Label labelSize;

    Timer timer;

    private int size = 1;
    private int x = 0;
    private int y = 0;

    private void init() {
        collectionInfo.setAlignment(Pos.CENTER_LEFT);
        nickname.setAlignment(Pos.CENTER_RIGHT);
        lastCommand.setAlignment(Pos.CENTER_LEFT);
        lastHumanName.setAlignment(Pos.CENTER_LEFT);
        nickname.setText(SkeletonLogin.getNickname());
        collectionInfo.setText("Collection of Vector type. Contains objects of human type.");
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        timer = new Timer();
        timer.schedule(new BackTable(), 0, 3000);
        commandsList.getItems().addAll(
                Login.currentResource.getString("add"),
                Login.currentResource.getString("remove"),
                Login.currentResource.getString("removeGreater"),
                Login.currentResource.getString("removeLower"),
                Login.currentResource.getString("addIfMin"));
        slider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                labelSize.setText(String.format("%.2f", newValue));
                size = newValue.intValue();
            }
        });
    }

    @FXML
    void clear(ActionEvent event) {
        humanName.clear();
        humanAge.clear();
        skillName.clear();
        skillInfo.clear();
        commandsList.setValue(null);
        slider.setValue(slider.getMin());
        labelSize.setText("0");
    }

    @FXML
    void day(ActionEvent event) {
        timer.cancel();
        Stage stageP = (Stage) nickname.getScene().getWindow();
        stageP.close();
        Stage stage = new Stage();
        stage.setTitle(Login.currentResource.getString("main"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client/UI/MainUIDay.fxml"));
        Login.loadScene(stage, loader);
    }

    @FXML
    void night(ActionEvent event) {
        timer.cancel();
        Stage stageP = (Stage) nickname.getScene().getWindow();
        stageP.close();
        Stage stage = new Stage();
        stage.setTitle(Login.currentResource.getString("main"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client/UI/MainUINight.fxml"));
        Login.loadScene(stage, loader);

    }

    @FXML
    void fun(ActionEvent event) {
        ImageView imageView = new ImageView();
        ButtonType sorry = new ButtonType("Sorry");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Why?!");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(sorry);
        File file = new File("/Users/ivan/OneDrive - ITMO UNIVERSITY/�����/6/Lab/src/client/UI/cat.jpg");
        Image image = new Image(file.toURI().toString(), 400, 400, true, true);
        imageView.setImage(image);
        alert.setGraphic(imageView);
        alert.showAndWait();
    }

    @FXML
    void showHelp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Login.currentResource.getString("help"));
        alert.setContentText(Login.currentResource.getString("helpText"));
        alert.showAndWait();
    }

    @FXML
    void showTable(ActionEvent event) {
        VBox vbox;
        if (BackTable.tableView != null) {
            vbox = new VBox(BackTable.tableView);
        } else {
            TableView tb = new TableView();
            TableColumn firstNameCol = new TableColumn("Name");
            TableColumn age = new TableColumn("Age");
            TableColumn u = new TableColumn("Username");
            tb.getColumns().addAll(firstNameCol, age, u);
            vbox = new VBox(tb);
        }

        vbox.setSpacing(5);
        Scene scene = new Scene(vbox);
        Stage stage = new Stage();
        stage.setTitle("Objects");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void submit(ActionEvent event) {

        boolean command = false;

        try {

            String gson = "";
            if (humanName.getText() == null || humanAge.getText() == null || humanName.getText().isEmpty() || humanAge.getText().isEmpty() || commandsList.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(Login.currentResource.getString("command"));
                alert.setContentText("The fields command, name and age can't be void.\nMake sure they're set.");
                alert.showAndWait();
            } else {

                if ((xCoordinate.getText() == null || xCoordinate.getText().isEmpty()) && (yCoordinate.getText() == null || yCoordinate.getText().isEmpty())){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(Login.currentResource.getString("command"));
                    alert.setContentText("You didn't provide any coordinates. Thus the default preset was used!\n(0,0)");
                    alert.showAndWait();
                } else if ((xCoordinate.getText() != null && !xCoordinate.getText().isEmpty()) && (yCoordinate.getText() == null || yCoordinate.getText().isEmpty())) {
                    x = Integer.parseInt(xCoordinate.getText());
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(Login.currentResource.getString("command"));
                    alert.setContentText("You didn't provide Y coordinates. Thus the default preset was used!\n Y = 0");
                    alert.showAndWait();
                } else if ((yCoordinate.getText() != null && !yCoordinate.getText().isEmpty()) && (xCoordinate.getText() == null || xCoordinate.getText().isEmpty())){
                    y = Integer.parseInt(yCoordinate.getText());
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(Login.currentResource.getString("command"));
                    alert.setContentText("You didn't provide X coordinates. Thus the default preset was used!\n X = 0");
                    alert.showAndWait();
                } else {
                    x = Integer.parseInt(xCoordinate.getText());
                    y = Integer.parseInt(yCoordinate.getText());
                }

                if ((skillName.getText() == null || skillName.getText().isEmpty()) && skillInfo.getText()!= null && !skillInfo.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(Login.currentResource.getString("command"));
                    alert.setContentText("You can't provide skill info and don't provide skill name\nTry again!");
                    alert.showAndWait();
                } else if ((skillName.getText() == null || skillName.getText().isEmpty()) && (skillInfo.getText() == null || skillInfo.getText().isEmpty())){
                    gson = "{\"name\":\"" + humanName.getText() +"\", \"age\":" + humanAge.getText() + ", \"size\":" + size + ", \"x\": " + x + ", \"y\": " + y +"}";
                    command = true;
                } else if (!(skillName.getText() == null || skillName.getText().isEmpty()) && (skillInfo.getText() == null || skillInfo.getText().isEmpty())){
                    gson = "{\"name\":\"" + humanName.getText() +"\", \"age\":" + humanAge.getText() + ", \"size\":" + size + ", \"x\": " + x + ", \"y\": "
                            + y + ", \"skill\": {" + "\"name\": \"" + skillName.getText() + "\"}"  +"}";
                    command = true;
                } else {
                    gson = "{\"name\":\"" + humanName.getText() +"\", \"age\":" + humanAge.getText() + ", \"size\":" + size + ", \"x\": " + x + ", \"y\": "
                            + y + ", \"skill\": {" + "\"name\": \"" + skillName.getText() + "\""
                            + "\"info\": \"" + skillInfo.getText() +"\"}"  +"}";
                    command = true;
                }

                if (command) {

                    lastCommand.setText(commandsList.getValue());
                    lastHumanName.setText(humanName.getText());

                    SkeletonLogin.client.udpSocket.send(SkeletonLogin.client.createRequest(commandsList.getValue().replaceAll("\\s+", "_").toLowerCase(), gson, SkeletonLogin.getNickname() + " " + server.DataBaseConnection.encryptString(SkeletonLogin.getPassword())));
                    byte[] resp = new byte[8192];
                    DatagramPacket responsePacket = new DatagramPacket(resp, resp.length);
                    SkeletonLogin.client.udpSocket.receive(responsePacket);

                    try (ByteArrayInputStream bais = new ByteArrayInputStream(resp);
                         ObjectInputStream ois = new ObjectInputStream(bais)) {
                        Response response = (Response) ois.readObject();
                        String output = new String(SkeletonLogin.client.decodeResponse(commandsList.getValue(), response));
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(Login.currentResource.getString("command"));
                        alert.setContentText(output);
                        alert.showAndWait();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(Login.currentResource.getString("serverConnection"));
            alert.setContentText(Login.currentResource.getString("disconnected"));
            alert.showAndWait();
        }

    }

    @FXML
    void language1(ActionEvent event) {
        timer.cancel();
        Window stageP = nickname.getScene().getWindow();
        stageP.hide();
        Stage stage = new Stage();
        stage.setTitle(Login.currentResource.getString("main"));
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(ResourceBundle.getBundle("client.Localisation.MyResources",
                new Locale("ru", "Ru")));
        loader.setLocation(getClass().getClassLoader().getResource("client/UI/login.fxml"));
        Login.loadScene(stage, loader);
    }

    @FXML
    void language2(ActionEvent event) {

    }

    @FXML
    void language3(ActionEvent event) {

    }

    @FXML
    void language4(ActionEvent event) {

    }

    @FXML
    void logOut(ActionEvent event) {
        timer.cancel();
        Window stageP = nickname.getScene().getWindow();
        stageP.hide();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client/UI/login.fxml"));
        Login.loadScene(stage, loader);
    }

}