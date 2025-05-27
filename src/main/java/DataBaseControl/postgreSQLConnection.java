package DataBaseControl;


import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

import MyExeptions.*;
import WindowsControl.currentWindowInfo;
import driving_school_maven.driving_school_maven.commonData;
import driving_school_maven.driving_school_maven.main;

public class postgreSQLConnection {
	
	protected static String URL = "jdbc:postgresql://192.168.0.21:5432/DRIVING_SCHOOL" ; 
	private static String databaseUser = "postgres";
	private static String databaseUserPasswd = "1111";
	private static Connection connection = null;
	
	public static void loadConfigFromFile(String filename) {
		try (FileInputStream fis = new FileInputStream(filename)) {
            Properties props = new Properties();
            props.load(fis);

            StringBuilder sb = new StringBuilder();
            
            URL = CreateNewURLForDatabase(AESUtil.decrypt(props.getProperty("address")), AESUtil.decrypt(props.getProperty("port")), "DRIVING_SCHOOL");
            
            databaseUser = AESUtil.decrypt(props.getProperty("username"));
            databaseUserPasswd = AESUtil.decrypt(props.getProperty("password"));

            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(main.JF, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            LogWriter.WriteLog(DefaultErrors.DB_CONNECTION_CREATION_ERROR +"\n" + e.getMessage());
            System.exit(DefaultErrors.DB_ERROR_KODE);
        }
    }
	
	
	
	public static Connection GetConnection() {
		if ( connection==null ) CreateNewDBConnection();
		return connection;
	}
	
	public static Connection CreateNewDBConnection() {
		try{
		Class.forName("org.postgresql.Driver");

			
		connection = DriverManager.getConnection(URL, databaseUser, databaseUserPasswd);
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.DB_CONNECTION_CREATION_ERROR +"\n" + e.getMessage());
			JOptionPane.showMessageDialog(currentWindowInfo.GetCurFrame(), "Не удалось подключиться к базе данных, обратитесь к системному администратору", 
	                "Ошибка БД", JOptionPane.ERROR_MESSAGE);
			System.exit(DefaultErrors.DB_ERROR_KODE);
		}
		
		LogWriter.WriteLog(commonData.DB_CONNECTION_SUCCESS_LOG);
		return connection;
	}
	
	public static void SetURL(String newURL) {
		URL = newURL;
		URL = newURL;	
	}
	
	public static String GetURL() {
		return URL;
	}
	
	public static void SetDatabaseUser(String newDatabaseUser) {
		databaseUser = newDatabaseUser;	
	}
	
	public static String GetDatabaseUser() {
		return databaseUser;
	}
	
	public static void SetDatabaseUserPasswd(String newDatabaseUserPasswd) {
		databaseUserPasswd = newDatabaseUserPasswd;	
	}
	
	public static String GetDatabaseUserPasswd() {
		return databaseUserPasswd;
	}
	
	public static String CreateNewURLForDatabase(String address, String Port, String databaseName) {
		return "jdbc:postgresql://"+address+":"+ Port +"/"+databaseName;
	}
	
	
	
}


