package ru.nsi.mipsoft.model.api;

import com.pixelmed.dicom.*;
import com.pixelmed.display.SourceImage;
import com.sun.istack.internal.NotNull;

public class DicomImageWrapper {

    public DicomImageWrapper(@NotNull AttributeList attributeList) throws DicomException {
        if (attributeList.isEmpty()) {
            throw new RuntimeException(
                    ERROR_MESSAGE_PREFIX + "Попытка создания объекта типа DicomImageWrapper с помощью пустого объекта типа AttributeList"
            );
        }
        UnsignedShortAttribute bitsStored = (UnsignedShortAttribute) attributeList.get(TagFromName.BitsStored);
        sourceImage = new SourceImage(attributeList);
        int numberOfBufferedImages = sourceImage.getNumberOfBufferedImages();
        if (numberOfBufferedImages != 1) {
            String message = ERROR_MESSAGE_PREFIX + "При создании объекта типа DicomImageWrapper передан объект типа AttributeList," +
                    " связаный с DICOM файлом, в котором содержится несколько изображений." +
                    " Программа не умеет работать с такими файлами.";
            //todo LOGGER
            System.out.println(message);
            throw new RuntimeException(message);
        }
        CodeStringAttribute photometricInterpretationAttr =
                (CodeStringAttribute) attributeList.get(TagFromName.PhotometricInterpretation);
        String photometricInterpretationStr = photometricInterpretationAttr.getDelimitedStringValuesOrEmptyString();
        this.photometricInterpretation = PhotometricInterpretation.getInstanceFromString(photometricInterpretationStr);
        boolean isPhotometricInterpretationValid = PhotometricInterpretation.isBelongToValidValues(
                this.photometricInterpretation,
                PhotometricInterpretation.MONOCHROME1,
                PhotometricInterpretation.MONOCHROME2
        );
        if(!isPhotometricInterpretationValid) {
            String message = ERROR_MESSAGE_PREFIX +
                    String.format(
                            "значение тэга PhotometricInterpritation равно '%s'. Данное значение не поддерживается программой.",
                            this.photometricInterpretation.toString()
                    );
            //todo LOGGER
            System.out.println(message);
            throw new RuntimeException(message);
        }
        this.bitsStored = bitsStored.getShortValues()[0];
        OtherWordAttribute pixelDataAttr = (OtherWordAttribute) attributeList.get(TagFromName.PixelData);
        pixelData = pixelDataAttr.getIntegerValues();
    }

    public short getBitsStored() {
        return bitsStored;
    }

    public SourceImage getSourceImage() {
        return sourceImage;
    }

    public int getPixelWidth() {
        return sourceImage.getWidth();
    }

    public int getPixelHight() {
        return sourceImage.getHeight();
    }

    public double getPixelMaxValue() {
        return sourceImage.getMaximum();
    }

    public double getPixelMinValue() {
        return sourceImage.getMinimum();
    }

    /**
     * Возвращает копию массив пиксельных данных. Длина массива равна pixelWidth * pixelHeight,
     * где pixelWidth - ширина изображения в пикселях; pixelHeight - выоста в пикселях.
     *
     * @return КОПИЯ пиксельных данных. Изменения, сделанные с копией не повлияют на данные, хранящиеся объекте
     */
    public int[] getPixelData() {
        return pixelData.clone();
    }

    private final short bitsStored;
    private final SourceImage sourceImage;
    private final int[] pixelData;
    private final PhotometricInterpretation photometricInterpretation;
    private static final String ERROR_MESSAGE_PREFIX = "При создании объекта DicomImageWrapper произошла ошибка: ";
}
