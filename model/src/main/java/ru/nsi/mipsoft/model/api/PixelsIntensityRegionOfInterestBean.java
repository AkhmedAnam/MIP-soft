package ru.nsi.mipsoft.model.api;

/**
 * Класс в котором хранятся параметры преобразования интесивности пикселей изображения
 * в соответствии с заданным уровнем окна преобразования и шириной этого окна.
 * Класс является бином (капсулой с финальными полями).
 * Описание параметров смотреть в документации к конструктору этого класса {@link #PixelsIntensityRegionOfInterestBean(int, Percent, Percent, int)}.
 */
public class PixelsIntensityRegionOfInterestBean {
    /**
     * @param maxPossibleValue максимально возможное значение интенсивности пикселя. Определяется DICOM-тегом BitsStored.
     *                (смотреть описание метода {@link DicomImageWrapper#getBitsStored()})
     * @param windowWidthPercent ширина окна интенсивности, заданная в процентах относительно максимально возможной ширины (которая определяется уровнем окна)
     * @param windowLevelPercent уровень окна, заданный относительно максимальной и минимальной возможной интенсивности
     * @param normolizedValue нормирововчное значение. Предполагается использовать значение равное 256 (число градаций серого)
     */
    public PixelsIntensityRegionOfInterestBean(
            int maxPossibleValue,
            Percent windowWidthPercent,
            Percent windowLevelPercent,
            int normolizedValue
    ) {
        this.maxPossibleValue = maxPossibleValue;
        this.windowWidthPercent = windowWidthPercent;
        this.windowLevelPercent = windowLevelPercent;
        this.normolizedValue = normolizedValue;
    }

    private final int maxPossibleValue;
    private final Percent windowWidthPercent;
    private final Percent windowLevelPercent;
    private final int normolizedValue;

    public int getMaxPossibleValue() {
        return maxPossibleValue;
    }

    public Percent getWindowWidthPercent() {
        return windowWidthPercent;
    }

    public Percent getWindowLevelPercent() {
        return windowLevelPercent;
    }

    public int getNormolizedValue() {
        return normolizedValue;
    }
}
