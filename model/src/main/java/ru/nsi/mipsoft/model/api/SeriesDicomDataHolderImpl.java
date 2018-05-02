package ru.nsi.mipsoft.model.api;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SeriesDicomDataHolderImpl implements ISeriesDicomDataHolder {

    public SeriesDicomDataHolderImpl(String id, List<String> imagesPath) throws IOException, DicomException {
        ID = id;
        allImages = new ArrayList<>();
        AttributeList attributeList = new AttributeList();
        for (String imagePath : imagesPath) {
            final File dicomFile = new File(imagePath);
            attributeList.read(dicomFile);
            DicomImageWrapper dicomImageWrapper = new DicomImageWrapper(attributeList);
            final DicomImageModality currentImageModality = dicomImageWrapper.getImageModality();
            if(this.imageModality == null) {
                this.imageModality = currentImageModality;
            } else {
                if(this.imageModality != currentImageModality){
                    throw  new DicomException("Ошибка при создании объекта типа SeriesDicomDataHolderImpl: Модальность одного из изображений не совпадает с предыдущими.");
                }
            }
            allImages.add(dicomImageWrapper);
        }
    }

    @Override
    public DicomImageModality getImageModality() {
        return imageModality;
    }

    @Override
    public List<DicomImageWrapper> getAllImages() {
        return allImages;
    }

    @Override
    public String getID() {
        return ID;
    }

    private DicomImageModality imageModality = null;
    private final String ID;
    private final List<DicomImageWrapper> allImages;
}
