package nishat.net.Helper;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.PrintWriter;

import java.util.Date;

import javax.servlet.http.HttpSession;

public class Logger {
    String rootFolderPath = "//home//oracle//Desktop//HtdSalesPlanningLogs";
    File rootFolder;
    PrintWriter pw ;
    
    String username;

    public Logger() {
        rootFolder = new File(rootFolderPath);
        if (!rootFolder.exists()) {
            rootFolder.mkdir();
        }
        
    }

    public void logInfo(HttpSession sess)throws Exception {
        String username = sess.getAttribute("userName").toString();
        File userLogFolder =
            new File(rootFolderPath + "//" + username);
        if (!userLogFolder.exists()) {
            userLogFolder.mkdir();
        }

        File userLogFile =
            new File(rootFolderPath  + "//" + username +
                     "//" + username + "_" +
                     new Date(System.currentTimeMillis()).toString().split(" ")[0] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[1] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[2]);
        
        if (!userLogFile.exists()) {
            userLogFile.createNewFile();
        }
        pw =
            new PrintWriter(new BufferedWriter(new FileWriter(userLogFile,
                                                              true)));
        
        writeln("---------------------------------------------------------------------");
        writeln("Time= "+new Date(System.currentTimeMillis()).toString());
        writeln("UserName= "+sess.getAttribute("userName").toString()+", "+"UserID= "+sess.getAttribute("userID"));
        writeln("Profile= "+sess.getAttribute("profile")+", RespID= "+sess.getAttribute("respID"));
        writeln("---------------------------------------------------------------------");
        
        
        pw.close();
        

    }
    public void logError(Exception e,String username) throws Exception{
        File userLogDir =
            new File(rootFolderPath + "//" + username);
        if (!userLogDir.exists()) {
            userLogDir.mkdir();
        }

        File userLogFile =
            new File(rootFolderPath  + "//" + username +
                     "//" + username + "_" +
                     new Date(System.currentTimeMillis()).toString().split(" ")[0] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[1] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[2]+"_error");
        
        if (!userLogFile.exists()) {
            userLogFile.createNewFile();
        }
        
        pw =
            new PrintWriter(new BufferedWriter(new FileWriter(userLogFile,
                                                              true)));
        
        writeln("");
        writeln("*********************************************************************");
        writeln("Time= "+new Date(System.currentTimeMillis()).toString());
        writeln("ErrorMessage= "+e.getMessage()+" ThrowableErrorMessage= "+e.getCause().getMessage()+" ");
        writeln("*********************************************************************");
        writeln("");
        pw.close();
        
    }
    public void logDebugStmt(String debug,String username) throws Exception{
        File userLogDir =
            new File(rootFolderPath + "//" + username);
        if (!userLogDir.exists()) {
            userLogDir.mkdir();
            System.out.println("****************************************************************");
            System.out.println(" created user log dir "+username);
            System.out.println("****************************************************************");
        }

        File userLogFile =
            new File(rootFolderPath  + "//" + username +
                     "//" + username + "_" +
                     new Date(System.currentTimeMillis()).toString().split(" ")[0] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[1] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[2]);
        
        if (!userLogFile.exists()) {
            userLogFile.createNewFile();
            System.out.println("****************************************************************");
            System.out.println(" created user log file "+username);
            System.out.println("****************************************************************");
        }
        
        pw =
            new PrintWriter(new BufferedWriter(new FileWriter(userLogFile,
                                                              true)));
        writeln(debug);
        pw.close();
    }
    
    private void writeln(String str){
        pw.append(str+"\n");
    }
    
    public void logAction(String action,String username)throws Exception{        
        File userLogDir =
            new File(rootFolderPath + "//" + username);
        

        File userLogFile =
            new File(rootFolderPath  + "//" + username +
                     "//" + username + "_" +
                     new Date(System.currentTimeMillis()).toString().split(" ")[0] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[1] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[2]);
        
        if (!userLogFile.exists()) {
            userLogFile.createNewFile();
        }
        
        pw =
            new PrintWriter(new BufferedWriter(new FileWriter(userLogFile,
                                                              true)));
        writeln("=========>   "+action+"    <==========");
        pw.close();
        
    }
    
    
    
}
