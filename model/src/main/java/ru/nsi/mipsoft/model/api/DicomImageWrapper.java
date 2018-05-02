package ru.nsi.mipsoft.model.api;

import com.pixelmed.dicom.*;
import com.pixelmed.display.SourceImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DicomImageWrapper {

    public DicomImageWrapper(AttributeList attributeList) throws DicomException {
        if (attributeList.isEmpty()) {
            throw new DicomException(
                    ERROR_MESSAGE_PREFIX + "Попытка создания объекта типа DicomImageWrapper с помощью пустого объекта типа AttributeList"
            );
        }
        UnsignedShortAttribute bitsStored = (UnsignedShortAttribute) attributeList.get(TagFromName.BitsStored);
        final SourceImage sourceImage = new SourceImage(attributeList);
        int numberOfBufferedImages = sourceImage.getNumberOfBufferedImages();
        if (numberOfBufferedImages != 1) {
            String message = ERROR_MESSAGE_PREFIX + "При создании объекта типа DicomImageWrapper передан объект типа AttributeList," +
                    " связаный с DICOM файлом, в котором содержится несколько изображений." +
                    " Программа не умеет работать с такими файлами.";
            //todo LOGGER
            throw new DicomException(message);
        }
        CodeStringAttribute photometricInterpretationAttr =
                (CodeStringAttribute) attributeList.get(TagFromName.PhotometricInterpretation);
        String photometricInterpretationStr = photometricInterpretationAttr.getDelimitedStringValuesOrEmptyString();
        PhotometricInterpretation photometricInterpretation = PhotometricInterpretation.getInstanceFromString(photometricInterpretationStr);
        boolean isPhotometricInterpretationValid = PhotometricInterpretation.isBelongToValidValues(
                photometricInterpretation,
                PhotometricInterpretation.MONOCHROME1,
                PhotometricInterpretation.MONOCHROME2
        );
        if(!isPhotometricInterpretationValid) {
            String message = ERROR_MESSAGE_PREFIX +
                    String.format(
                            "значение тэга PhotometricInterpritation равно '%s'. Данное значение не поддерживается программой.",
                            photometricInterpretation.toString()
                    );
            //todo LOGGER
            throw new DicomException(message);
        }
        String patientOrientation =
                Attribute.getDelimitedStringValuesOrEmptyString(attributeList, TagFromName.ImageOrientationPatient);
        imageSliceOrientation = ImageSliceOrientation.getImageToPatientOrientationFromString(patientOrientation);
        final String modalityStr = Attribute.getDelimitedStringValuesOrEmptyString(attributeList, TagFromName.Modality);
        imageModality = DicomImageModality.valueOf(modalityStr);
        this.bitsStored = bitsStored.getShortValues()[0];
        imagePixelWidth = sourceImage.getWidth();
        imagePixelHeight = sourceImage.getHeight();
        pixelMinimumValue = sourceImage.getMinimum();
        pixelMaximumValue = sourceImage.getMaximum();
        OtherWordAttribute pixelDataAttr = (OtherWordAttribute) attributeList.get(TagFromName.PixelData);
        pixelData = pixelDataAttr.getIntegerValues();
        String pixelSpacingStr = Attribute.getDelimitedStringValuesOrEmptyString(attributeList, TagFromName.PixelSpacing);
        pixelSpacingStr = pixelSpacingStr.trim();
        final String[] splitStrings = pixelSpacingStr.split("\\\\");
        if(splitStrings.length != 2){
            throw new DicomException( ERROR_MESSAGE_PREFIX + "неверный формат строки для атрибута Pixel Spacing."
            + " Ожидалась строка вида 'pixelWidthInMm\\pixelHeightInMm'. Переданная строка: " + pixelSpacingStr);
        }
        final String pixelWidthInMmStr = splitStrings[0];
        final String pixelHeightInMmStr = splitStrings[1];
        pixelWidthInMm = Float.parseFloat(pixelWidthInMmStr);
        pixelHeightInMm = Float.parseFloat(pixelHeightInMmStr);
    }

    public static void main(String[] args) throws IOException {
        final BufferedImage bufferedImage = ImageIO.read(new File("C://Users//ahmed//desktop//girl.png"));
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        BufferedImage greyScaleBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                final int rgb = bufferedImage.getRGB(i, j);
                greyScaleBufferedImage.setRGB(i, j, rgb);
            }
        }
        System.out.println("");
    }

    /**
     * Метод возвращает кол-во бит, выделенное для хранения градации серого.
     * Это значение определяет максимальную интенсивность пикселя, которое равно 2^(bitsStored - 1),
     * где bitsStored - кол-во бит, возвращаемое этим методом
     * @return кол-во бит, которым кодируется интенсивность каждого пикселя
     */
    public short getBitsStored() {
        return bitsStored;
    }

    public int getImagePixelWidth() {
        return imagePixelWidth;
    }

    public int getImagePixelHeight() {
        return imagePixelHeight;
    }

    public double getPixelMaximumValue() {
        return pixelMaximumValue;
    }

    public double getPixelMinimumValue() {
        return pixelMinimumValue;
    }

    public float getPixelWidthInMm() {
        return pixelWidthInMm;
    }

    public float getPixelHeightInMm() {
        return pixelHeightInMm;
    }

    public ImageSliceOrientation getImageSliceOrientation() {
        return imageSliceOrientation;
    }

    public WritableImage getWritableImage(PixelsIntensityRegionOfInterestBean intensityRegionOfInterestBean){
        BufferedImage tempBuffImg = new BufferedImage(imagePixelWidth, imagePixelHeight, BufferedImage.TYPE_BYTE_GRAY);
        final int[] correctedPixelsIntensity = DicomImagePixelIntesityCorrector.getCorrectedPixelsIntensity(pixelData, intensityRegionOfInterestBean);
        tempBuffImg.getRaster().setPixels(0, 0, imagePixelWidth, imagePixelHeight, correctedPixelsIntensity);
        return SwingFXUtils.toFXImage(tempBuffImg, null);
    }

    /**
     * Возвращает копию массив пиксельных данных. Длина массива равна imagePixelWidth * imagePixelHeight,
     * где imagePixelWidth - ширина изображения в пикселях; imagePixelHeight - выоста в пикселях.
     *
     * @return КОПИЯ пиксельных данных. Изменения, сделанные с копией не повлияют на данные, хранящиеся объекте этого класса
     */
    public int[] getPixelData() {
        return pixelData.clone();
    }

    public DicomImageModality getImageModality() {
        return imageModality;
    }

    private final short bitsStored;
    private final int imagePixelWidth;
    private final int imagePixelHeight;
    private final double pixelMinimumValue;
    private final double pixelMaximumValue;
    private final int[] pixelData;
    private final float pixelWidthInMm;
    private final float pixelHeightInMm;
    private final ImageSliceOrientation imageSliceOrientation;
    private final DicomImageModality imageModality;
    private static final String ERROR_MESSAGE_PREFIX = "При создании объекта DicomImageWrapper произошла ошибка: ";
}
