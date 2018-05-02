package ru.nsi.mipsoft.model.api;

import com.pixelmed.display.SourceImage;
import javafx.scene.image.WritableImage;

import java.util.Collection;
import java.util.List;


public interface ISeriesDicomDataHolder extends IObjectWithID<String> {

    DicomImageModality getImageModality();

    List<DicomImageWrapper> getAllImages();

}
