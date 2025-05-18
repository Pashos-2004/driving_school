package MyExeptions;

import java.io.FileWriter;

public class LogWriter {
	
	public static void WriteLog(String log) {
	try(FileWriter writer = new FileWriter("AvtoSchool.log", true))
    {
		writer.append(java.time.LocalDateTime.now().toString());
		writer.append('\n');
        writer.append(log);
        writer.append('\n');
        
         
        writer.flush();
        writer.close();
    }
    catch (Exception e) {
    	
	}
	}
	
}
