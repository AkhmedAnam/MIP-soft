package ri.nsi.mipsoft.views.controls;


import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import ru.nsi.mipsoft.model.api.DicomImageWrapper;
import ru.nsi.mipsoft.model.api.Percent;
import ru.nsi.mipsoft.model.api.PixelsIntensityRegionOfInterestBean;

import java.util.List;

public class DicomImagesViewer extends ImageView {

    public DicomImagesViewer(List<DicomImageWrapper> allImages) {
        super();
        this.allImages = allImages;
        if (!this.allImages.isEmpty()) {
            final DicomImageWrapper dicomImageWrapper = this.allImages.get(currentIndex);
            final short bitsStored = dicomImageWrapper.getBitsStored();
            maximumPixelIntensity = (int) (Math.pow(2, bitsStored) - 1);
            PixelsIntensityRegionOfInterestBean intensityRegionOfInterestBean = new PixelsIntensityRegionOfInterestBean(
                    maximumPixelIntensity,
                    new Percent(1),
                    new Percent(0.5),
                    PixelsIntensityRegionOfInterestBean.NORMALIZED_VALUE_DEFUALT
            );
            final WritableImage writableImage = dicomImageWrapper.getWritableImage(intensityRegionOfInterestBean);
            setImage(writableImage);
        } else {
            maximumPixelIntensity = 0;
        }
        EventHandler<MouseEvent> mouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!event.isPrimaryButtonDown()) {
                    return;
                }
                final DicomImagesViewer source = (DicomImagesViewer) event.getSource();
                final Bounds boundsInParent = source.getBoundsInParent();
                final double width = boundsInParent.getWidth();
                final double height = boundsInParent.getHeight();
                final double x = event.getX();
                final double y = event.getY();
                if (x < 0 || x > width || y < 0 || y > height) {
                    return;
                }
                final Percent windowWidthPersent = new Percent(x / width);
                final Percent windowLevelPerscent = new Percent(y / height);
                final DicomImageWrapper dicomImageWrapper = allImages.get(currentIndex);
//            final short bitsStored = dicomImageWrapper.getBitsStored();
//            final int maxIntesity = (int) (Math.pow(2, bitsStored) - 1);
                final PixelsIntensityRegionOfInterestBean intensityRegionOfInterestBean = new PixelsIntensityRegionOfInterestBean(
                        maximumPixelIntensity,
                        windowWidthPersent,
                        windowLevelPerscent,
                        PixelsIntensityRegionOfInterestBean.NORMALIZED_VALUE_DEFUALT
                );
                final WritableImage writableImage = dicomImageWrapper.getWritableImage(
                        intensityRegionOfInterestBean
                );
                DicomImagesViewer.this.setImage(writableImage);
                lastPixelsIntensityRegionOfInterestBean = intensityRegionOfInterestBean;
            }
        };
        setOnMouseDragged(mouseDraggedEventHandler);
        EventHandler<ScrollEvent> mouseWheelScrollEventHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                final double deltaY = event.getDeltaY();
                final int imagesCount = allImages.size();
                int newIndex;
                if (deltaY > 0) {
                    newIndex = currentIndex + 1;
                    if (newIndex >= imagesCount) {
                        return;
                    }
                } else {
                    newIndex = currentIndex - 1;
                    if (newIndex < 0) {
                        return;
                    }
                }
                currentIndex = newIndex;
                final DicomImageWrapper dicomImageWrapper = allImages.get(currentIndex);
                final WritableImage writableImage = dicomImageWrapper.getWritableImage(lastPixelsIntensityRegionOfInterestBean);
                setImage(writableImage);
            }
        };
        setOnScroll(mouseWheelScrollEventHandler);
        lastPixelsIntensityRegionOfInterestBean = new PixelsIntensityRegionOfInterestBean(
                maximumPixelIntensity,
                new Percent(0.5),
                new Percent(0.5),
                PixelsIntensityRegionOfInterestBean.NORMALIZED_VALUE_DEFUALT
        );
    }

    private final List<DicomImageWrapper> allImages;
    private int currentIndex = 0;
    private final int maximumPixelIntensity;
    private PixelsIntensityRegionOfInterestBean lastPixelsIntensityRegionOfInterestBean;
}
