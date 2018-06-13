
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestMousePressed extends Application {
//    private boolean isLeftBtnPressed = false;
//
//    public boolean isLeftBtnPressed() {
//        return isLeftBtnPressed;
//    }
//
//    public void setLeftBtnPressed(boolean leftBtnPressed) {
//        isLeftBtnPressed = leftBtnPressed;
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        final Button b1 = new Button("B1");
        final Button b2 = new Button("B2");
        b1.setPrefSize(150, 150);
        b2.setPrefSize(150, 150);
        borderPane.setCenter(b1);
        borderPane.setBottom(b2);
        final EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!event.isPrimaryButtonDown()){
                    return;
                }
                final Button sourceBtn = (Button) event.getSource();
                final double width = sourceBtn.getWidth();
                final double height = sourceBtn.getHeight();
                final double x = event.getX();
                final double y = event.getY();
                if (x >= 0 && x <= width && y >= 0 && y <= height) {
                    System.out.println("(" + x +"; " + y + ")");
                }
            }
        };
//        b1.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                setLeftBtnPressed(true);
//            }
//        });
//        b1.setOnMouseReleased(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                setLeftBtnPressed(false);
//            }
//        });
        b1.setOnMouseDragged(eventHandler);
        b2.setOnMouseDragged(eventHandler);
//        b1.setOnMouseMoved(eventHandler);
        b1.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                final double deltaY = event.getDeltaY();
                System.out.println("deltaY = " + deltaY);
            }
        });
        Scene scene = new Scene(borderPane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(TestMousePressed.class, args);
    }
}
