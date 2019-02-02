package ru.nsi.mipsoft.model.api;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


public interface ISeriesDicomDataHolder extends IObjectWithID<String> {

    /**
     * Метод получения модальности для данной серии (КТ, ПЭТ и тд)
     *
     * @return модальность данной серии
     */
    DicomImageModality getImageModality();

    /**
     * Инициализировать объект
     *
     * @throws DicomException возникает при ошибке во время парсинга DICOM файла
     * @throws IOException
     */
    void initialize() throws DicomException, IOException;

    boolean isInitialized();

    /**
     * Метод вызывается когда на одном из сечений выбран пиксель и нужно отобразить два изображения
     * других ориентаций, которые соответсвуют этому пикселю. Например: на одном из аксиальных сечений выбран пиксель,
     * нужно отобразить саггитальное и карональное сечение, которые соответствуют этому пикселю на аксиальном сечении
     *
     * @param xCoord Х-координата выбранного пикселя
     * @param yCoord У-координата выбранного пикселя
     * @param sourceOrientation ориентация сечения, на котором был выбран пиксель (аксильное, карональное или саггиттальное)
     */
    void synchronizeImages(int xCoord, int yCoord, ImageSliceOrientation sourceOrientation);

    /**
     * Переключиться на следующий аксиальный срез. В результате должно измениться FX-свойство,
     * получаемое методом {@link #getAxialImageWrapperProperty()} ()}
     *
     * @return true если следующее изображение существует и переключение успешно осуществлено, иначе false
     */
    boolean nextAxialImage();

    /**
     * Переключиться на следующий саггитальный срез. В результате должно измениться FX-свойство,
     * получаемое методом {@link #getSaggitalImageWrapperProperty()}
     *
     * @return true если следующее изображение существует и переключение успешно осуществлено, иначе false
     */
    boolean nextSaggitalImage();

    /**
     * Переключиться на следующий корональный срез. В результате должно измениться FX-свойство,
     * получаемое методом {@link #getCoronalImageWrapperProperty()}
     *
     * @return true если следующее изображение существует и переключение успешно осуществлено, иначе false
     */
    boolean nextCoronalImage();

    /**
     * Переключиться на предыдущий аксиальный срез. В результате должно измениться FX-свойство,
     * получаемое методом {@link #getAxialImageWrapperProperty()} ()}
     *
     * @return true если следующее изображение существует и переключение успешно осуществлено, иначе false
     */
    boolean previousAxialImage();

    /**
     * Переключиться на предыдущий саггитальный срез. В результате должно измениться FX-свойство,
     * получаемое методом {@link #getSaggitalImageWrapperProperty()}
     *
     * @return true если следующее изображение существует и переключение успешно осуществлено, иначе false
     */
    boolean previousSaggitalImage();

    /**
     * Переключиться на предыдущий корональный срез. В результате должно измениться FX-свойство,
     * получаемое методом {@link #getCoronalImageWrapperProperty()}
     *
     * @return true если следующее изображение существует и переключение успешно осуществлено, иначе false
     */
    boolean previousCoronalImage();

    /**
     * Метод получения текущего аксиального среза в данной серии
     *
     * @return текущий аксиальный срез
     */
    ObjectProperty<DicomImageWrapper> getAxialImageWrapperProperty();

    /**
     * Метод получения текущего коранального среза в данной серии
     *
     * @return текущий коранальный срез
     */
    ObjectProperty<DicomImageWrapper> getCoronalImageWrapperProperty();

    /**
     * Метод получения текущего саггитального среза в данной серии
     *
     * @return текущий саггитальный срез
     */
    ObjectProperty<DicomImageWrapper> getSaggitalImageWrapperProperty();
}
