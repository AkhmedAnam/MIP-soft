package ru.nsi.mipsoft.model.api;


public class DicomImagePixelIntesityCorrector {
    /**
     * Метод корректирует массив пиксельных данных в соответствии с заданным уровнем окна и шириной окна.
     *
     * @param sourcePixelData               исходный массив пиксельных данных
     * @param intensityRegionOfInterestBean капсула в которой хранятся параметры преобразования интенсивности пикселей
     *                                      (смотреть класс {@link PixelsIntensityRegionOfInterestBean})
     * @return скорректированный массив пиксельных данных: пиксели с исходной интенсивностью ниже нижней границы окна будут иметь интенсивность = 0,
     * а пиксели с исходной интенсивностью больше верхней границы окна будут иметь максимальную интенсивность.
     */
    public static int[] getCorrectedPixelsIntensity(
            int[] sourcePixelData,
            PixelsIntensityRegionOfInterestBean intensityRegionOfInterestBean
    ) {
        final int normolizedValue = intensityRegionOfInterestBean.getNormolizedValue();
        final int maxPossibleValue = intensityRegionOfInterestBean.getMaxPossibleValue();
        final Percent windowLevelPercent = intensityRegionOfInterestBean.getWindowLevelPercent();
        final Percent windowWidthPercent = intensityRegionOfInterestBean.getWindowWidthPercent();
        if (normolizedValue < 0) {
            String msg = "В метод DicomImagePixelIntesityCorrector.getCorrectedPixelsIntensity передано нормировочное значение, которое меньше нуля";
            throw new IllegalArgumentException(msg);
        }
        int[] toReturn = new int[sourcePixelData.length];
        final float maxValueFloat = (float) maxPossibleValue;
        final float windowLevelValue = windowLevelPercent.getPercentOfValue(maxPossibleValue);
        final float maxWindowWidth = Math.min(maxValueFloat - windowLevelValue, windowLevelValue);
        final float windowWidthValue = windowWidthPercent.getPercentOfValue(maxWindowWidth);
        final float windowHalfWidth = windowWidthValue * 0.5F;
        final float minCurrVal = windowLevelValue - windowHalfWidth;

        for (int i = 0; i < sourcePixelData.length; i++) {
            final float sourcePixelIntensity = (float) sourcePixelData[i];
            float targetPixelIntensity = (sourcePixelIntensity - minCurrVal) * normolizedValue / windowWidthValue;
            if (targetPixelIntensity < 0) {
                targetPixelIntensity = 0;
            } else if (targetPixelIntensity > normolizedValue) {
                targetPixelIntensity = normolizedValue;
            }
            toReturn[i] = (int) targetPixelIntensity;
        }
        return toReturn;
    }
}
