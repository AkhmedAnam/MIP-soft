import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class ImageViewActualSIzeTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        ImageView imageView = new ImageView();
        Image image = new Image(new FileInputStream(new File("C:\\Users\\ahmed\\Desktop\\dog.jpg")));
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(150);
        BorderPane borderPane1 = new BorderPane(imageView);
        borderPane1.setStyle("-fx-border-color: blue;");
        Button button = new Button("Get size");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Bounds boundsInParent = imageView.getBoundsInParent();
                final double width = boundsInParent.getWidth();
                final double height = boundsInParent.getHeight();
//                final double width = imageView.getFitWidth();
//                final double height = imageView.getFitHeight();
                System.out.println("(" + width + "; " + height + ")");
            }
        });
        borderPane.setCenter(borderPane1);
        borderPane.setBottom(button);
        borderPane.setStyle("-fx-border-color: red;");
        Scene scene = new Scene(borderPane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
