import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TabPaneTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab tab = new Tab("First");
        tab.setContent(new TextArea("Ahmed"));
        Tab tab1 = new Tab("Second");
        tab1.setContent(new TextArea("Nadir"));
        tabPane.getTabs().addAll(tab, tab1);
        primaryStage.setScene(new Scene(tabPane, 600, 600));
        primaryStage.show();
    }
}
