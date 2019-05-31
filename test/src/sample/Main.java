package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.opencv.core.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root, 844, 519);
        stage.setTitle("Генерация изображений " + Core.VERSION);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        stage.show();
    }
}