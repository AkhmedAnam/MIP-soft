package ru.nsi.mipsoft.model.api;

import com.pixelmed.dicom.DicomException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StudyDicomDataHolderImpl implements IStudyDicomDataHolder {


    public StudyDicomDataHolderImpl(String id, Map<String, List<String>> seriesToImagesPathListMap) throws IOException, DicomException {
        ID = id;
        allSeries = new ArrayList<>();
        for (Map.Entry<String, List<String>> stringListEntry : seriesToImagesPathListMap.entrySet()) {
            final String seriesID = stringListEntry.getKey();
            final List<String> imagesPathForTheSeries = stringListEntry.getValue();
            DicomSeriesDataHolder seriesDicomDataHolder = new DicomSeriesDataHolder(seriesID, imagesPathForTheSeries);
            allSeries.add(seriesDicomDataHolder);
        }
    }

    @Override
    public Collection<String> getAllSeriesID() {
        return allSeries.stream()
                .map(ISeriesDicomDataHolder::getID)
                .collect(Collectors.toList());
    }

    @Override
    public ISeriesDicomDataHolder getSeriesDicomDataHolder(String seriesID) {
        final Optional<DicomSeriesDataHolder> result = allSeries.stream()
                .filter(iSeriesDicomDataHolder -> seriesID.equals(iSeriesDicomDataHolder.getID()))
                .findFirst();
        return result.isPresent() ? result.get() : null;
    }

    @Override
    public String getID() {
        return ID;
    }

    private final String ID;
    private final List<DicomSeriesDataHolder> allSeries;
}
