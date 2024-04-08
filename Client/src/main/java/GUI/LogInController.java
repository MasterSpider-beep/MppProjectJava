package GUI;

import client.StartClient;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import domain.User;
import exceptions.LogInException;
import service.IService;

import java.io.IOException;

public class LogInController {
    @FXML
    public TextField txtUsername;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public Button btnLogIn;
    private IService service;
    private Stage stage = null;

    ClientController clientController;

    public void setService(IService service) {
        this.service = service;
    }

    public void initialize() {
        btnLogIn.setOnAction(this::onPressLogIn);
    }

    private void showLogInError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        stage = (Stage) btnLogIn.getScene().getWindow();
        stage.close();
    }

    private void createClientController(User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(StartClient.class.getResource("views/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage1 = new Stage();
            stage1.initOwner(null);
            stage1.initModality(Modality.NONE);
            stage1.setScene(scene);
            ClientController clientController = fxmlLoader.getController();
            clientController.setService(service);
            clientController.setUser(user);
            clientController.setStage(stage1);
            stage1.setTitle("Application");
            this.clientController = clientController;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void changeToMainWindow() {
        clientController.getStage().show();
    }

    @FXML
    private void onKeyPressedUser(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            txtPassword.requestFocus();
        }
    }

    @FXML
    private void onKeyPressedPassword(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onPressLogIn(new ActionEvent());
        }
    }

    @FXML
    public void onPressLogIn(ActionEvent actionEvent) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showLogInError("Please insert your username and password!");
            return;
        }
        try {
            User user = new User(username, password);
            createClientController(user);
            service.logInUser(user, clientController);
            changeToMainWindow();
            closeWindow();
        } catch (LogInException e) {
            showLogInError(e.getMessage());
        }
    }
}
