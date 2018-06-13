package ru.nsi.mipsoft.model.api;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DicomSeriesDataHolder implements ISeriesDicomDataHolder {

    public DicomSeriesDataHolder(String id, List<String> imagesPath) throws IOException, DicomException {
        this(id);
        initialize(imagesPath);
    }

    public DicomSeriesDataHolder(String id) throws IOException, DicomException {
        ID = id;
        imageSliceOrientationToImagesList = new HashMap<>();
    }

    @Override
    public DicomImageModality getImageModality() {
        return imageModality;
    }

    @Override
    public List<DicomImageWrapper> getAllImages() {
        final ArrayList<DicomImageWrapper> dicomImageWrappers = new ArrayList<>();
        final Set<ImageSliceOrientation> imageSliceOrientations = imageSliceOrientationToImagesList.keySet();
        for (ImageSliceOrientation imageSliceOrientation : imageSliceOrientations) {
            final List<DicomImageWrapper> allImageForImageSliceOrientation = getAllImageForImageSliceOrientation(imageSliceOrientation);
            dicomImageWrappers.addAll(allImageForImageSliceOrientation);
        }
        return dicomImageWrappers;
    }

    @Override
    public List<DicomImageWrapper> getAllImageForImageSliceOrientation(ImageSliceOrientation imageSliceOrientation) {
        final ArrayList<DicomImageWrapper> dicomImageWrappers = new ArrayList<>();
        final List<String> imagesPath = imageSliceOrientationToImagesList.get(imageSliceOrientation);
        if (imagesPath == null) {
            return dicomImageWrappers;
        }
        for (String imagePath : imagesPath) {
            DicomImageWrapper dicomImageWrapper = null;
            try {
                dicomImageWrapper = new DicomImageWrapper(imagePath);
            } catch (DicomException e) {
                //todo logger
                continue;
            } catch (IOException e) {
                //todo logger
                continue;
            }
            dicomImageWrappers.add(dicomImageWrapper);
        }
        return dicomImageWrappers;
    }

    @Override
    public void initialize(List<String> imagesPath) throws DicomException, IOException {
        AttributeList attributeList = new AttributeList();
        for (String imagePath : imagesPath) {
            final File dicomFile = new File(imagePath);
            attributeList.read(dicomFile);
            DicomFileParserImpl dicomFileParser = new DicomFileParserImpl(attributeList);
            final ImageSliceOrientation imageSliceOrientation = dicomFileParser.getImageSliceOrientation();
            List<String> imagesPathForOrientation = imageSliceOrientationToImagesList.computeIfAbsent(imageSliceOrientation, k -> new ArrayList<>());
            imagesPathForOrientation.add(imagePath);
            final DicomImageModality currentImageModality = dicomFileParser.getDicomImageModality();
            if (this.imageModality == null) {
                this.imageModality = currentImageModality;
            } else {
                if (this.imageModality != currentImageModality) {
                    throw new DicomException("Ошибка при создании объекта типа DicomSeriesDataHolder: Модальность одного из изображений не совпадает с предыдущими.");
                }
            }
        }
        final List<String> axialImagePaths = imageSliceOrientationToImagesList.get(ImageSliceOrientation.AXIAL);
        if (!axialImagePaths.isEmpty()) {
            final String firstImgPath = axialImagePaths.get(0);
            setImagePropertyFromFilePath(firstImgPath, ImageSliceOrientation.AXIAL);
        }
        final List<String> coronalImagePaths = imageSliceOrientationToImagesList.get(ImageSliceOrientation.CORONAL);
        if(!coronalImagePaths.isEmpty()){
            final String firstImgPath = coronalImagePaths.get(0);
            setImagePropertyFromFilePath(firstImgPath, ImageSliceOrientation.CORONAL);
        }
        final List<String> saggitalImagePaths = imageSliceOrientationToImagesList.get(ImageSliceOrientation.SAGITTALL);
        if(!saggitalImagePaths.isEmpty()){
            final String firstImgPath = saggitalImagePaths.get(0);
            setImagePropertyFromFilePath(firstImgPath, ImageSliceOrientation.SAGITTALL);
        }
    }

    @Override
    public ObjectProperty<Image> axialImageProperty() {
        return null;
    }

    @Override
    public ObjectProperty<Image> coronalImageProperty() {
        return null;
    }

    @Override
    public ObjectProperty<Image> saggitalImageProperty() {
        return null;
    }

    @Override
    public String getID() {
        return ID;
    }

    public Image getAxialImageProperty() {
        return axialImageProperty.get();
    }

    public ObjectProperty<Image> axialImagePropertyProperty() {
        return axialImageProperty;
    }

    public void setAxialImageProperty(Image axialImageProperty) {
        Platform.runLater(() -> this.axialImageProperty.set(axialImageProperty));
    }

    public Image getCoronalImageProperty() {
        return coronalImageProperty.get();
    }

    public ObjectProperty<Image> coronalImagePropertyProperty() {
        return coronalImageProperty;
    }

    public void setCoronalImageProperty(Image coronalImageProperty) {
        Platform.runLater(() -> this.coronalImageProperty.set(coronalImageProperty));
    }

    public Image getSaggitalImageProperty() {
        return saggitalImageProperty.get();
    }

    public ObjectProperty<Image> saggitalImagePropertyProperty() {
        return saggitalImageProperty;
    }

    public void setSaggitalImageProperty(Image saggitalImageProperty) {
        Platform.runLater(() -> this.saggitalImageProperty.set(saggitalImageProperty));
    }

    private void setImagePropertyFromFilePath(String filePath, ImageSliceOrientation imageSliceOrientation) throws IOException, DicomException {
        final DicomImageWrapper dicomImageWrapper = new DicomImageWrapper(filePath);
        final WritableImage writableImage = dicomImageWrapper.getWritableImage();
        switch (imageSliceOrientation) {
            case AXIAL:
                setAxialImageProperty(writableImage);
                break;
            case CORONAL:
                setCoronalImageProperty(writableImage);
                break;
            case SAGITTALL:
                setSaggitalImageProperty(writableImage);
        }
    }
    private DicomImageModality imageModality = null;
    private final String ID;
    private final Map<ImageSliceOrientation, List<String>> imageSliceOrientationToImagesList;
    private final ObjectProperty<Image> axialImageProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> coronalImageProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> saggitalImageProperty = new SimpleObjectProperty<>();
}
