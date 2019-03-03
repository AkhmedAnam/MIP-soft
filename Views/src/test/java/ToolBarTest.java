import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ToolBarTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        final BorderPane borderPane = new BorderPane();
        Button button = new Button("BBB");
        ToolBar toolBar = new ToolBar();
        toolBar.managedProperty().bind(toolBar.visibleProperty());
        ToggleGroup toggleGroup = new ToggleGroup();
        for (int i = 0; i < 20; i++) {
            ToggleButton b = new ToggleButton("Text_" + String.valueOf(i));
            b.setToggleGroup(toggleGroup);
            toolBar.getItems().addAll(b, new Separator());
        }
        button.setOnAction(event -> {
            if(toolBar.isVisible())
                toolBar.setVisible(false);
            else
                toolBar.setVisible(true);
        });
        borderPane.setTop(toolBar);
        borderPane.setCenter(button);
        primaryStage.setScene(new Scene(borderPane, 400, 500));
        primaryStage.show();
    }
}
