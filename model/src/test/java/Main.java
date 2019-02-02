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
                fileChooser.setTitle("Выберете DICOM файл");
                File file = fileChooser.showOpenDialog(null);
                attributeList = new AttributeList();
                SUVTransform suvTransform = new SUVTransform(attributeList);
                final SUVTransform.SingleSUVTransform singleSUVTransform = suvTransform.getSingleSUVTransform(0);
                try {
                    attributeList.read(file);
//                    System.out.println(attributeList.toString());
//                    Attribute pixelData = attributeList.getPixelData();
//                    System.out.println(pixelData.toString());
//                    byte[] byteValues = pixelData.getByteValues();
                    SourceImage sourceImage = new SourceImage(attributeList);
                    final String modalityStr = getTagInformation(TagFromName.Modality);
                    System.out.println("Modality: " + modalityStr);
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
                    System.out.println("Series date: " + getTagInformation(TagFromName.SeriesDate));
                    System.out.println("Series time: " + getTagInformation(TagFromName.SeriesTime));
                    System.out.println("Units: " + getTagInformation(TagFromName.Units));
                    System.out.println("Dose units: " + getTagInformation(TagFromName.DoseUnits));
                    if (modalityStr.equals("PT")) {
                        final SequenceAttribute petDose = (SequenceAttribute) attributeList.get(TagFromName.RadiopharmaceuticalInformationSequence);
                        final SequenceItem item = petDose.getItem(0);
                        final AttributeList petAttributeList = item.getAttributeList();
                        final String doseStr = petAttributeList.get(TagFromName.RadionuclideTotalDose).getDelimitedStringValuesOrEmptyString();
                        System.out.println("Injected dose: " + doseStr);
                        System.out.println("Injected start time: " + petAttributeList.get(TagFromName.RadiopharmaceuticalStartTime).getDelimitedStringValuesOrEmptyString());
                    }
                    BufferedImage bufferedImage = sourceImage.getBufferedImage();
                    final WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
                    Stage stage = new Stage();
                    BorderPane borderPane = new BorderPane();
                    ImageView imageView = new ImageView(writableImage);
                    borderPane.setCenter(imageView);
                    Scene scene = new Scene(borderPane, 600, 600);
                    stage.setScene(scene);
                    stage.show();
                    OtherWordAttribute otherByteAttribute = (OtherWordAttribute) Main.attributeList.get(TagFromName.PixelData);
                    short[] pixelData = otherByteAttribute.getShortValues();
                    System.out.println("pixelData.length = " + pixelData.length);
                    UnsignedShortAttribute bitsStoredAttribute = (UnsignedShortAttribute) Main.attributeList.get(TagFromName.BitsStored);
                    short[] shortValues = bitsStoredAttribute.getShortValues();
                    System.out.println("shortValues.length = " + shortValues.length);
                    System.out.println("shortValues[0] = " + shortValues[0]);
                    CodeStringAttribute photoInterpret = (CodeStringAttribute) Main.attributeList.get(TagFromName.PhotometricInterpretation);
                    String photoStr = photoInterpret.getDelimitedStringValuesOrEmptyString();
                    System.out.println("photoStr = " + photoStr.toLowerCase());
                    final UnsignedShortAttribute rowsAttr = (UnsignedShortAttribute) Main.attributeList.get(TagFromName.Rows);
                    final short rows = rowsAttr.getShortValues()[0];
                    final UnsignedShortAttribute columnsAttr = (UnsignedShortAttribute) Main.attributeList.get(TagFromName.Columns);
                    final short columns = columnsAttr.getShortValues()[0];
                    System.out.println("columns = " + columns);
                    System.out.println("rows = " + rows);
                    final DecimalStringAttribute imagePos = (DecimalStringAttribute) Main.attributeList.get(TagFromName.ImagePositionPatient);
                    final String imgPos = imagePos.getDelimitedStringValuesOrEmptyString();
                    final String[] split = imgPos.split("\\\\");
                    System.out.println("imagePos = " + String.format("[%s, %s, %s]", split[0], split[1], split[2]));
                    final DecimalStringAttribute sliceLocation = (DecimalStringAttribute) Main.attributeList.get(TagFromName.SliceLocation);
                    System.out.println("sliceLocation = " + sliceLocation.getDelimitedStringValuesOrEmptyString());
                    System.out.println("imageIndex = " + getTagInformation(TagFromName.ImageIndex));
                    System.out.println("Aquition date = " + getTagInformation(TagFromName.AcquisitionDate));
                    System.out.println("Acquituin time = " + getTagInformation(TagFromName.AcquisitionTime));
                    System.out.println("SpacingBetweenSlices = " + getTagInformation(TagFromName.SpacingBetweenSlices));
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
