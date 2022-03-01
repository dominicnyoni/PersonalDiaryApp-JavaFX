/*The search controller handles the matching
of the keyword from the user with the diary entries in the database
and return the pages containing matches in a table view.
The pattern matching algorithm used is the boyermoore algorithm.
 * 
 */
package Controllers.search;


import Controllers.MainWindow.MainWindowController;
import Controllers.MainWindow.MainWindowController.Page;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.database.DatabaseHandle;
import utils.Alert.AlertMaker;

public class SearchController extends MainWindowController implements Initializable{


@FXML
private TableView<Page> entriesTable;
@FXML
private ScrollPane entriesScrollPane;
@FXML
private TableColumn<Page, String> dateColumn;
@FXML
private TableColumn<Page, String> subjectColumn;
@FXML
private TableColumn<Page, String> contentColumn;
@FXML
private  String searchString;


ObservableList<Page> matchList=FXCollections.observableArrayList();
DatabaseHandle databaseHandler;

   

@Override
public void initialize(URL url,ResourceBundle rs){

databaseHandler = DatabaseHandle.getInstance();    

}

//for getting the search keyword from the user
public void passSearchKeyword(String keyword){
initFoundPages(keyword);
}

//loads the pages with positive matches into a table view    
private  void initFoundPages(String keyword){
col();
loadData(keyword);
}

@Override
protected void col() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        }


protected void loadData(String string){
    
findMatches(string);
entriesTable.setItems(matchList);
if(matchList.isEmpty())
   AlertMaker.showSimpleAlert("Sorry,no match found", "Try another keyword");
}



private void findMatches(String searchedWord){
matchList.clear();
String sourceText;
for(MainWindowController.Page page:MainWindowController.list){
 sourceText=page.getDate()+page.getSubject()+page.getContent();
  if (findMatch(sourceText.toLowerCase(),searchedWord.toLowerCase())){
     matchList.add(page);
  }
  
  
}
}

                                                
//Boyermoore algorithm for pattern matching,returns true when match is found
//Source code for algorithm from 'AlgorithmsFourthEdition' by R.Sedgewick Chapter 5 page 772
private boolean findMatch(String text,String pattern){
int n=text.length();
int m=pattern.length();

Map<Character,Integer>skiptable=new HashMap<>();
for(int i=0;i<m;i++){
skiptable.put(pattern.charAt(i),Math.max(1,m-i-1));
}
int skips;                                                
for(int i=0;i<=n-m;i+=skips){             
int k=m-1;                                  
                                                              
while(k>0 && text.charAt(i+k)==pattern.charAt(k))                         
k--;                                                                       
if(k==0){                                                                        
return true;
}else{
   if(skiptable.get(text.charAt(i+k))!=null){
   skips=skiptable.get(text.charAt(i+k));
   }else{
   skips=m;
   }
}

}
return false;
}

    
}

