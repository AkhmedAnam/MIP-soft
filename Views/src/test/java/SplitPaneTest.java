import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SplitPaneTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane rootPane = new BorderPane();
        SplitPane splitPane = new SplitPane();
        TextArea textArea = new TextArea();
        TextArea textArea1 = new TextArea();
        splitPane.getItems().addAll(textArea, textArea1);
        rootPane.setCenter(splitPane);
        primaryStage.setScene(new Scene(rootPane, 600, 600));
        primaryStage.show();

    }
}
