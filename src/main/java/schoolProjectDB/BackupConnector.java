package schoolProjectDB;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;


public class BackupConnector {

    public static void main(String[] args) {


        try {
            makeBackUp();
            loadBackUp();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void makeBackUp() throws URISyntaxException, IOException, InterruptedException {
        CodeSource codeSource = BackupConnector.class.getProtectionDomain().getCodeSource();
        File jarFile = new File(codeSource.getLocation().toURI().getPath());
        String jarDir = jarFile.getParentFile().getPath();

        String dbName = "schoolDB";
        String dbUser = "root";
        String dbPass = "--------"; //TODO insert your password

        /*NOTE: Creating Path Constraints for folder saving*/
        /*NOTE: Here the backup folder is created for saving inside it*/
        String folderPath = jarDir + "\\backup";
        /*NOTE: Creating Folder if it does not exist*/
        File f1 = new File(folderPath);
        f1.mkdir();

        /*NOTE: Creating Path Constraints for backup saving*/
        /*NOTE: Here the backup is saved in a folder called backup with the name backup.sql*/
        String savePath = "\"" + jarDir + "/" + "backup.sql\"";

        /*NOTE: Used to create a cmd command*/
        String executeCmd = "mysqldump -u" + dbUser + " -p" + dbPass + " " + dbName + " -r " + savePath;
        String[] cmdarr = {"/bin/sh", "-c", executeCmd};
        System.out.println(executeCmd);
        Process runtimeProcess = Runtime.getRuntime().exec(cmdarr);
        int processComplete = runtimeProcess.waitFor();

        if (processComplete == 0) {
            JOptionPane.showMessageDialog(null, "Kopia zapasowa została wykonana");
        } else {
            JOptionPane.showMessageDialog(null, "Bład poczas tworzenia kopii");


        }
    }

    public static void loadBackUp() throws URISyntaxException, IOException, InterruptedException {

        CodeSource codeSource = BackupConnector.class.getProtectionDomain().getCodeSource();
        File jarFile = new File(codeSource.getLocation().toURI().getPath());
        String jarDir = jarFile.getParentFile().getPath();

        String dbName = "schoolDB";
        String dbUser = "root";
        String dbPass = "--------"; //TODO insert your password


        String restorePath = "\"" + jarDir + "/" + "backup.sql\"";

        String executeCmd = "mysql " + " -u" + dbUser + " -p" + dbPass + " " + dbName + " < " + restorePath;
        String[] cmdarr = {"/bin/sh", "-c", executeCmd};
        System.out.println(executeCmd);


        Process runtimeProcess = Runtime.getRuntime().exec(cmdarr);
        int processComplete = runtimeProcess.waitFor();


        if (processComplete == 0) {
            JOptionPane.showMessageDialog(null, "Wczytano kopie zapasową");
        } else {
            JOptionPane.showMessageDialog(null, "Błąd podczas wczytywania kopii zapasowej");
        }
    }
}
