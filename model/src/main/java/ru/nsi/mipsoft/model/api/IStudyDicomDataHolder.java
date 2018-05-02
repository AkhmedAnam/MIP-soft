package ru.nsi.mipsoft.model.api;

import com.pixelmed.display.SourceImage;

import java.util.Collection;

public interface IStudyDicomDataHolder extends IObjectWithID<String> {

    Collection<String> getAllSeriesID();

    ISeriesDicomDataHolder getSeriesDicomDataHolder(String seriesID);

}
