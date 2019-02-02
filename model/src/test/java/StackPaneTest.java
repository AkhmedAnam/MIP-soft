import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class StackPaneTest extends Application {
    public StackPaneTest() throws FileNotFoundException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: red");
        stackPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stackPane.setCursor(imageCursor);
            }
        });
        stackPane.setOnMouseExited(event -> stackPane.setCursor(Cursor.DEFAULT));
//        BorderPane borderPane = new BorderPane();
        ImageView imageView = new ImageView(new Image(new FileInputStream(new File("C:\\users\\ahmed\\Desktop\\imageView1.jpg"))));
        ImageView imageView1 = new ImageView(new Image(new FileInputStream(new File("C:\\users\\ahmed\\Desktop\\imageView2.jpg"))));
        imageView1.setOpacity(0);
        imageView.setPreserveRatio(true);
        //borderPane.setCenter(imageView);
       //imageView.fitWidthProperty().bind(borderPane.widthProperty());
       // imageView.fitHeightProperty().bind(borderPane.heightProperty());

       // borderPane.setStyle("-fx-background-color: red");
        stackPane.getChildren().addAll(imageView, imageView1);
        stackPane.maxWidthProperty().bind(imageView.fitWidthProperty());
        stackPane.maxHeightProperty().bind(imageView.fitHeightProperty());
        Slider slider = new Slider(0, 1, 0);
        TextField textField = new TextField();
        textField.setStyle("-fx-border-width: 10");
        textField.setMaxSize(400, 50);
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                boolean isOk = false;
                try{
                    if (!newValue.isEmpty()) {
                        Double.parseDouble(newValue.replace(',', '.'));
                    } else {
                        textField.setText("0");
                    }
                    isOk = true;
                } catch (Exception e){
                    isOk = false;
                }
                final Tooltip tooltip = new Tooltip();
                if(!isOk){
                    textField.setText(oldValue);
                    tooltip.setText("Ошибка: необходимо ввести вещественное число от 0 до 1");
                    textField.setTooltip(tooltip);
                    fadeTransition.setNode(textField);
                    fadeTransition.setCycleCount(Animation.INDEFINITE);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.1);
                    fadeTransition.setAutoReverse(true);
                    textField.setStyle("-fx-border-color: red");
                    fadeTransition.play();
                } else {
                    fadeTransition.stop();
                    textField.setTooltip(null);
                    textField.setStyle("-fx-border-color: transparent");
                    textField.setOpacity(1);
                }
            }
        });
        Bindings.bindBidirectional(textField.textProperty(), slider.valueProperty(), new NumberStringConverter());
        imageView1.opacityProperty().bind(slider.valueProperty());
        vBox.getChildren().addAll(stackPane, slider, textField);
        Scene scene = new Scene(vBox, 500, 500);
        primaryStage.setScene(scene);
//        primaryStage.setFullScreen(true);
//        primaryStage.setFullScreenExitHint("Olololo");
//        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000));
    ImageCursor imageCursor = new ImageCursor(new Image(new FileInputStream(new File("C:\\users\\ahmed\\Desktop\\Lasso-Tool-512.png"))));
}
