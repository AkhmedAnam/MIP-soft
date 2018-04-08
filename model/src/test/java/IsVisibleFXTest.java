import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class IsVisibleFXTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        Button ok = new Button("OK");
        Button cancel = new Button("CANCEL");
        cancel.managedProperty().bind(cancel.visibleProperty());
        borderPane.setCenter(ok);
        borderPane.setTop(cancel);
        ok.setOnAction(new EventHandler<ActionEvent>() {
            boolean wasOkPressed = false;
            @Override
            public void handle(ActionEvent event) {
                cancel.setVisible(wasOkPressed);
                wasOkPressed = !wasOkPressed;
                borderPane.autosize();
            }
        });
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
//        primaryStage.setResizable(true);
        primaryStage.show();
    }
}
