package ru.nsi.mipsoft.model.api;

public enum ImageSliceOrientation {
    AXIAL {
        private final DicomImageToPatientOrientation myOrientation =
                new DicomImageToPatientOrientation(
                        new Vector3D<Float>(1.F, 0.F, 0.F),
                        new Vector3D<Float>(0.F, 1.F, 0.F)
                );
        @Override
        DicomImageToPatientOrientation getImageToPatientOrientation() {
            return myOrientation ;
        }
    },
    SAGITTALL {
        private final DicomImageToPatientOrientation myOrientation =
                new DicomImageToPatientOrientation(
                        new Vector3D<Float>(0.F, 0.F, 1.F),
                        new Vector3D<Float>(0.F, 1.F, 0.F)
                );
        @Override
        DicomImageToPatientOrientation getImageToPatientOrientation() {
            return myOrientation ;
        }
    },
    CORONAL {
        private final DicomImageToPatientOrientation myOrientation =
                new DicomImageToPatientOrientation(
                        new Vector3D<Float>(1.F, 0.F, 0.F),
                        new Vector3D<Float>(0.F, 0.F, -1.F)
                );
        @Override
        DicomImageToPatientOrientation getImageToPatientOrientation() {
            return myOrientation ;
        }
    };

    abstract DicomImageToPatientOrientation getImageToPatientOrientation();
}
