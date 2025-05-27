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
import driving_school_maven.driving_school_maven.user;
import driving_school_maven.driving_school_maven.userInfo;

public class reportOnGroupMenu {
	private static ArrayList<group> groups = new ArrayList<group>(20); 
	private static int curGroupIndexInArray = 1;
	private static JComboBox<String> groupComboBox ;
	
	
	private static String fileDir = "";
	
	public static JFrame GetReportOnGroupMenuJF() {
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
			chooseButton.setBounds(15,80,200,20);
	        JLabel statusLabel = new JLabel("Папка не выбрана");
	        statusLabel.setBounds(15,50,250,20);
	        
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
			
			
			JButton reportOnGroup = new JButton();
			
			reportOnGroup.setBounds(15, 130, 200, 20);
			reportOnGroup.setText("Сформировать отчёт");
			reportOnGroup.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(fileDir.equals("")) {
						JOptionPane.showMessageDialog(main.JF,"Сначала выберите путь!","Ошибка",JOptionPane.ERROR_MESSAGE );
						return;
					}
					
					try {
						
						ArrayList<String> headers = new ArrayList<String>();
						headers.add("Группа");
						headers.add("ID");
						headers.add("ФИО");
						headers.add("% правильных ответов");
						headers.add("Средняя оценка за экзамены");
						headers.add("Количество пройденных экзаменов");
						headers.add("Количество неаттестаций по экзаменам");
						
						ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
						Connection connection =  postgreSQLConnection.GetConnection();
						//Statement statement = connection.createStatement();
						ResultSet buffFIO_ID = connection.createStatement().executeQuery("Select _user.name_ as user_name, _user.surname, _user.patronymic, _user.user_id, _group.name_ as group_name from _user join _group on _group.group_id = _user.group_id where _group.group_id = " +groups.get(curGroupIndexInArray).groupId);
						long userId;
						String groupName="";
						String FIO;
						while(buffFIO_ID.next()) {
							ArrayList<String> row = new ArrayList<String>();
							userId = buffFIO_ID.getLong("user_id");
							groupName = buffFIO_ID.getString("group_name");
							FIO = buffFIO_ID.getString("user_name") + " " + buffFIO_ID.getString("surname") + " " +buffFIO_ID.getString("patronymic")  ;
							//System.out.println(userId);
							
							row.add(groupName);
							row.add(userId +"");
							row.add(FIO);
							row.add((userStatistic.FindCounOfRightSolvedQuestions(userId)*1.0)/userStatistic.FindCounOfSolvedQuestions(userId)*100.0+"");
							row.add(userStatistic.FindAverageMarkForTheExam(userId)+"");
							row.add(userStatistic.FindCountOfNonPassedExams(userId)+"");
							row.add(userStatistic.FindCountOfNonPassedExams(userId)+"");
							data.add(row);
						}
						
						//System.out.println(data);
						excelWriter.WriteInXLXC("Отчёт по группе "+groupName + " "+ System.currentTimeMillis() / 1000L, fileDir, "отчёт", headers, data);
						JOptionPane.showMessageDialog(main.JF,"Отчёт создан","Информирование",JOptionPane.INFORMATION_MESSAGE );
						
					}catch (Exception e1) {
						LogWriter.WriteLog(DefaultErrors.REPORTS_FRAME_CREATION_ERROR + "\n "+e1.getMessage());
						System.exit(DefaultErrors.REPORTS_WINDOW_ERROR_KODE);
					}
					
					
					
					
					
				}
			});
			
			groupComboBox = GetDefaultGroupComboBox();
			
			
			loadGroupComboBoxBase();			

			JPanel reportsMenuJP = new JPanel();
			reportsMenuJP.setLayout(null);
			reportsMenuJP.add(backBTN);
			reportsMenuJP.add(reportOnGroup);
			
			reportsMenuJP.add(groupComboBox);
			reportsMenuJP.add(chooseButton);
			reportsMenuJP.add(statusLabel);
			
			reportsMenuJF.add(reportsMenuJP);
			
		
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.REPORTS_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.REPORTS_WINDOW_ERROR_KODE);
		}
		
		
		return reportsMenuJF;
		
	}
	public static void loadGroupComboBoxBase() {
		String query;
		query = "Select group_id , name_ , is_active from _group where is_active = true ORDER BY  creation_time";
		
		try {
			Connection connection =  postgreSQLConnection.GetConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			groups = new ArrayList<group>(20);
			while (resultSet.next()) {
				groups.add(new group(resultSet));
			}
			
			groupComboBox.removeAllItems();
			
			for(int i=0; i<groups.size();i++) {
				groupComboBox.addItem((groups.get(i)).groupName );
			}
			
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.REPORTS_FRAME_CREATION_ERROR+e.getMessage());
			System.exit(DefaultErrors.REPORTS_WINDOW_ERROR_KODE);
		}
		
	}
	
	public static void loadGroupComboBoxForStudentChagne() {
		loadGroupComboBoxBase();
		
	}
	
	
	public static JComboBox<String> GetDefaultGroupComboBox() {
		JComboBox<String> comboBox = new JComboBox();
		comboBox = new JComboBox<String>();
		comboBox.setBounds(20,10,250,20);
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String) groupComboBox.getSelectedItem();
				for(int i=0; i<groups.size();i++) {
					if(groups.get(i).groupName.equals(selected)) {
						curGroupIndexInArray = i;
						
						break;
					}
				}
			}
		});
		return comboBox;
	}
	
	
}
