
package Controllers.MainWindow;

/**
 *
 * 
 */
import Controllers.login.LoginController;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainWindowLauncher extends Application {
   private final static Logger LOGGER = LogManager.getLogger(LoginController.class.getName());  
    @Override
    public void start(Stage stage) throws Exception {
        try{
        Parent root = FXMLLoader.load(getClass().getResource("/Views/userPage.fxml"));
     stage.initStyle(StageStyle.DECORATED);
      stage.setScene(new Scene(root));
            stage.show();
        }catch(IOException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
