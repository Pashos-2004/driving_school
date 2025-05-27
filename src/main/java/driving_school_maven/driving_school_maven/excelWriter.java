package driving_school_maven.driving_school_maven;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;


import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;

public class excelWriter {

	public static void WriteInXLXC(String filename, String fileDir, String sheetName, ArrayList<String> headers, ArrayList<ArrayList<String>> data) {
		
		try{
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(sheetName);
			
	        Row headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.size(); i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers.get(i));
	        }
	        
	        for (int i = 0; i < data.size(); i++) {
	            Row row = sheet.createRow(i + 1);
	            for (int j = 0; j < data.get(i).size(); j++) {
	                Cell cell = row.createCell(j);
	                cell.setCellValue(data.get(i).get(j));
	            }
	        }
	        for (int i = 0; i < headers.size(); i++) {
	            sheet.autoSizeColumn(i);
	        }
			
	        try (FileOutputStream fos = new FileOutputStream(fileDir+"\\"+filename+".xlsx")) {
	            workbook.write(fos);
	            
	        } catch (IOException e) {
	        	e.printStackTrace();
	        	LogWriter.WriteLog(DefaultErrors.ECXEL_WRITE_ERROR + "\n" + e.getMessage());
				JOptionPane.showMessageDialog(main.JF,"Ошибка записи в .xlsx","Ошибка",JOptionPane.ERROR_MESSAGE );
	        } finally {
	          workbook.close();
	        }
	        
	        
		}catch (Exception e) {
			e.printStackTrace();
			LogWriter.WriteLog(DefaultErrors.ECXEL_WRITE_ERROR + "\n" + e.getMessage());
			JOptionPane.showMessageDialog(main.JF,"Ошибка записи в .xlsx","Ошибка",JOptionPane.ERROR_MESSAGE );
			
		}
		
		
	}

	
	
}
