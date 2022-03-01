
package Controllers.MainWindow;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import utils.DiaryUtil;

public class ToolbarController implements Initializable {
 
 @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    @FXML
    private void addNewEntry(ActionEvent event) {
        DiaryUtil.loadWindow(getClass().getResource("/Views/Writepage.fxml"), "Add New Entry", null);
    }

    

    @FXML
    private void loadSettings(ActionEvent event) {
        DiaryUtil.loadWindow(getClass().getResource("/Views/Settings/settings.fxml"), "Settings", null);
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

   

    
    

}