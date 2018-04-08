package ru.nsi.mipsoft.model.api;

public interface IDicomFileParser {

    ImageSliceOrientation getImageToPatientOrientation();

    int[] getPixelData();

    int getImagePerFileCount();

    int getImagePixelWidth();

}
