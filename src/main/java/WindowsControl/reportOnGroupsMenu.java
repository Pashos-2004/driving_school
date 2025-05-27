package WindowsControl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import DataBaseControl.postgreSQLConnection;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.commonData;
import driving_school_maven.driving_school_maven.excelWriter;
import driving_school_maven.driving_school_maven.group;
import driving_school_maven.driving_school_maven.main;

public class reportOnGroupsMenu {
	
	
	
	private static String fileDir = "";
	
	public static JFrame GetReportOnGroupsMenuJF() {
		JFrame reportsMenuJF = new JFrame();
		BufferedImage appIcon;
		fileDir = "";
		try {
			
			try {
				appIcon = ImageIO.read(new File("src/pictures/icon.png"));
				reportsMenuJF.setIconImage(appIcon);
			} catch (IOException e) {
				e.printStackTrace();
				LogWriter.WriteLog(DefaultErrors.PICTURE_LOAD_ERROR+" icon.png : \n "+e.getMessage());
			}
			reportsMenuJF.setVisible(true);
			reportsMenuJF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			reportsMenuJF.setTitle("Driving school: Report");
			
			reportsMenuJF.setBounds(commonData.maxScreenWidtn/2-commonData.SMALL_REPORTS_WINDOW_WIGTH/2,commonData.maxScreenHeigt/2-commonData.SMALL_REPORTS_WINDOW_HEIGHT/2
					,commonData.SMALL_REPORTS_WINDOW_WIGTH,commonData.SMALL_REPORTS_WINDOW_HEIGHT );
			reportsMenuJF.setMinimumSize(new Dimension(commonData.SMALL_REPORTS_WINDOW_WIGTH,commonData.SMALL_REPORTS_WINDOW_HEIGHT));
			reportsMenuJF.setMaximumSize(new Dimension(commonData.SMALL_REPORTS_WINDOW_WIGTH,commonData.SMALL_REPORTS_WINDOW_HEIGHT));
			
			JButton backBTN = new JButton();
			
			backBTN.setBounds(15, 180, 80, 20);
			backBTN.setText("Назад");
			backBTN.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					main.JF.dispose();
					main.JF = reportsMenu.GetReportsMenuJF();
				}
			});
			
			JButton chooseButton = new JButton("Выбрать папку");
			chooseButton.setBounds(15,40,250,20);
	        JLabel statusLabel = new JLabel("Папка не выбрана");
	        statusLabel.setBounds(15,20,250,20);
	        
	        chooseButton.addActionListener(e -> {
	            JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Режим только папки
	            
	            int returnValue = fileChooser.showOpenDialog(null);
	            if (returnValue == JFileChooser.APPROVE_OPTION) {
	                File selectedFolder = fileChooser.getSelectedFile();
	                statusLabel.setText("Выбрана папка: " + selectedFolder.getAbsolutePath());
	                fileDir= selectedFolder.getAbsolutePath();
	                //System.out.println(fileDir);
	            } else {
	                statusLabel.setText("Выбор отменен");
	            }
	        });
			
			
			JButton reportOnGroups = new JButton();
			
			reportOnGroups.setBounds(15, 80, 200, 20);
			reportOnGroups.setText("Сформировать отчёт");
			reportOnGroups.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(fileDir.equals("")) {
						JOptionPane.showMessageDialog(main.JF,"Сначала выберите путь!","Ошибка",JOptionPane.ERROR_MESSAGE );
						return;
					}
					
					try {
						
						ArrayList<String> headers = new ArrayList<String>();
						headers.add("Группа");
						headers.add("% правильных ответов");
						headers.add("Средняя оценка за экзамены");
						headers.add("Количество проведённых экзаменов");
						
						
						ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
						Connection connection =  postgreSQLConnection.GetConnection();
						//Statement statement = connection.createStatement();
						ResultSet buffGroup = connection.createStatement().executeQuery("Select name_ ,group_id from _group where _group.is_active = true" );
						
						long groupId;
						String groupName="";
						
						while(buffGroup.next()) {
							ArrayList<String> row = new ArrayList<String>();
							
							groupId = buffGroup.getLong("group_id");
							groupName = buffGroup.getString("name_");
							
							row.add(groupName);
							row.add(FindPercentOfRightAnswersInGroup(groupId)+"");
							row.add(FindAVGMarkInGroup(groupId)+"");
							row.add(FindCountOFExamsInGroup(groupId)+"");
							
							data.add(row);
						}
						
						//System.out.println(data);
						excelWriter.WriteInXLXC("Отчёт по группам "+ System.currentTimeMillis() / 1000L, fileDir, "отчёт", headers, data);
						
						JOptionPane.showMessageDialog(main.JF,"Отчёт создан","Информирование",JOptionPane.INFORMATION_MESSAGE );
					}catch (Exception e1) {
						LogWriter.WriteLog(DefaultErrors.REPORTS_FRAME_CREATION_ERROR + "\n "+e1.getMessage());
						System.exit(DefaultErrors.REPORTS_WINDOW_ERROR_KODE);
					}
					
					
					
					
					
				}
			});
			
				

			JPanel reportsMenuJP = new JPanel();
			reportsMenuJP.setLayout(null);
			reportsMenuJP.add(backBTN);
			reportsMenuJP.add(reportOnGroups);
			reportsMenuJP.add(chooseButton);
			reportsMenuJP.add(statusLabel);
			
			reportsMenuJF.add(reportsMenuJP);
			
		
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.REPORTS_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.REPORTS_WINDOW_ERROR_KODE);
		}
		
		
		return reportsMenuJF;
		
	}
	
	public static double FindPercentOfRightAnswersInGroup(long group_id) {
		
		try {
			
			
			Connection connection =  postgreSQLConnection.GetConnection();
			//Statement statement = connection.createStatement();
			ResultSet resState = connection.createStatement().executeQuery("Select count(*) as rightAnsw  from answer join _user on _user.user_id=answer.user_id join _group on _group.group_id = _user.group_id"
					+ " where _group.is_active = true and answer.is_right = true and _group.group_id = "+group_id );
			resState.next();
			int rightAnsw = resState.getInt("rightAnsw");
			
			resState = connection.createStatement().executeQuery("Select count(*) as allAnsw  from answer join _user on _user.user_id=answer.user_id join _group on _group.group_id = _user.group_id"
						+ " where _group.is_active = true and _group.group_id = "+group_id );
				resState.next();
			int allAnsw = resState.getInt("allAnsw");
			
			if(allAnsw==0) return 0;
			return rightAnsw*1.0/allAnsw*100.0;
			
		}catch (Exception e1) {
			LogWriter.WriteLog(DefaultErrors.ECXEL_WRITE_ERROR + "\n "+e1.getMessage());
			//System.exit(DefaultErrors.REPORTS_WINDOW_ERROR_KODE);
		}
		
		return -1;
		
		
		
	}
	public static double FindAVGMarkInGroup(long group_id) {
		
		try {
			
			
			Connection connection =  postgreSQLConnection.GetConnection();
			//Statement statement = connection.createStatement();
			ResultSet resState = connection.createStatement().executeQuery("Select AVG(mark.mark) as AVGMark  from mark join _user on _user.user_id = mark.user_id where _user.group_id = "+group_id);
			resState.next();
			
			return resState.getDouble("AVGMark");
			
		}catch (Exception e1) {
			LogWriter.WriteLog(DefaultErrors.ECXEL_WRITE_ERROR + "\n "+e1.getMessage());
			//System.exit(DefaultErrors.REPORTS_WINDOW_ERROR_KODE);
		}
		
		return -1;
	}
		
public static double FindCountOFExamsInGroup(long group_id) {
		
		try {
			
			Connection connection =  postgreSQLConnection.GetConnection();
			//Statement statement = connection.createStatement();
			ResultSet resState = connection.createStatement().executeQuery("Select count(*) as CountOFExams  from exam where group_id = "+group_id);
			resState.next();
			
			return resState.getDouble("CountOFExams");
			
		}catch (Exception e1) {
			LogWriter.WriteLog(DefaultErrors.ECXEL_WRITE_ERROR + "\n "+e1.getMessage());
			//System.exit(DefaultErrors.REPORTS_WINDOW_ERROR_KODE);
		}
		
		return -1;
	}	
	
	
}
