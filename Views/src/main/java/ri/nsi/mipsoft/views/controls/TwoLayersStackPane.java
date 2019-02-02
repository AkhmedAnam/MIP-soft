package ri.nsi.mipsoft.views.controls;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;


public class TwoLayersStackPane extends StackPane {
    private final ImageView bottomLayer = new ImageView();
    private final ImageView topLayer = new ImageView();

    public TwoLayersStackPane(){
        bottomLayer.setPreserveRatio(true);
        topLayer.setPreserveRatio(true);
        topLayer.fitWidthProperty().bind(bottomLayer.fitWidthProperty());
        topLayer.fitHeightProperty().bind(bottomLayer.fitHeightProperty());

    }
}
