package ru.nsi.mipsoft.model.api;

import com.pixelmed.display.SourceImage;
import javafx.scene.image.WritableImage;

import java.util.Collection;


public interface ISeriesDicomDataHolder extends IObjectWithID<String> {

    DicomImageModality getImageModality();

    Collection<DicomImageWrapper> getAllImages();

    void addImage(DicomImageWrapper image);

}
