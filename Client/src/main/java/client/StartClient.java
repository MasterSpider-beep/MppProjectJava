package client;

import GUI.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.IService;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {

    public static void main(String[] args) {
        launch();
    }

    private Stage primaryStage;
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        IService service = new ServiceProxy(serverIP,serverPort);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(StartClient.class.getResource("/views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LogInController controller = fxmlLoader.getController();
        controller.setService(service);

        primaryStage.setScene(scene);
        primaryStage.setTitle("LogIn");
        primaryStage.show();
    }
}
