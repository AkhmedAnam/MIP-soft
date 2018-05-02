package ru.nsi.mipsoft.model.api;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.TagFromName;

public class DicomFileParserImpl implements IDicomFileParser {

    public DicomFileParserImpl(AttributeList attributeList){
        this.attributeList = attributeList;
    }

    @Override
    public String getStudyID() {
        return Attribute.getDelimitedStringValuesOrEmptyString(attributeList, TagFromName.StudyInstanceUID);
    }

    @Override
    public String getSeriesID() {
        return Attribute.getDelimitedStringValuesOrEmptyString(attributeList, TagFromName.SeriesInstanceUID);
    }

    @Override
    public String getPersonID() {
        return Attribute.getDelimitedStringValuesOrEmptyString(attributeList, TagFromName.PatientID);
    }

    private final AttributeList attributeList;
}
