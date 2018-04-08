package ru.nsi.mipsoft.model;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class DicomDataHolder {
    private Map<String, Map<String, String[]>> personToModalityToImagesMap;

    {
        personToModalityToImagesMap = new HashMap<>();
    }

    public DicomDataHolder(File dicomFolder){
    }

    public DicomDataHolder(){

    }
}
