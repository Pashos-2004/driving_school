package driving_school_maven.driving_school_maven;

import java.sql.ResultSet;

import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;

public class user {
	public int user_id;
	public String name;
	public String surname;
	public String patronymic;
	public String login;
	public String role;
	public String id_FIO;
	public int group_id;
	public boolean is_deleted;
	
	public user(ResultSet resSet) {
		try {
			user_id = resSet.getInt("user_id");
			name = resSet.getString("name_");
			surname = resSet.getString("surname");
			patronymic = resSet.getString("patronymic");
			login = resSet.getString("login");
			role = resSet.getString("role_name");
			group_id = resSet.getInt("group_id");
			is_deleted = resSet.getBoolean("is_deleted");
			
			id_FIO = user_id+" "+surname+" "+name.charAt(0)+". "+patronymic.charAt(0)+".";
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.USER_LOAD_ERROR+"\n "+e.getMessage());
			System.exit(DefaultErrors.USER_LOAD_ERROR_KODE);
		}
		
	}
	
	
}
