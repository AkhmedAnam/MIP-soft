package ru.nsi.mipsoft.model;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import ru.nsi.mipsoft.model.api.DicomFileParserImpl;
import ru.nsi.mipsoft.model.api.IPatientDicomDataHolder;
import ru.nsi.mipsoft.model.api.PatientDicomDataHolderImpl;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DicomDataHolder {

    {
        allPatients = new ArrayList<>();
    }

    public DicomDataHolder(File dicomFolder) {
        final File[] dicomFiles = dicomFolder.listFiles();
        if (dicomFiles == null || dicomFiles.length == 0) {
            throw new RuntimeException("В выбранной папке не обнаружено DICOM файлов");
        }
        AttributeList attributeList = new AttributeList();
        Map<String, Map<String, Map<String, List<String>>>> patientIdToStudyIdToSeriesIdToImagesPathMap = new HashMap<>();
        for (File dicomFile : dicomFiles) {
            try {
                attributeList.read(dicomFile);
                final String dicomFileAbsolutePath = dicomFile.getAbsolutePath();
                DicomFileParserImpl dicomFileParser = new DicomFileParserImpl(attributeList);
                final String personID = dicomFileParser.getPersonID();
                final String studyID = dicomFileParser.getStudyID();
                final String seriesID = dicomFileParser.getSeriesID();
                if (patientIdToStudyIdToSeriesIdToImagesPathMap.containsKey(personID)) {
                    final Map<String, Map<String, List<String>>> patientMap = patientIdToStudyIdToSeriesIdToImagesPathMap.get(personID);
                    if (patientMap.containsKey(studyID)) {
                        final Map<String, List<String>> seriesToImagesMapForPatient = patientMap.get(studyID);
                        if (seriesToImagesMapForPatient.containsKey(seriesID)) {
                            final List<String> imagesForPatient = seriesToImagesMapForPatient.get(seriesID);
                            imagesForPatient.add(dicomFileAbsolutePath);
                        } else {
                            final ArrayList<String> imagesForPatient = new ArrayList<>();
                            imagesForPatient.add(dicomFileAbsolutePath);
                            seriesToImagesMapForPatient.put(seriesID, imagesForPatient);
                        }
                    } else {
                        final HashMap<String, List<String>> seriesToImagesMap = new HashMap<>();
                        final ArrayList<String> imagesForPatient = new ArrayList<>();
                        imagesForPatient.add(dicomFileAbsolutePath);
                        seriesToImagesMap.put(seriesID, imagesForPatient);
                        patientMap.put(studyID, seriesToImagesMap);
                    }
                } else {
                    final HashMap<String, Map<String, List<String>>> patientMap = new HashMap<>();
                    final HashMap<String, List<String>> seriesToImagesMap = new HashMap<>();
                    final ArrayList<String> imagesForPatient = new ArrayList<>();
                    imagesForPatient.add(dicomFileAbsolutePath);
                    seriesToImagesMap.put(seriesID, imagesForPatient);
                    patientMap.put(studyID, seriesToImagesMap);
                    patientIdToStudyIdToSeriesIdToImagesPathMap.put(personID, patientMap);
                }
                for (Map.Entry<String, Map<String, Map<String, List<String>>>> patientEntrySet : patientIdToStudyIdToSeriesIdToImagesPathMap.entrySet()) {
                    final String patientID = patientEntrySet.getKey();
                    final Map<String, Map<String, List<String>>> patientStudiesMap = patientEntrySet.getValue();
                    PatientDicomDataHolderImpl patientDicomDataHolder = new PatientDicomDataHolderImpl(patientID, patientStudiesMap);
                    allPatients.add(patientDicomDataHolder);
                }
            } catch (DicomException e) {
                //todo logger
            } catch (IOException e) {
                //todo logger
            }
        }
    }

    public IPatientDicomDataHolder getPatientDataHolder(String patientID) {
        final Optional<PatientDicomDataHolderImpl> optionalResult = allPatients.stream()
                .filter(patientDicomDataHolder -> patientDicomDataHolder.getID().equals(patientID))
                .findFirst();
        return optionalResult.isPresent() ? optionalResult.get() : null;
    }


    private final List<PatientDicomDataHolderImpl> allPatients;

}
