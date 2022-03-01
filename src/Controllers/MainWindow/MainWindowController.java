
package Controllers.MainWindow;

/**
 *
 * 
 */
import Controllers.login.LoginController;
import Controllers.readingPage.ReadingController;
import Controllers.search.SearchController;
import Controllers.MainWindow.MainWindowController.Page;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.Alert.AlertMaker;
import model.database.DatabaseHandle;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import utils.DiaryUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextFlow;
import javafx.stage.StageStyle;

public class MainWindowController implements Initializable{
 
    


public static ObservableList<Page> list = FXCollections.observableArrayList();






@FXML
private JFXButton newEntryButton;
@FXML
private JFXTextField searchField;
@FXML
private JFXHamburger hamburgerMenu;

@FXML
private TextFlow quoteField;
@FXML
private TableView<Page> entriesTable;
@FXML
private TableColumn<Page, String> dateColumn;
@FXML
private TableColumn<Page, String> subjectColumn;
@FXML
private TableColumn<Page, String> contentColumn;
@FXML
private StackPane rootPane;
@FXML
private JFXDrawer drawer;

private Text qText;
DatabaseHandle databaseHandle;

 @Override
 public void initialize(URL url,ResourceBundle rb){
databaseHandle=DatabaseHandle.getInstance();

initHamburgerMenu();
initDiaryEntries();
initQuote();
}
 



@FXML
    private void handleSearch(ActionEvent event) {
 try{ 
FXMLLoader loader=new FXMLLoader(getClass().getResource("/Views/searchScreen.fxml"));
Parent parent=loader.load();

SearchController controller=(SearchController)loader.getController();
controller.passSearchKeyword(searchField.getText());

Stage stage = new Stage(StageStyle.DECORATED);
stage.setTitle("Search Results");
stage.setResizable(false);
stage.setScene(new Scene(parent));
stage.show();
DiaryUtil.setStageIcon(stage);

}catch (IOException ex) {
            Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

 @FXML
    private void addNewEntry(ActionEvent event) {
        DiaryUtil.loadWindow(getClass().getResource("/Views/Writepage.fxml"), "Writing Mode", null);
    }


private void initHamburgerMenu(){
 try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/toolbar/tools.fxml"));
            VBox toolbar = loader.load();
            drawer.setSidePane(toolbar);
            
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburgerMenu);
        task.setRate(-1);
        hamburgerMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            drawer.toggle();
        });
        drawer.setOnDrawerOpening((event) -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.toFront();
        });
        drawer.setOnDrawerClosed((event) -> {
            drawer.toBack();
            task.setRate(task.getRate() * -1);
            task.play();
        });
    }


  private Stage getStage() {
        return (Stage) entriesTable.getScene().getWindow();
    }
  
  public void initDiaryEntries(){
col();
loadData();
}

 protected void col() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        }

public void loadData() {
        list.clear();
        DatabaseHandle handler = DatabaseHandle.getInstance();
        String qu = "SELECT * FROM PAGE";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String theDate = rs.getString("date");
                String theSubject = rs.getString("subject");
                String theContent = rs.getString("content");
                
                list.add(new Page(theDate, theSubject,theContent));
                
                
                
                
                
             }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
       
        entriesTable.setItems(list);
        
    }



private void initQuote(){

//initialize quote
qText=new Text();
qText.setText(quoteArray[(int)(Math.random()*quoteArray.length)]);
quoteField.getChildren().add(qText);
qText.getStyleClass().add("quote");
}

@FXML
    protected void handleDelete(ActionEvent event) {
        //Fetch the selected row
        Page selectedForDeletion = entriesTable.getSelectionModel().getSelectedItem();
        if (selectedForDeletion == null) {
            AlertMaker.showErrorMessage("No Page selected", "Please select a page for deletion.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting page");
        alert.setContentText("Are you sure want to delete the page " + selectedForDeletion.getDate() + " ?");
      
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            Boolean result = DatabaseHandle.getInstance().deletePage(selectedForDeletion);
            if (result) {
                AlertMaker.showSimpleAlert("Page deleted", selectedForDeletion.getDate() + " was deleted successfully.");
                list.remove(selectedForDeletion);
                
            } else {
                AlertMaker.showSimpleAlert("Failed", selectedForDeletion.getDate() + " could not be deleted");
            }
        } else {
            AlertMaker.showSimpleAlert("Deletion cancelled", "Deletion process cancelled");
        }
    }

@FXML
    public void handleRefresh(ActionEvent event) {
        loadData();
    }

@FXML
    protected void readEntry(ActionEvent event) {
        
        Page selectedForEdit = entriesTable.getSelectionModel().getSelectedItem();//For fetching the selected row
        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("No Page selected", "Please select a page for edit.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ReadingPage.fxml"));
            Parent parent = loader.load();

            ReadingController controller = (ReadingController) loader.getController();
            controller.fillReadingPage(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene(parent));
            stage.setTitle("Reading Mode");
            stage.setResizable(false);
            stage.show();
            DiaryUtil.setStageIcon(stage);

            stage.setOnHiding((e) -> {
                handleRefresh(new ActionEvent());
            });

        } catch (IOException ex) {
            Logger.getLogger(ReadingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


@FXML
protected void saveAllPagesAsPDF(ActionEvent event) {
        
    DiaryUtil.exportToPDF(rootPane,entriesTable ,getStage(),list);
        
    }

    @FXML
    protected void closeStage(ActionEvent event) {
        getStage().close();
    }
    
    @FXML
    public void handleAbout(ActionEvent e){
    LoginController con=new LoginController();
    con.handleAbout(e);
    }
    
   
    
    public static class Page {

        private final SimpleStringProperty subject;
        private final SimpleStringProperty date;
        private final SimpleStringProperty content;
       

        public Page(String date, String subject, String content) {
            this.date = new SimpleStringProperty(date);
            this.subject = new SimpleStringProperty(subject);
            
            this.content = new SimpleStringProperty(content);
            
         }
       
        

        public String getSubject() {
            return subject.get();
        }

        public String getDate() {
            return date.get();
        }

        public String getContent() {
            return content.get();
        }

       
    }
     private final String[] quoteArray={
"If you want to achieve greatness stop asking for permission. --Anonymous" ,
"Things work out best for those who make the best of how things work out. --John Wooden ",
 "To live a creative life, we must lose our fear of being wrong. --Anonymous" ,
 "If you are not willing to risk the usual you will have to settle for the ordinary. --Jim Rohn", 
 "Trust because you are willing to accept the risk, not because it's safe or certain. --Anonymous ",
"Take up one idea. Make that one idea your life--think of it, dream of it, live on that idea. Let the brain, muscles, nerves, every part of your body, be full of that idea, and just leave every other idea alone. This is the way to success. --Swami Vivekananda ",
 "All our dreams can come true if we have the courage to pursue them. --Walt Disney", 
 "Good things come to people who wait, but better things come to those who go out and get them. -Anonymous ",
"If you do what you always did, you will get what you always got.  --Anonymous", 
 "Success is walking from failure to failure with no loss of enthusiasm. --Winston Churchill", 
 "Just when the caterpillar thought the world was ending, he turned into a butterfly. --Proverb ",
"Successful entrepreneurs are givers and not takers of positive energy. --Anonymous ",
"Whenever you see a successful person you only see the public glories, never the private sacrifices to reach them. --Vaibhav Shah ",
"Opportunities don't happen, you create them. --Chris Grosser", 
"Try not to become a person of success, but rather try to become a person of value. --Albert Einstein", 
"Great minds discuss ideas; average minds discuss events; small minds discuss people. --Eleanor Roosevelt ",
"I have not failed. I've just found 10,000 ways that won't work. --Thomas A. Edison ",
"If you don't value your time, neither will others. Stop giving away your time and talents--start charging for it. --Kim Garst ",
"A successful man is one who can lay a firm foundation with the bricks others have thrown at him. -David Brinkley", 
 "No one can make you feel inferior without your consent.  --Eleanor Roosevelt ",
"The whole secret of a successful life is to find out what is one's destiny to do, and then do it.  -Henry Ford ",
"If you're going through hell keep going. --Winston Churchill ",
"The ones who are crazy enough to think they can change the world, are the ones who do. -Anonymous ",
"Don't raise your voice, improve your argument. --Anonymous ",
 "What seems to us as bitter trials are often blessings in disguise. --Oscar Wilde ",
"The meaning of life is to find your gift. The purpose of life is to give it away. --Anonymous", 
 "The distance between insanity and genius is measured only by success. --Bruce Feirstein ",
 "When you stop chasing the wrong things, you give the right things a chance to catch you. --Lolly Daskal ",
"Don't be afraid to give up the good to go for the great. --John D. Rockefeller ",
"No masterpiece was ever created by a lazy artist. --Anonymous ",
"Happiness is a butterfly, which when pursued, is always beyond your grasp, but which, if you will sit down quietly, may alight upon you. --Nathaniel Hawthorne ",
"If you can't explain it simply, you don't understand it well enough. --Albert Einstein ",
"Blessed are those who can give without remembering and take without forgetting. --Anonymous ",
 "Do one thing every day that scares you. --Anonymous ",
 "What's the point of being alive if you don't at least try to do something remarkable. --Anonymous ",
"Life is not about finding yourself. Life is about creating yourself. --Lolly Daskal", 
"Nothing in the world is more common than unsuccessful people with talent. --Anonymous ",
 "Knowledge is being aware of what you can do. Wisdom is knowing when not to do it. --Anonymous ", 
"Your problem isn't the problem. Your reaction is the problem. --Anonymous ",
"You can do anything, but not everything. --Anonymous ",
"Innovation distinguishes between a leader and a follower. --Steve Jobs", 
"There are two types of people who will tell you that you cannot make a difference in this world: those who are afraid to try and those who are afraid you will succeed. --Ray Goforth", 
"Thinking should become your capital asset, no matter whatever ups and downs you come across in your life. --A.P.J. Abdul Kalam  ",
"I find that the harder I work, the more luck I seem to have.--Thomas Jefferson ",
"The starting point of all achievement is desire. --Napoleon Hill ",
"Success is the sum of small efforts, repeated day-in and day-out. --Robert Collier ",
"If you want to achieve excellence, you can get there today. As of this second, quit doing less-thanexcellent work. --Thomas J. Watson ",
"All progress takes place outside the comfort zone. --Michael John Bobak", 
"You may only succeed if you desire succeeding; you may only fail if you do not mind failing.  -Philippos ",
"Courage is resistance to fear, mastery of fear--not absense of fear. --Mark Twain ",
"Only put off until tomorrow what you are willing to die having left undone. --Pablo Picasso ",
 "People often say that motivation doesn't last. Well, neither does bathing--that's why we recommend it daily. --Zig Ziglar ",
"We become what we think about most of the time, and that's the strangest secret. --Earl Nightingale ",
 "The only place where success comes before work is in the dictionary. --Vidal Sassoon ",
"The best reason to start an organization is to make meaning; to create a product or service to make the world a better place. --Guy Kawasaki ",
"I find that when you have a real interest in life and a curious life, that sleep is not the most important thing. --Martha Stewart  ",
"It's not what you look at that matters, it's what you see. --Anonymous ",
 "The road to success and the road to failure are almost exactly the same. --Colin R. Davis ",
 "The function of leadership is to produce more leaders, not more followers. --Ralph Nader ",
"Success is liking yourself, liking what you do, and liking how you do it. --Maya Angelou ",
"As we look ahead into the next century, leaders will be those who empower others.--Bill Gates ",
"If you genuinely want something, don't wait for it--teach yourself to be impatient. --Gurbaksh Chahal ",
"Don't let the fear of losing be greater than the excitement of winning. --Robert Kiyosaki ",
"If you want to make a permanent change, stop focusing on the size of your problems and start focusing on the size of you! --T. Harv Eker ",
"You can't connect the dots looking forward; you can only connect them looking backwards. So you have to trust that the dots will somehow connect in your future. You have to trust in something--your gut, destiny, life, karma, whatever. This approach has never let me down, and it has made all the difference in my life. --Steve Jobs ",
"Successful people do what unsuccessful people are not willing to do. Don't wish it were easier, wish you were better. --Jim Rohn ",
"The number one reason people fail in life is because they listen to their friends, family, and neighbors. --Napoleon Hill ",
"The reason most people never reach their goals is that they don't define them, or ever seriously consider them as believable or achievable. Winners can tell you where they are going, what they plan to do along the way, and who will be sharing the adventure with them. --Denis Waitley ",
"In my experience, there is only one motivation, and that is desire. No reasons or principle contain it or stand against it. --Jane Smiley ",
"Success does not consist in never making mistakes but in never making the same one a second time.--George Bernard Shaw ",
 "I don't want to get to the end of my life and find that I lived just the length of it. I want to have lived the width of it as well. --Diane Ackerman", 
"You must expect great things of yourself before you can do them. --Michael Jordan ",
"Motivation is what gets you started. Habit is what keeps you going. --Jim Ryun ",
"People rarely succeed unless they have fun in what they are doing.--Dale Carnegie ",
"There is no chance, no destiny, no fate, that can hinder or control the firm resolve of a determined soul.--Ella Wheeler Wilcox ",
"Our greatest fear should not be of failure but of succeeding at things in life that don't really matter. --Francis Chan ",
"You've got to get up every morning with determination if you're going to go to bed with satisfaction. --George Lorimer ",
"To be successful you must accept all challenges that come your way. You can't just accept the ones you like. --Mike Gafka ",
"Success is ... knowing your purpose in life, growing to reach your maximum potential, and sowing seeds that benefit others. --John C. Maxwell ",
"Be miserable. Or motivate yourself. Whatever has to be done, it's always your choice. --Wayne Dyer", 
"To accomplish great things, we must not only act, but also dream, not only plan, but also believe. -Anatole France ",
 "Most of the important things in the world have been accomplished by people who have kept on trying when there seemed to be no help at all. --Dale Carnegie", 
"You measure the size of the accomplishment by the obstacles you had to overcome to reach your goals. --Booker T. Washington", 
 "Real difficulties can be overcome; it is only the imaginary ones that are unconquerable. --Theodore N. Vail ",
"It is better to fail in originality than to succeed in imitation. --Herman Melville ",
"Fortune sides with him who dares. --Virgil", 
 "Little minds are tamed and subdued by misfortune; but great minds rise above it. --Washington Irving", 
"Failure is the condiment that gives success its flavor. --Truman Capote ",
"Don't let what you cannot do interfere with what you can do.--John R. Wooden ",
"You may have to fight a battle more than once to win it. --Margaret Thatcher ",
"A man can be as great as he wants to be. If you believe in yourself and have the courage, the determination, the dedication, the competitive drive and if you are willing to sacrifice the little things in life and pay the price for the things that are worthwhile, it can be done. --Vince Lombardi"
};
    
}






