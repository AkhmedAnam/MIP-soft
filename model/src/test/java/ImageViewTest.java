import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileInputStream;

public class ImageViewTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        final Image image = new Image(new FileInputStream(new File("C:\\users\\ahmed\\Desktop\\girl.png")));
        final double width = image.getWidth();
        final double height = image.getHeight();
        ImageView imageView = new ImageView(image);
        final BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-width: 5");
        borderPane.setStyle("-fx-background-color: red");
        borderPane.setCenter(imageView);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                final ImageView source = (ImageView) event.getSource();
                final double fitWidth = source.getFitWidth();
                final double fitHeight = source.getFitHeight();
                final double x = event.getX();
                final double y = event.getY();
                final double percX = x / fitWidth;
                final double percH = y / fitHeight;
                System.out.println(String.format("x = %f; y = %f", percX * width, percH * height));
            }
        });
        imageView.fitWidthProperty().bind(borderPane.widthProperty());
        imageView.fitHeightProperty().bind(borderPane.heightProperty());
        Scene scene = new Scene(borderPane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ObjectProperty imgP = new SimpleObjectProperty();
}
