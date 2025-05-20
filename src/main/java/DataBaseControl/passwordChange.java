package DataBaseControl;

import java.sql.Connection;
import java.sql.Statement;

import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.userInfo;

public class passwordChange {

	public static boolean ChangePassword(String password) {
		try {
			
			
		Connection connection = postgreSQLConnection.GetConnection();
		Statement statement = connection.createStatement();
		
		password = passwordHashing.GetPasswordHash(password);
		
		statement.execute("update _user set passwd =  '" + password+"'" +" where user_id = " + userInfo.user_id);
		return true;
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.USER_PASWORD_CHANGE_ERROR+"\n"+e.getMessage());
			return false;
		}
		
	}
	
	
}
