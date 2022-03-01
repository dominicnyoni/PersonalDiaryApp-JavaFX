
package diary;

import Controllers.settings.Preferences;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Diary extends Application {
 private final static Logger LOGGER = LogManager.getLogger(Diary.class.getName());
 Preferences preference;
 


    @Override
    public void start(Stage stage) throws Exception{
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/Views/login.fxml"));
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        
    }
  
 public static void main(String[]args){
    Long startTime = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Diary launched on {}", utils.DiaryUtil.formatDateTimeString(startTime));
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Long exitTime = System.currentTimeMillis();
                LOGGER.log(Level.INFO, "Diary is closing on {}. Used for {} ms", utils.DiaryUtil.formatDateTimeString(startTime), exitTime);
            }
        });
    }
}
