import com.pixelmed.dicom.*;
import com.pixelmed.display.DicomBrowser;
import com.pixelmed.display.DicomImageViewer;
import com.pixelmed.display.SingleImagePanel;
import com.pixelmed.display.SourceImage;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Main {

    private static AttributeList attributeList;

    public static void main(String[] args){
        new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                        "DICOM files",
                        "*dcm", "*dicom");
                fileChooser.setSelectedExtensionFilter(extensionFilter);
                fileChooser.setTitle("Выберете DICOM файл");
                File file = fileChooser.showOpenDialog(null);
                attributeList = new AttributeList();
                try {
                    attributeList.read(file);
//                    System.out.println(attributeList.toString());
//                    Attribute pixelData = attributeList.getPixelData();
//                    System.out.println(pixelData.toString());
//                    byte[] byteValues = pixelData.getByteValues();
                    SourceImage sourceImage = new SourceImage(attributeList);
                    System.out.println("Modality: " + getTagInformation(TagFromName.Modality));
                    System.out.println("Samples per pixel: " + getTagInformation(TagFromName.SamplesPerPixel));
                    System.out.println("Photometric Interpretation: " + getTagInformation(TagFromName.PhotometricInterpretation));
                    System.out.println("Pixel sapcing: " + getTagInformation(TagFromName.PixelSpacing));
                    System.out.println("Bits allocated: " + getTagInformation(TagFromName.BitsAllocated));
                    System.out.println("Bits stored: " + getTagInformation(TagFromName.BitsStored));
                    System.out.println("High bit: " + getTagInformation(TagFromName.HighBit));
                    System.out.println("High bit: " + getTagInformation(TagFromName.HighBit));
                    System.out.println("Number of frames: " + sourceImage.getNumberOfFrames());
                    System.out.println("sourceImage.getWidth() = " + sourceImage.getWidth());
                    System.out.println("sourceImage.getHeight() = " + sourceImage.getHeight());
                    System.out.println("sourceImage.isGrayscale() = " + sourceImage.isGrayscale());
                    System.out.println("seriesID = " + getTagInformation(TagFromName.SeriesInstanceUID));
                    System.out.println("studyID = " + getTagInformation(TagFromName.StudyInstanceUID));
                    System.out.println("Image orientation = " + getTagInformation(TagFromName.ImageOrientationPatient));
                    System.out.println("Image position = " + getTagInformation(TagFromName.ImagePositionPatient));
                    System.out.println("Slice thickness = " + getTagInformation(TagFromName.SliceThickness));
                    System.out.println("Patient ID = " + getTagInformation(TagFromName.PatientID));
                    BufferedImage bufferedImage = sourceImage.getBufferedImage();
                    final WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
                    Stage stage = new Stage();
                    BorderPane borderPane = new BorderPane();
                    ImageView imageView = new ImageView(writableImage);
                    borderPane.setCenter(imageView);
                    Scene scene = new Scene(borderPane, 600, 600);
                    stage.setScene(scene);
                    stage.show();
                    OtherWordAttribute otherByteAttribute = (OtherWordAttribute) attributeList.get(TagFromName.PixelData);
                    short[] pixelData = otherByteAttribute.getShortValues();
                    System.out.println("pixelData.length = " + pixelData.length);
                    UnsignedShortAttribute bitsStoredAttribute = (UnsignedShortAttribute) attributeList.get(TagFromName.BitsStored);
                    short[] shortValues = bitsStoredAttribute.getShortValues();
                    System.out.println("shortValues.length = " + shortValues.length);
                    System.out.println("shortValues[0] = " + shortValues[0]);
                    CodeStringAttribute photoInterpret = (CodeStringAttribute) attributeList.get(TagFromName.PhotometricInterpretation);
                    String photoStr = photoInterpret.getDelimitedStringValuesOrEmptyString();
                    System.out.println("photoStr = " + photoStr.toLowerCase());
                    final UnsignedShortAttribute rowsAttr = (UnsignedShortAttribute) attributeList.get(TagFromName.Rows);
                    final short rows = rowsAttr.getShortValues()[0];
                    final UnsignedShortAttribute columnsAttr = (UnsignedShortAttribute) attributeList.get(TagFromName.Columns);
                    final short columns = columnsAttr.getShortValues()[0];
                    System.out.println("columns = " + columns);
                    System.out.println("rows = " + rows);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DicomException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    static String getTagInformation(AttributeTag attributeTag){
        return Attribute.getDelimitedStringValuesOrEmptyString(attributeList, attributeTag);
    }
}
