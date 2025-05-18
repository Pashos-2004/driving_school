package DataBaseControl;

import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;
import java.sql.Statement;

import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import WindowsControl.currentWindowInfo;

import javax.swing.*;


public class authorization {

	public static boolean authUser(String login, String passwd) {
		
		try{
		
		Connection connection = postgreSQLConnection.GetConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT passwd FROM _user where is_deleted = false and login = '" + login+"'");
		//System.out.println("SELECT passwd FROM _user where is_deleted = false and login = '" + login+"'");
		if(resultSet.next()){
			        String usrPasswd = resultSet.getString("passwd");
			        return passwordHashing.CheckPassword(passwd, usrPasswd);
		}
		
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.AUTH_ERROR+"\n"+e.getMessage());
			
		}
		
		
		
		return false;
	}
	
}
