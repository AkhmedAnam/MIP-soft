import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class ExpandImageViewTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        final ImageView imageView = new ImageView();
        Image image = new Image(new FileInputStream(new File("C:\\users\\ahmed\\Desktop\\girl.png")));
        height = image.getHeight();
        width = image.getWidth();
        imageView.setPreserveRatio(true);
        imageView.setImage(image);
        imageView.setFitWidth(50);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ImageView iv = new ImageView();
                iv.setPreserveRatio(true);
                iv.imageProperty().bind(imageView.imageProperty());
                iv.setFitWidth(width);
                Stage stage = new Stage();
                stage.initOwner(primaryStage);
                stage.initModality(Modality.APPLICATION_MODAL);
                final BorderPane root = new BorderPane();
                root.setCenter(iv);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            }
        });
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(imageView);
        primaryStage.setScene(new Scene(borderPane));
        primaryStage.show();
    }

    private double width;
    private double height;
}
