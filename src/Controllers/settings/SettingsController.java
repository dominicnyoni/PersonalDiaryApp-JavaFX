
package Controllers.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.database.DatabaseHandle;
import model.database.export.DatabaseExport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Alert.AlertMaker;


public class SettingsController implements Initializable {
   
@FXML
 private JFXPasswordField passwordField;

@FXML
private JFXButton loginSaveButton;
@FXML
private JFXButton exportDatabase;
@FXML private JFXSpinner progressSpinner;

private final static Logger LOGGER = LogManager.getLogger(DatabaseHandle.class.getName());

    
    @Override
    public void initialize(URL url,ResourceBundle rb){
    initDefaultValues();
        
    }
    
    private void initDefaultValues(){
    Preferences preferences = Preferences.getPreferences(); 
    String passwordHash = String.valueOf(preferences.getPassword());
    passwordField.setText(passwordHash.substring(0, Math.min(passwordHash.length(), 10)));
   
    }
    
    @FXML
    private void handleLoginSettings(ActionEvent event) {
        String thePassword=passwordField.getText();
        Preferences preferences = Preferences.getPreferences();
        preferences.setPassword(thePassword);
        Preferences.writePreferenceToFile(preferences);
    }
    
     @FXML
    private void createLocalBackup(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Location to Create Backup");
        File selectedDirectory = directoryChooser.showDialog(getStage());
        if (selectedDirectory == null) {
            AlertMaker.showErrorMessage("Export cancelled", "No Valid Directory Found");
        } else {
            DatabaseExport databaseExporter = new DatabaseExport(selectedDirectory);
            progressSpinner.visibleProperty().bind(databaseExporter.runningProperty());
            new Thread(databaseExporter).start();
            
        }
    }

    private Window getStage() {
     return ((Stage) passwordField.getScene().getWindow());    
        
    }
    
}
