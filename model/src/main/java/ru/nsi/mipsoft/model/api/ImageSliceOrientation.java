package ru.nsi.mipsoft.model.api;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public enum ImageSliceOrientation {
    AXIAL {
        private final DicomImageToPatientOrientation myOrientation =
                new DicomImageToPatientOrientation(
                        new Vector3D<>(1.F, 0.F, 0.F),
                        new Vector3D<>(0.F, 1.F, 0.F)
                );
        @Override
        public DicomImageToPatientOrientation getImageToPatientOrientation() {
            return myOrientation ;
        }
    },
    SAGITTAL {
        private final DicomImageToPatientOrientation myOrientation =
                new DicomImageToPatientOrientation(
                        new Vector3D<>(0.F, 0.F, 1.F),
                        new Vector3D<>(0.F, 1.F, 0.F)
                );
        @Override
        public DicomImageToPatientOrientation getImageToPatientOrientation() {
            return myOrientation ;
        }
    },
    CORONAL {
        private final DicomImageToPatientOrientation myOrientation =
                new DicomImageToPatientOrientation(
                        new Vector3D<>(1.F, 0.F, 0.F),
                        new Vector3D<>(0.F, 0.F, -1.F)
                );
        @Override
        public DicomImageToPatientOrientation getImageToPatientOrientation() {
            return myOrientation ;
        }
    },

    OTHER {
        @Override
        public DicomImageToPatientOrientation getImageToPatientOrientation() {
            return null;
        }
    };

    public abstract DicomImageToPatientOrientation getImageToPatientOrientation();

    public static ImageSliceOrientation getImageToPatientOrientationFromString(String stringRespresentation){
        stringRespresentation = stringRespresentation.trim();
        if(stringRespresentation.length() != CHAR_COUNT_IN_STRING_REPRESENTATION){
            throw new RuntimeException("При попытке извлечь значение тэга 'Ориентация изображения относительно пациента' из строки произошла ошибка:" +
                    " заданная строка не соответствует формату 'd\\d\\d\\d\\d\\d' (получена строка - " + stringRespresentation + ")");
        }
        char rowsXStr = stringRespresentation.charAt(ROWS_X_POSITION_IN_STRING_REPRESENTATION);
        char rowsYStr = stringRespresentation.charAt(ROWS_Y_POSITION_IN_STRING_REPRESENTATION);
        char rowsZStr = stringRespresentation.charAt(ROWS_Z_POSITION_IN_STRING_REPRESENTATION);
        char colsXStr = stringRespresentation.charAt(COLUMNS_X_POSITION_IN_STRING_REPRESENTATION);
        char colsYStr = stringRespresentation.charAt(COLUMNS_Y_POSITION_IN_STRING_REPRESENTATION);
        char colsZStr = stringRespresentation.charAt(COLUMNS_Z_POSITION_IN_STRING_REPRESENTATION);

        final float rowsX = Float.parseFloat(String.valueOf(rowsXStr));
        final float rowsY = Float.parseFloat(String.valueOf(rowsYStr));
        final float rowsZ = Float.parseFloat(String.valueOf(rowsZStr));
        final float columnsX = Float.parseFloat(String.valueOf(colsXStr));
        final float columnsY = Float.parseFloat(String.valueOf(colsYStr));
        final float columnsZ = Float.parseFloat(String.valueOf(colsZStr));

        final DicomImageToPatientOrientation dicomImageToPatientOrientation =
                new DicomImageToPatientOrientation(new Vector3D<Float>(rowsX, rowsY, rowsZ), new Vector3D<Float>(columnsX, columnsY, columnsZ));
        final Optional<ImageSliceOrientation> imageSliceOrientationOptional = Arrays.stream(ImageSliceOrientation.values())
                .filter(imageSliceOrientation -> dicomImageToPatientOrientation.equals(imageSliceOrientation.getImageToPatientOrientation()))
                .findFirst();
        return imageSliceOrientationOptional.isPresent() ? imageSliceOrientationOptional.get() : OTHER;
    }

    private static final int CHAR_COUNT_IN_STRING_REPRESENTATION = 11;
    private static final int ROWS_X_POSITION_IN_STRING_REPRESENTATION = 0;
    private static final int ROWS_Y_POSITION_IN_STRING_REPRESENTATION = 2;
    private static final int ROWS_Z_POSITION_IN_STRING_REPRESENTATION = 4;
    private static final int COLUMNS_X_POSITION_IN_STRING_REPRESENTATION = 6;
    private static final int COLUMNS_Y_POSITION_IN_STRING_REPRESENTATION = 8;
    private static final int COLUMNS_Z_POSITION_IN_STRING_REPRESENTATION = 10;
}
