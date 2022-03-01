
package model.database.export;


import java.io.File;
import java.sql.CallableStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.concurrent.Task;
import utils.Alert.AlertMaker;
import model.database.DatabaseHandle;
import utils.DiaryUtil;


public class DatabaseExport extends Task<Boolean> {

    private final File backupDirectory;

    public DatabaseExport(File backupDirectory) {
        this.backupDirectory = backupDirectory;
    }

    @Override
    protected Boolean call() {
        try {
            createBackup();
            return true;
        } catch (Exception exp) {
            AlertMaker.showErrorMessage(exp);
        }
        return false;
    }

    private void createBackup() throws Exception {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_ss");
        String backupdirectory = backupDirectory.getAbsolutePath() + File.separator + LocalDateTime.now().format(dateFormat);
        try (CallableStatement cs = DatabaseHandle.getInstance().getConnection().prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)")) {
            cs.setString(1, backupdirectory);
            cs.execute();
        }
        File file = new File(backupdirectory);
        DiaryUtil.openFileWithDesktop(file);
    }
}

