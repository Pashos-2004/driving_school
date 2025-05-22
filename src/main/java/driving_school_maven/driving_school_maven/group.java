package driving_school_maven.driving_school_maven;

import java.sql.ResultSet;
import java.sql.SQLException;

import MyExeptions.DefaultErrors;

public class group {
	public int groupId;	
	public String groupName;
	public boolean isActive; 
	
	public group(int groupId,String groupName, boolean isActive) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.isActive = isActive;
	}
	
	public group(ResultSet resSet) {
		try {
			this.groupId = resSet.getInt("group_id");
			this.groupName = resSet.getString("name_");
			this.isActive = resSet.getBoolean("is_active");
		} catch (SQLException e) {
			MyExeptions.LogWriter.WriteLog(MyExeptions.DefaultErrors.GROUP_ITEM_CREACTION_ERROR +"\n"+ e.getMessage() );
			System.exit(DefaultErrors.GROUP_ITEM_ERROR);
			
		}
		
		
	}
}
