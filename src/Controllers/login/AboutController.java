/**
 *
 * 
 * 
 */
package Controllers.login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class AboutController implements Initializable{
    private static final String EMAIL="dominicnyoni60@gmail.com";
    Text mainMessage=new Text();
    Text contacts=new Text();
   
    
    
    @FXML
    private TextFlow message;
    
    @Override
    public void initialize(URL url,ResourceBundle rb){
    mainMessage.setText(String.format("Hello %s!", System.getProperty("user.name").toUpperCase())+ "Thanks for checking out my Personal Diary App!\n");
    contacts.setText("Contact me at :"+" "+EMAIL);
    message.getChildren().addAll(mainMessage,contacts);
    
    }
    
}
