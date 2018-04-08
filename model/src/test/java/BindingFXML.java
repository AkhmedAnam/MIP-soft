import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class BindingFXML {

    @FXML
    private Button cancelBtn;

    public void okHandler(ActionEvent actionEvent){
        boolean visible = cancelBtn.isVisible();
        cancelBtn.setVisible(!visible);
    }
}
