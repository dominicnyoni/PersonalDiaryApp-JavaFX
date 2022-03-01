/*
This is the controller for the login window.
The controller mainly handles the login button which tests if password entered by user matches the one saved in the 
preferences,and loads main window if match is true;

 */
package Controllers.login;

import com.jfoenix.controls.JFXPasswordField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Controllers.settings.Preferences;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.DiaryUtil;

public class LoginController implements Initializable {

    private final static Logger LOGGER = LogManager.getLogger(LoginController.class.getName());

   
    @FXML
    private JFXPasswordField passwordField;
    

    Preferences preference;
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preference = Preferences.getPreferences();
        
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
       String pword = DigestUtils.shaHex(passwordField.getText());
       
       

        if (pword.equals(preference.getPassword())) {
            closeStage();
            loadMain();
            LOGGER.log(Level.INFO, "User successfully logged in");
        }
        else {
          passwordField.getStyleClass().add("wrong-credentials");
        }
    }
    
     @FXML
    private void handleCancelButton(ActionEvent event) {
        System.exit(0);
    }
    
@FXML
public void handleAbout(ActionEvent event){
DiaryUtil.loadWindow(getClass().getResource("/Views/about.fxml"), "About", null);
}
   

    private void closeStage() {
        ((Stage) passwordField.getScene().getWindow()).close();
    }

    public void loadMain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Views/MainWindow.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            DiaryUtil.setStageIcon(stage);
            stage.setTitle("Personal Diary");
            stage.setResizable(false);
            stage.setScene(new Scene(parent));
            stage.show();
            
        }
        catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
    }

}


