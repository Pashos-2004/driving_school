package driving_school_maven.driving_school_maven;

import java.sql.ResultSet;
import java.sql.SQLException;

import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;

public class exam {
	public int examId;
	public String plan_date; 
	public String group_name;
	public String outputData;
	
	public exam (ResultSet resSet) {
		try {
			examId=resSet.getInt("exam_id");
			plan_date = resSet.getString("plan_date");
			group_name = resSet.getString("name_");
			outputData = examId + ". " + group_name + " "+ plan_date;
			
		} catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.EXAM_LOAD_ERROR+"\n "+e.getMessage());
			System.exit(DefaultErrors.USER_LOAD_ERROR_KODE);
		}
		
	}
	
	
}
