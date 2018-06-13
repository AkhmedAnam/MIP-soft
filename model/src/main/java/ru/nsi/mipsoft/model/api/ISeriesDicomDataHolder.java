package ru.nsi.mipsoft.model.api;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


public interface ISeriesDicomDataHolder extends IObjectWithID<String> {

    DicomImageModality getImageModality();

    List<DicomImageWrapper> getAllImages();

    List<DicomImageWrapper> getAllImageForImageSliceOrientation(ImageSliceOrientation imageSliceOrientation);

    void initialize(List<String> imagesPath) throws DicomException, IOException;

    ObjectProperty<Image> axialImageProperty();

    ObjectProperty<Image> coronalImageProperty();

    ObjectProperty<Image> saggitalImageProperty();
}
