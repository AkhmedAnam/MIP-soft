package ri.nsi.mipsoft.views.controls;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import ru.nsi.mipsoft.model.api.ImageSliceOrientation;

public class SliceView extends BorderPane {
    private final Canvas canvas = new Canvas();
    private final ImageSliceOrientation sliceOrientation;

    public SliceView(ImageSliceOrientation sliceOrientation){
        this.sliceOrientation = sliceOrientation;

    }
}
