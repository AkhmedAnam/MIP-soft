package ru.nsi.mipsoft.model.api;

import com.pixelmed.display.SourceImage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeriesDicomDataHolderImpl implements ISeriesDicomDataHolder {

    public SeriesDicomDataHolderImpl(DicomImageModality imagesModality, String id) {
        this.imagesModality = imagesModality;
        ID = id;
        images = new HashSet<>();
    }

    @Override
    public DicomImageModality getImageModality() {
        return imagesModality;
    }

    @Override
    public Collection<DicomImageWrapper> getAllImages() {
        return images;
    }

    @Override
    public void addImage(DicomImageWrapper image) {
        images.add(image);
    }

    @Override
    public String getID() {
        return ID;
    }

    private final DicomImageModality imagesModality;
    private final String ID;
    private final Set<DicomImageWrapper> images;
}
