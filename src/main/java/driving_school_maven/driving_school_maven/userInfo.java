package driving_school_maven.driving_school_maven;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import DataBaseControl.postgreSQLConnection;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;

public class userInfo {

	public static long user_id;
	public static String login;
	public static String role;
	public static long group_id;
	public static String group;

	
	public static void LoadUserFromDB(String user_login) {
		login=user_login;
		try {
		Connection connection = postgreSQLConnection.GetConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select user_id, _role.role_name, _group.group_id , _group.name_ from _user FULL join _group on _group.group_id = _user.group_id "
				+ "FULL  join _role on _role.role_id = _user.role_id where _user.login = '" +user_login +"' ;");
		resultSet.next();
		
		user_id = resultSet.getInt("user_id");
		role = resultSet.getString("role_name");
		group_id = resultSet.getInt("group_id");
		group = resultSet.getString("name_");
		
		
		System.out.println(user_id);
		System.out.println(role);
		System.out.println(group_id);
		System.out.println(group);
		
		}
		
		catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.USER_LOAD_ERROR+"\n"+e.getMessage());
			System.exit(DefaultErrors.AUTH_WINDOW_ERROR_KODE);
		}
		
	}
	
	public static void ClearUser() {
		user_id = -1;
		role = null;
		group_id = -1;
		group = null;
	}
	
}
