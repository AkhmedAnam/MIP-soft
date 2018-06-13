package ru.nsi.mipsoft.model.api;

public interface IDicomFileParser {

    String getStudyID();

    String getSeriesID();

    String getPersonID();

    ImageSliceOrientation getImageSliceOrientation();

    DicomImageModality getDicomImageModality();

}
