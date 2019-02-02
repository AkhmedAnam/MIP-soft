package ru.nsi.mipsoft.model.api;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("Duplicates")
public class DicomSeriesDataHolder implements ISeriesDicomDataHolder {

    private static final String ERROR_MESSAGE_PREFIX = "Ошибка при создании объекта типа DicomSeriesDataHolder:";

    private boolean initialized = false;
    private final List<String> imagesPath;
    private final String ID;
    private final TreeSet<DicomImageWrapper> sourceAxialImages = new TreeSet<>();
    private DicomImageModality imageModality = null;
    private final ObjectProperty<DicomImageWrapper> axialImageProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<DicomImageWrapper> coronalImageProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<DicomImageWrapper> saggitalImageProperty = new SimpleObjectProperty<>();
    private float sliceThickness;
    private DicomImageWrapper[] cachedSrcAxialArr;

    public DicomSeriesDataHolder(@NotNull String id, @NotNull List<String> imagesPath){
        this.imagesPath = imagesPath;
        ID = id;
    }


    @Override
    public DicomImageModality getImageModality() {
        return imageModality;
    }

    @Override
    public void initialize() throws DicomException, IOException {
        AttributeList attributeList = new AttributeList();
        ImageSliceOrientation lastOrientation = null;
        float lastSliceThickness = -1.F;
        ImagePosition lastImagePosition = null;
        for (String imagePath : imagesPath) {
            final File dicomFile = new File(imagePath);
            attributeList.read(dicomFile);
            DicomFileParserImpl dicomFileParser = new DicomFileParserImpl(attributeList);
            final ImageSliceOrientation imageSliceOrientation = dicomFileParser.getImageSliceOrientation();
            if (lastOrientation == null) {
                lastOrientation = imageSliceOrientation;
            } else {
                if (lastOrientation != imageSliceOrientation) {
                    //Исходные данные в серии - это всегда набор аксиальных срезов, из которых програмным путём получают
                    //саггитальные и корональные срезы.
                    throw new DicomException(
                            String.format(
                                    "Ориентация одного из срезов [%s] не совпадает с предыдущим срезом [%s]",
                                    imageSliceOrientation.toString(),
                                    lastOrientation.toString()
                            )
                    );
                }
            }
            final DicomImageModality currentImageModality = dicomFileParser.getDicomImageModality();
            if (imageModality == null) {
                imageModality = currentImageModality;
            } else {
                if (imageModality != currentImageModality) {
                    throw new DicomException(String.format(
                            ERROR_MESSAGE_PREFIX +
                                    " модальность одного из изображений [%s] не совпадает с предыдущими [%s].",
                            currentImageModality.toString(), imageModality.toString()
                    ));
                }
            }
            final DicomImageWrapper imageWrapper = new DicomImageWrapper(imagePath);
            final ImagePosition imagePositionPatient = imageWrapper.getImagePositionPatient();
            if (lastImagePosition == null) {
                lastImagePosition = imagePositionPatient;
            } else {
                final float z = imagePositionPatient.getZ();
                final float last_z = lastImagePosition.getZ();
                final float sliceThickness = Math.abs(z - last_z);
                if (lastSliceThickness < 0) {
                    lastSliceThickness = sliceThickness;
                } else {
                    if (lastSliceThickness != sliceThickness) {
                        throw new DicomException(
                                String.format(
                                        ERROR_MESSAGE_PREFIX +
                                                " толщина двух текущих смежных срезов [%f] отличается от предыдущих двух смежных срезов [%f]",
                                        sliceThickness, lastSliceThickness
                                )
                        );
                    } else {
                        this.sliceThickness = sliceThickness;
                    }
                }
            }

            sourceAxialImages.add(imageWrapper);
        }
        cachedSrcAxialArr = ((DicomImageWrapper[]) sourceAxialImages.toArray());
        setAxialImageProperty(sourceAxialImages.first());
        synchImagesFromAxial(0, 0);
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void synchronizeImages(int xCoord, int yCoord, ImageSliceOrientation sourceOrientation) {
        switch (sourceOrientation) {
            case AXIAL:
                synchImagesFromAxial(xCoord, yCoord);
                break;
            case SAGITTAL:
                synchImagesFromSaggital(xCoord, yCoord);
                break;
            case CORONAL:
                synchImagesFromCoronal(xCoord, yCoord);
        }
    }

    private void synchSaggitalFromAxial(int axial_x, int axial_y){
        final DicomImageWrapper currentAxialImage = getAxialImageProperty();
        final int axialImagePixelWidth = currentAxialImage.getImagePixelWidth();
        final int axialImagePixelHeight = currentAxialImage.getImagePixelHeight();
        final DicomImageModality imageModality = currentAxialImage.getImageModality();
        final short bitsStored = currentAxialImage.getBitsStored();
        final ImagePosition axialImagePositionPatient = currentAxialImage.getImagePositionPatient();
        final float axialPixelHeightInMm = currentAxialImage.getPixelHeightInMm();
        final float axialPixelWidthInMm = currentAxialImage.getPixelWidthInMm();
        final float axial_minimal_x = axialImagePositionPatient.getX();
        final float axial_minimal_y = axialImagePositionPatient.getY();
        final float axial_minimal_Z = sourceAxialImages.first().getImagePositionPatient().getZ();
        final int axialImagesSize = sourceAxialImages.size();

        int frameWidth = axialImagesSize;
        int frameHeight = axialImagePixelHeight;
        ImagePosition imagePosition = new ImagePosition(
                axial_minimal_x + axial_x * axialPixelWidthInMm,
                axial_minimal_y,
                axial_minimal_Z
        );
        int[] dstData = new int[frameWidth * frameHeight];
        MinMaxPair minMaxPair = SlicesDataTransformationKt.fillDistinationDataWithAxialData(
                cachedSrcAxialArr,
                dstData,
                axial_x,
                axial_y,
                axialImagePixelWidth,
                frameWidth,
                frameHeight,
                ImageSliceOrientation.SAGITTAL
        );
        DicomImageWrapper.Builder builder = new DicomImageWrapper.Builder();
        final DicomImageWrapper saggitalImage = builder.setBitsStored(bitsStored)
                .setData(dstData)
                .setImageModality(imageModality)
                .setImagePosition(imagePosition)
                .setImageSliceOrientation(ImageSliceOrientation.SAGITTAL)
                .setPixelHeight(frameHeight)
                .setPixelWidth(frameWidth)
                .setPixHeightInMm(axialPixelHeightInMm)
                .setPixWidthInMm(sliceThickness)
                .setPixMaxValue(minMaxPair.getMax())
                .setPixMinValue(minMaxPair.getMin())
                .build();
        setSaggitalImageProperty(saggitalImage);
    }

    private void synchCoronalFromAxial(int axial_x, int axial_y){
        final DicomImageWrapper currentAxialImage = getAxialImageProperty();
        final int axialImagePixelWidth = currentAxialImage.getImagePixelWidth();
        final int axialImagePixelHeight = currentAxialImage.getImagePixelHeight();
        final DicomImageModality imageModality = currentAxialImage.getImageModality();
        final short bitsStored = currentAxialImage.getBitsStored();
        final ImagePosition axialImagePositionPatient = currentAxialImage.getImagePositionPatient();
        final float axialPixelHeightInMm = currentAxialImage.getPixelHeightInMm();
        final float axialPixelWidthInMm = currentAxialImage.getPixelWidthInMm();
        final int axialImagesSize = sourceAxialImages.size();

        final DicomImageWrapper lastAxialImage = sourceAxialImages.last();
        final ImagePosition lastAxialImagePosition = lastAxialImage.getImagePositionPatient();
        final float last_axial_x = lastAxialImagePosition.getX();
        final float last_axial_y = lastAxialImagePosition.getY();
        final float last_axial_z = lastAxialImagePosition.getZ();
        ImagePosition imagePosition = new ImagePosition(
                last_axial_x,
                axial_y * axialPixelHeightInMm + last_axial_y,
                last_axial_z
        );
        int frameWidth = axialImagePixelWidth;
        int frameHeight = axialImagesSize;
        int[] dstData = new int[frameHeight * frameWidth];
        MinMaxPair minMaxPair = SlicesDataTransformationKt.fillDistinationDataWithAxialData(
                cachedSrcAxialArr,
                dstData,
                axial_x,
                axial_y,
                axialImagePixelWidth,
                frameWidth,
                frameHeight,
                ImageSliceOrientation.CORONAL
        );
        final DicomImageWrapper coronalImage = new DicomImageWrapper.Builder().setBitsStored(bitsStored)
                .setData(dstData)
                .setImageModality(imageModality)
                .setImagePosition(imagePosition)
                .setImageSliceOrientation(ImageSliceOrientation.CORONAL)
                .setPixelHeight(frameHeight)
                .setPixelWidth(frameWidth)
                .setPixHeightInMm(sliceThickness)
                .setPixWidthInMm(axialPixelWidthInMm)
                .setPixMaxValue(minMaxPair.getMax())
                .setPixMinValue(minMaxPair.getMin())
                .build();
        setCoronalImageProperty(coronalImage);
    }
    private void synchImagesFromAxial(int xCoord, int yCoord) {
        synchSaggitalFromAxial(xCoord, yCoord);
        synchCoronalFromAxial(xCoord, yCoord);
    }


    private void synchImagesFromSaggital(int xCoord, int yCoord) {
        setAxialImageProperty(cachedSrcAxialArr[yCoord]);

        //region setting Coronal slice
        int axial_x = 0;
        int axial_y = xCoord;
        synchCoronalFromAxial(axial_x, axial_y);
        //endregion
    }

    private void synchImagesFromCoronal(int xCoord, int yCoord) {
        setAxialImageProperty(cachedSrcAxialArr[yCoord]);

        //region setting Sagittal slice
        int axial_x = xCoord;
        int axial_y = 0;
        synchSaggitalFromAxial(axial_x, axial_y);
        //endregion
    }

    @Override
    public boolean nextAxialImage() {
        return false;
    }

    @Override
    public boolean nextSaggitalImage() {
        return false;
    }

    @Override
    public boolean nextCoronalImage() {
        return false;
    }

    @Override
    public boolean previousAxialImage() {
        return false;
    }

    @Override
    public boolean previousSaggitalImage() {
        return false;
    }

    @Override
    public boolean previousCoronalImage() {
        return false;
    }

    @Override
    public ObjectProperty<DicomImageWrapper> getAxialImageWrapperProperty() {
        return axialImagePropertyProperty();
    }

    @Override
    public ObjectProperty<DicomImageWrapper> getCoronalImageWrapperProperty() {
        return coronalImagePropertyProperty();
    }

    @Override
    public ObjectProperty<DicomImageWrapper> getSaggitalImageWrapperProperty() {
        return saggitalImagePropertyProperty();
    }

    @Override
    public String getID() {
        return ID;
    }

    public DicomImageWrapper getAxialImageProperty() {
        return axialImageProperty.get();
    }

    public DicomImageWrapper getCoronalImageProperty() {
        return coronalImageProperty.get();
    }

    public DicomImageWrapper getSaggitalImageProperty() {
        return saggitalImageProperty.get();
    }

    public ObjectProperty<DicomImageWrapper> axialImagePropertyProperty() {
        return axialImageProperty;
    }

    public ObjectProperty<DicomImageWrapper> coronalImagePropertyProperty() {
        return coronalImageProperty;
    }

    public ObjectProperty<DicomImageWrapper> saggitalImagePropertyProperty() {
        return saggitalImageProperty;
    }

    public void setAxialImageProperty(DicomImageWrapper axialImageProperty) {
        Platform.runLater(() -> this.axialImageProperty.set(axialImageProperty));
    }

    public void setCoronalImageProperty(DicomImageWrapper coronalImageProperty) {
        Platform.runLater(() -> this.coronalImageProperty.set(coronalImageProperty));
    }

    public void setSaggitalImageProperty(DicomImageWrapper saggitalImageProperty) {
        Platform.runLater(() -> this.saggitalImageProperty.set(saggitalImageProperty));
    }
}
