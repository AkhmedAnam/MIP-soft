package ru.nsi.mipsoft.model.api;

import java.util.Collection;

public interface IPatientDicomDataHolder extends IObjectWithID<String> {

    Collection<String> getAllStudiesID();

    IStudyDicomDataHolder getStudyDicomDataHolder(String studyID);

}
