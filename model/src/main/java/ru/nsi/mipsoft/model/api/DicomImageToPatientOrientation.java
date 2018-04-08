package ru.nsi.mipsoft.model.api;

public class DicomImageToPatientOrientation {

    private final Vector3D<Float> rowsOrientation;
    private final Vector3D<Float> columnsOrientation;

    public DicomImageToPatientOrientation(Vector3D<Float> rowsOrientation, Vector3D<Float> columnsOrientation) {
        this.rowsOrientation = rowsOrientation;
        this.columnsOrientation = columnsOrientation;
    }

    public Vector3D<Float> getRowsOrientation() {
        return rowsOrientation;
    }

    public Vector3D<Float> getColumnsOrientation() {
        return columnsOrientation;
    }
}
