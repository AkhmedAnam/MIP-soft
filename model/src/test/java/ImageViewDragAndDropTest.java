import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

public class ImageViewDragAndDropTest extends Application{
    File iV1 = new File("C:\\users\\ahmed\\Desktop\\imageView1.jpg");
    File iV2 = new File("C:\\users\\ahmed\\Desktop\\imageView2.jpg");
    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = new GridPane();
        ImageView imageView1 = new ImageView(new Image(new FileInputStream(iV1)));
        BorderPane bp1 = new BorderPane();
        bp1.setCenter(imageView1);
        ImageView imageView2 = new ImageView(new Image(new FileInputStream(iV2)));
        BorderPane bp2 = new BorderPane();
        bp2.setCenter(imageView2);
        gridPane.add(bp1, 0, 0);
        gridPane.add(bp2, 1, 0);

        imageView1.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                final Dragboard dragboard = imageView1.startDragAndDrop(TransferMode.COPY);
                final ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putImage(imageView1.getImage());
                dragboard.setContent(clipboardContent);
                event.consume();
            }
        });
        imageView2.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(event.getGestureSource() != imageView2 && event.getDragboard().hasImage()){
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            }
        });
        imageView2.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(event.getGestureSource() != imageView2 && event.getDragboard().hasImage()){
                    bp2.setStyle("-fx-border-color: red");
                }
                event.consume();
            }
        });
        imageView2.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                bp2.setStyle("-fx-border-color: transparent");
            }
        });
        imageView2.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                final Dragboard db = event.getDragboard();
                boolean isOk = false;
                if(db.hasImage()){
                    final Image image = db.getImage();
                    final Image sourceImg = imageView1.getImage();
                    final int width = (int) image.getWidth();
                    final int height = (int) image.getHeight();
                    WritableImage result = new WritableImage(width, height);
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            final Color srcColor = sourceImg.getPixelReader().getColor(i, j);
                            final Color targetColor = image.getPixelReader().getColor(i, j);
                            final int srcBlue = (int) (srcColor.getBlue() * 255);
                            final int srcRed = (int) (srcColor.getRed() * 255);
                            final int srcGreen = (int) (srcColor.getGreen() * 255);
                            final int trgtBlue = (int) (targetColor.getBlue() * 255 * 0.5);
                            final int trgtRed = (int) (targetColor.getRed() * 255 * 0.5);
                            final int trgtGreen = (int) (targetColor.getGreen() * 255 * 0.5);
                            int resultBlue = srcBlue + trgtBlue;
                            int resultRed = srcRed + trgtRed;
                            int resultGreen = srcGreen + trgtGreen;
                        }
                    }
                }
            }
        });

        Scene scene = new Scene(gridPane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
