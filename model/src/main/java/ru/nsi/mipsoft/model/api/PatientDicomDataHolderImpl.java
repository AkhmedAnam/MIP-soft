package ru.nsi.mipsoft.model.api;

import com.pixelmed.dicom.DicomException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PatientDicomDataHolderImpl implements IPatientDicomDataHolder {

    public PatientDicomDataHolderImpl(String id, Map<String, Map<String, List<String>>> patientMap) throws IOException, DicomException {
        ID = id;
        this.allStudies = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<String>>> studyIdToSeriesMap : patientMap.entrySet()) {
            final String studyId = studyIdToSeriesMap.getKey();
            final Map<String, List<String>> seriesMap = studyIdToSeriesMap.getValue();
            StudyDicomDataHolderImpl studyDicomDataHolder = new StudyDicomDataHolderImpl(studyId, seriesMap);
            allStudies.add(studyDicomDataHolder);
        }
    }

    @Override
    public Collection<String> getAllStudiesID() {
        return allStudies.stream()
                .map(IStudyDicomDataHolder::getID)
                .collect(Collectors.toList());
    }

    @Override
    public IStudyDicomDataHolder getStudyDicomDataHolder(String studyID) {
        final Optional<StudyDicomDataHolderImpl> resultOptional = allStudies.stream()
                .filter(iStudyDicomDataHolder -> studyID.equals(iStudyDicomDataHolder.getID()))
                .findFirst();
        return resultOptional.isPresent() ? resultOptional.get() : null;
    }

    @Override
    public String getID() {
        return ID;
    }

    private final String ID;
    private final List<StudyDicomDataHolderImpl> allStudies;
}
