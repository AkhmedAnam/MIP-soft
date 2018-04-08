import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class TestBindingFromFXML extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
//        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\ahmed\\MIPsoft\\model\\src\\test\\java\\BindingFXML.fxml");

//        Parent root = FXMLLoader.load(fileInputStream);
//        Parent root = fxmlLoader.load(fileInputStream);
        URL resource = TestBindingFromFXML.class.getResource("BindingFXML.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        fxmlLoader.load();
        Parent root = fxmlLoader.getRoot();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
