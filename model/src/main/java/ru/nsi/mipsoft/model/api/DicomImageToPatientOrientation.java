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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DicomImageToPatientOrientation)) return false;

        DicomImageToPatientOrientation that = (DicomImageToPatientOrientation) o;

        if (rowsOrientation != null ? !rowsOrientation.equals(that.rowsOrientation) : that.rowsOrientation != null)
            return false;
        return columnsOrientation != null ? columnsOrientation.equals(that.columnsOrientation) : that.columnsOrientation == null;
    }

    @Override
    public int hashCode() {
        int result = rowsOrientation != null ? rowsOrientation.hashCode() : 0;
        result = 31 * result + (columnsOrientation != null ? columnsOrientation.hashCode() : 0);
        return result;
    }
}
