/*
 *
 */
package Controllers.readingPage;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import utils.Alert.AlertMaker;
import model.database.DatabaseHandle;
import com.jfoenix.controls.JFXTextArea;
import Controllers.MainWindow.MainWindowController.Page;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import utils.DiaryUtil;
import Controllers.writingPage.WritepageController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import Controllers.MainWindow.MainWindowController;


public class ReadingController implements Initializable {

    @FXML
    private JFXTextField dateField;
    @FXML
    private JFXTextField subjectField;
    @FXML
    private JFXTextArea contentField;
    @FXML
    private AnchorPane bgPane;
    @FXML
    private StackPane mainContainer;
    

    private DatabaseHandle databaseHandler;
    private MainWindowController.Page page;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandle.getInstance();
    }

    
    @FXML
    private void handleDelete(ActionEvent event) {
        Page selectedForDeletion = page;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting page");
        alert.setContentText("Are you sure want to delete the page " + selectedForDeletion.getDate() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            Boolean result = DatabaseHandle.getInstance().deletePage(selectedForDeletion);
            if (result) {
                AlertMaker.showSimpleAlert("Page deleted", selectedForDeletion.getDate() + " was deleted successfully.");
                MainWindowController.list.remove(selectedForDeletion);
            } else {
                AlertMaker.showSimpleAlert("Failed", selectedForDeletion.getDate() + " could not be deleted");
            }
        } else {
            AlertMaker.showSimpleAlert("Deletion cancelled", "Deletion process cancelled");
        }
    }
    
    @FXML
    private void saveAsPDF(){
    String[] printData = new String[3];
    
       
            printData[0]=page.getDate();
            
            printData[1]=page.getSubject();
            
            printData[2]=page.getContent();
            
        
        DiaryUtil.exportToPDF(mainContainer,bgPane,getStage(), printData);
}
     private Stage getStage() {
        return (Stage) bgPane.getScene().getWindow();
    }

    
@FXML
private void handleEdit() {
try{
 FXMLLoader  loader=new FXMLLoader(getClass().getResource("/Views/Writepage.fxml"));
 Parent parent=loader.load();

WritepageController control=(WritepageController)loader.getController();
control.fillWritePage(page);

Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(parent));
            DiaryUtil.setStageIcon(stage);
            stage.setTitle("Edit Mode");
             stage.setResizable(false);
            stage.show();


        } catch (IOException ex) {
            Logger.getLogger(ReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    
    /*The method collects data from the page selected
     from the diary entries table and populates 
     the date field,the subject field and the content field.
*/
    public void fillReadingPage(MainWindowController.Page page) {
        this.page=page;
        dateField.setText(page.getDate());
        subjectField.setText(page.getSubject());
        contentField.setText(page.getContent());
      
        
    }

    
   
   
}
