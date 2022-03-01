
package model.database;


import Controllers.MainWindow.MainWindowController;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.DiaryPage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DataHelperBase {

    private final static Logger LOGGER = LogManager.getLogger(DatabaseHandle.class.getName());

    public static boolean insertNewPage(DiaryPage page) {
        try {
            PreparedStatement statement = DatabaseHandle.getInstance().getConnection().prepareStatement(
                    "INSERT INTO PAGE(date,subject,content) VALUES(?,?,?)");
            statement.setString(1, page.getDate());
            statement.setString(2, page.getTitle());
            statement.setString(3, page.getContent());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }

    

    public static boolean isPageExists(String date) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM PAGE WHERE DATE=?";
            PreparedStatement stmt = DatabaseHandle.getInstance().getConnection().prepareStatement(checkstmt);
            stmt.setString(1, date);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println(count);
                return (count > 0);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }

  public static void wipeTable(String tableName) {
        try {
            Statement statement = DatabaseHandle.getInstance().getConnection().createStatement();
            statement.execute("DELETE FROM " + tableName + " WHERE TRUE");
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
    }
  public static void removeTable(String tableName){
  try{
      Statement statement =DatabaseHandle.getInstance().getConnection().createStatement();
      statement.execute("DROP TABLE"+" "+tableName);
      System.out.println(tableName+"is deleted");
  }catch(SQLException ex){
  LOGGER.log(Level.ERROR,"{}", ex);
  }
  }


   

   
}
