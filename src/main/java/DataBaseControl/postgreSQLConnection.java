package DataBaseControl;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import MyExeptions.*;
import WindowsControl.currentWindowInfo;
import driving_school_maven.driving_school_maven.commonData;

public class postgreSQLConnection {
	
	protected static String URL = "jdbc:postgresql://192.168.0.21:5432/DRIVING_SCHOOL" ; 
	private static String databaseUser = "postgres";
	private static String databaseUserPasswd = "1111";
	private static Connection connection = null;
	
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
		
		LogWriter.WriteLog("Successful connection to the database");
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


