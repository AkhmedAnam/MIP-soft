import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.stage.Stage;

import javax.naming.Binding;


public class DrawShapesOnPaneTest extends Application {
    private final Line xLine1 = new Line();
    private final Line xLine2 = new Line();
    private final Line yLine1 = new Line();
    private final Line yLine2 = new Line();
    private final Pane pane = new Pane();

    {
        xLine1.setStrokeWidth(3);
        xLine1.setStroke(Color.RED);
        xLine1.setOpacity(0.5);
        xLine2.setOpacity(0.5);
        xLine2.setStrokeWidth(3);
        xLine2.setStroke(Color.RED);
        xLine2.endXProperty().bind(pane.widthProperty());
        yLine1.setStrokeWidth(3);
        yLine1.setOpacity(.5);
        yLine2.setOpacity(.5);
        yLine1.setStroke(Color.BLUE);
        yLine2.setStrokeWidth(3);
        yLine2.setStroke(Color.BLUE);
        yLine2.endYProperty().bind(pane.heightProperty());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane stackPane = new StackPane();
//        stackPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> event.consume());
        stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("StackPane mouse clicked " + event.getSource().getClass() + "; " + event.getTarget().getClass());
            }
        });
        BorderPane borderPane = new BorderPane();
        borderPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Border pane mouse clicked "+ event.getSource().getClass() + "; " + event.getTarget().getClass());
            }
        });
        stackPane.getChildren().add(borderPane);
        pane.setStyle("-fx-background-color: black");
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Pane mouse clicked " + event.getSource().getClass() + "; " + event.getTarget().getClass());
                final double x = event.getX();
                final double y = event.getY();
                final double width = pane.getWidth();
                final double height = pane.getHeight();
                xLine1.setStartX(0);
                xLine1.setStartY(y);
                xLine1.setEndY(y);
                xLine1.setEndX(x - .5);

                xLine2.setStartY(y);
                xLine2.setEndY(y);
                xLine2.setStartX(x + .5);

                yLine1.setStartX(x);
                yLine1.setStartY(0);
                yLine1.setEndX(x);
                yLine1.setEndY(y - .5);

                yLine2.setStartX(x);
                yLine2.setEndX(x);
                yLine2.setStartY(y + .5);

//                Line yLine = new Line(x, 0, x, height);
//                yLine.endYProperty().bind(pane.heightProperty());
                final ObservableList<Node> children = pane.getChildren();
                children.clear();
                children.addAll(xLine1, xLine2, yLine1, yLine2);
                if (event.getClickCount() == 2) {
                    children.add(new Circle(width / 2, height / 2, 50));
                }
//                event.consume();
            }
        });
        stackPane.getChildren().add(pane);
        Scene scene = new Scene(stackPane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
