
package Controllers.writingPage;


import Controllers.MainWindow.MainWindowController;
import static Controllers.MainWindow.MainWindowController.list;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utils.Alert.AlertMaker;
import model.DiaryPage;
import model.database.DataHelperBase;
import model.database.DatabaseHandle;
import java.text.SimpleDateFormat;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

public class WritepageController  implements Initializable {
   private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
   private  Date date=new Date(System.currentTimeMillis());
    
   
    private DatabaseHandle databaseHandler;
    private MainWindowController.Page page;

    @FXML
    private JFXTextField dateField;
    @FXML
    private JFXTextField subjectField;
    @FXML
    private JFXTextArea contentField;
   @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;
    @FXML
    private VBox vbox;
   
  
   
    
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandle.getInstance();
         dateField.setText(formatter.format(date));
    }
@FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }
    
 private Stage getStage() {
        return (Stage) rootPane.getScene().getWindow();
    }
 
@FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

@FXML
  
    private void handleSaveOrUpdate(ActionEvent event) {
        
    
        page=new MainWindowController.Page(dateField.getText(), subjectField.getText(), contentField.getText());
        if(DataHelperBase.isPageExists(page.getDate())){
            
            if(databaseHandler.updatePage(page))
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(),"Success","Update completed");
            else
                AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(),"Failed","Update failed");
            
          
        }else{
        String pageDate = StringUtils.trimToEmpty(dateField.getText());
        String pageSubject = StringUtils.trimToEmpty(subjectField.getText());
        String pageContent = StringUtils.trimToEmpty(contentField.getText());
     

        if (pageContent.isEmpty() ) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed to Save", "Content cannot be empty\n"+"Please write some content.");
            return;
        }

       DiaryPage entry = new DiaryPage(pageDate, pageSubject, pageContent);
        boolean result = DataHelperBase.insertNewPage(entry);
        if (result) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Page Saved","New page added to diary\n"+ "Great!");
            clearEntries();
           
            list.add(new MainWindowController.Page(pageDate,pageSubject,pageContent));
           
     
            
            
        } else {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed to add new page", "Check all the entries and try again");
        }
        }
    }
    
   /*
    The method is used to transfer page data from Reading
    Page to WritePage for the purpose of editing.
    */
  public void fillWritePage(MainWindowController.Page page) {
        
        dateField.setText(page.getDate());
        subjectField.setText(page.getSubject());
        contentField.setText(page.getContent());
      
        
    }   

 private void clearEntries() {
        dateField.clear();
        subjectField.clear();
        contentField.clear();
        
    }
}

