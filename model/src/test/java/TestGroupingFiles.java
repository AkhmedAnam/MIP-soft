import com.pixelmed.dicom.*;
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
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

public class TestGroupingFiles {

    private static AttributeList attributeList;
    static Set<String> imagOrientations = new HashSet<>();

    public static void main(String[] args) {
        new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                        "DICOM files",
                        "*dcm", "*dicom");
                fileChooser.setSelectedExtensionFilter(extensionFilter);
                File choosenFile = fileChooser.showOpenDialog(null);
                File folder = choosenFile.getParentFile();
                File[] files = folder.listFiles();
                int i = 0;
                for (File file : files) {
                    attributeList = new AttributeList();
                    try {
                        attributeList.read(file);
                        DecimalStringAttribute imageOrientation = (DecimalStringAttribute) attributeList.get(TagFromName.ImageOrientationPatient);
                        String[] stringValues = imageOrientation.getStringValues(NumberFormat.getNumberInstance());
                        for (String stringValue : stringValues) {
                            System.out.print(stringValue + " ");
                        }
                        System.out.println("");
                        i++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DicomException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("i = " + i);
            }
        });
    }
}
