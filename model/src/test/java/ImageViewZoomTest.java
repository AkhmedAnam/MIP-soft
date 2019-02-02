import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageViewZoomTest extends Application {

    public ImageViewZoomTest(){
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        zoomProperty = new SimpleDoubleProperty(1);
        try {
            final Image image = new Image(new FileInputStream(new File("C:\\users\\ahmed\\Desktop\\girl.png")));
            initWidth = image.getWidth();
            initHeight = image.getHeight();
            imageView.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        zoomProperty.addListener((observable, oldValue, newValue) -> {
            final double val = newValue.doubleValue();
            imageView.setFitWidth(initWidth * val);
            imageView.setFitHeight(initHeight * val);
        });
        imageView.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                final boolean controlDown = event.isControlDown();
                if(!controlDown){
                    return;
                }
                final double deltaY = event.getDeltaY();
                final double zoom = zoomProperty.get();
                if(deltaY > 0){
                    zoomProperty.setValue(zoom * 2);
                } else if(deltaY < 0){
                    zoomProperty.setValue(zoom / 2);
                }
            }
        });
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(imageView);
//        scrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
//            @Override
//            public void handle(ScrollEvent event) {
//                final boolean controlDown = event.isControlDown();
//            }
//        });
//        scrollPane.setOnScroll(new EventHandler<ScrollEvent>() {
//            @Override
//            public void handle(ScrollEvent event) {
//
//            }
//        });
        Scene scene = new Scene(scrollPane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView imageView;
    private DoubleProperty zoomProperty;
    private double initWidth;
    private double initHeight;
}
