import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TransparentCanvasTest extends Application {
    private Canvas canvas;
    final Image image = new Image(new FileInputStream(new File("C:\\users\\ahmed\\Desktop\\girl.png")));

    public TransparentCanvasTest() throws FileNotFoundException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        canvas = new Canvas();
        canvas.widthProperty().bind(borderPane.widthProperty());
        canvas.heightProperty().bind(borderPane.heightProperty());
        borderPane.setCenter(canvas);
        borderPane.setOnMouseClicked(event -> {
            final double width = canvas.getWidth();
            final double height = canvas.getHeight();
            final GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
            graphicsContext2D.drawImage(image, 0, 0, width, height);
            graphicsContext2D.setStroke(new Color(1, 0, 0, 0.3));
            graphicsContext2D.strokeLine(width / 2, 0, width / 2, height);
        });
        primaryStage.setScene(new Scene(borderPane));
        primaryStage.show();
    }
}
