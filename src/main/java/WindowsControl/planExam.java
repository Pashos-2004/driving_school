package WindowsControl;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.jdesktop.swingx.JXDatePicker;

import DataBaseControl.postgreSQLConnection;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.commonData;
import driving_school_maven.driving_school_maven.exam;
import driving_school_maven.driving_school_maven.group;
import driving_school_maven.driving_school_maven.main;
import driving_school_maven.driving_school_maven.userInfo;

public class planExam {
	
	private static ArrayList<group> groups = new ArrayList<group>(20); 
	private static int curGroupIndexInArray = 1;
	private static JComboBox<String> groupComboBox ;
	
	private static ArrayList<exam> exams = new ArrayList<exam>(20); 
	private static int curExamIndexInArray = 1;
	private static JComboBox<String> examComboBox ;
	
	private static JXDatePicker datePicker;
	//private static JPanel curJP;
	public static JFrame GetPlanExamFrame() {
		JFrame JF = new JFrame() {};
		
		BufferedImage appIcon;
		JF.setTitle("Driving school clinet");
		try {
		
			try {
				appIcon = ImageIO.read(new File("src/pictures/icon.png"));
				JF.setIconImage(appIcon);
			} catch (IOException e) {
				//e.printStackTrace();
				LogWriter.WriteLog(DefaultErrors.PICTURE_LOAD_ERROR+" icon.png : \n "+e.getMessage());
			}
		
        JF.setVisible(true);
		JF.setBounds(commonData.maxScreenWidtn/2-commonData.PLAN_EXAM_WINDOW_WIGTH/2,commonData.maxScreenHeigt/2-commonData.PLAN_EXAM_WINDOW_HEIGHT/2
				,commonData.PLAN_EXAM_WINDOW_WIGTH,commonData.PLAN_EXAM_WINDOW_HEIGHT );
		
		JButton exitBTN = new JButton();

		exitBTN.setBounds(10, 330, 80, 20);
		exitBTN.setText("Назад");
		exitBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				main.JF.dispose();
				main.JF = mainWindow.GetAdminMainJFrame();	
				//exitBTN.setVisible(false);
			}
		});
		JLabel datePickerLabel = new JLabel("Выберите дату экзамена"); 
		datePickerLabel.setBounds(20,10,150,15);
		
		Date today = new Date();
		datePicker = new JXDatePicker();
        datePicker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
        datePicker.getMonthView().setLowerBound(today);
        datePicker.setBounds(20,26,150,25);
        
		JLabel groupPickerLabel = new JLabel("Выберите группу"); 
		groupPickerLabel.setBounds(20,53,150,15);
		
		groupComboBox = GetDefaultGroupComboBox();
		examComboBox = GetDefaultExamComboBox();
		
		JButton planExamBTN = new JButton();
		planExamBTN.setText("Запланировать");
		planExamBTN.setBounds(20,100 ,150 ,20 );
		planExamBTN.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(datePicker.getDate() == null) {
					JOptionPane.showMessageDialog(main.JF, "Не удалось запланировать экзамен, заполните все поля", 
			                "Ошибка планирования", JOptionPane.ERROR_MESSAGE);
					return;}
				
				
				try {
					Connection connection =  postgreSQLConnection.GetConnection();
					Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery("Select * from exam where plan_date = '"+new SimpleDateFormat("dd.MM.yyyy").format(datePicker.getDate())+"'"
							+ " and group_id = "+ groups.get(curGroupIndexInArray).groupId);
					
					if(resultSet.next()) {
						JOptionPane.showMessageDialog(main.JF, "Не удалось запланировать экзамен, так как уже есть запланировнный экзамен для этой группы на выбранный день", 
				                "Ошибка планирования", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					statement.execute("insert into exam (group_id, plan_date) "
							+ "values ("+groups.get(curGroupIndexInArray).groupId+", '"+new SimpleDateFormat("dd.MM.yyyy").format(datePicker.getDate()) +"' )");
					JOptionPane.showMessageDialog(main.JF, "Экзамен запланирован !", 
			                "Планирование", JOptionPane.INFORMATION_MESSAGE);
					LogWriter.WriteLog("Exam planed by : "+userInfo.login+" "+"\n for : "  +groups.get(curGroupIndexInArray).groupName);
				}catch (Exception e1) {
					JOptionPane.showMessageDialog(main.JF, "Не удалось запланировать экзамен", 
			                "Ошибка планирования", JOptionPane.ERROR_MESSAGE);
					LogWriter.WriteLog(DefaultErrors.PLAN_EXAM_ERROR+"\n "+e1.getMessage());
					
				}
				loadExamComboBox();
			}
		});
		
		
		JButton deleteExamBTN = new JButton();
		deleteExamBTN.setBounds(20,178,220,20);
		deleteExamBTN.setText("Удалить экзамен");
		deleteExamBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(JOptionPane.showConfirmDialog(main.JF, "Вы уверены, что хотите удалить экзамен?", "Предупреждение",
							JOptionPane.INFORMATION_MESSAGE )!=JOptionPane.OK_OPTION) return;
					
					Connection connection =  postgreSQLConnection.GetConnection();
					Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery("Select * from mark where exam_id = "+exams.get(curExamIndexInArray).examId);
					
					if(resultSet.next()) {
						JOptionPane.showMessageDialog(main.JF, "Не удалось удалить экзамен, так как он уже был проведён", 
				                "Ошибка планирования", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					statement.execute("delete from exam where exam_id = " + exams.get(curExamIndexInArray).examId);
					JOptionPane.showMessageDialog(main.JF, "Экзамен удалён !", 
			                "Планирование", JOptionPane.INFORMATION_MESSAGE);
					LogWriter.WriteLog("Exam deleted by : "+userInfo.login+" "+"\n for : "  +exams.get(curExamIndexInArray).group_name + " date : " + exams.get(curExamIndexInArray).plan_date);
					loadExamComboBox();
				}catch (Exception e1) {
					JOptionPane.showMessageDialog(main.JF, "Не удалось удалить экзамен", 
			                "Ошибка планирования", JOptionPane.ERROR_MESSAGE);
					LogWriter.WriteLog(DefaultErrors.PLAN_EXAM_ERROR+"\n "+e1.getMessage());
					
				}
				loadExamComboBox();
				
				
				
			}
		});
		
        JPanel planExamPanel = new JPanel();
		planExamPanel.setLayout(null);
		
		planExamPanel.add(planExamBTN);
		planExamPanel.add(groupComboBox);
		planExamPanel.add(groupPickerLabel);
		planExamPanel.add(datePickerLabel);
		planExamPanel.add(exitBTN);
		planExamPanel.add(datePicker);
		planExamPanel.add(examComboBox);
		planExamPanel.add(deleteExamBTN);
		
		JF.add(planExamPanel);
		
		loadComboBox();
		loadExamComboBox();
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.EXAM_PLAN_FRAME_CREATION_ERROR+e.getMessage());
			System.exit(DefaultErrors.GROUP_CONTROL_WINDOW_ERROR_KODE);
		}
		
		
		
		
		return JF;
	}
	
	public static void loadComboBox() {
		String query = "Select group_id , name_ , is_active from _group where is_active = true ORDER BY  creation_time";
		
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
			LogWriter.WriteLog(DefaultErrors.GROUP_CONTROL_FRAME_CREATION_ERROR+e.getMessage());
			System.exit(DefaultErrors.GROUP_CONTROL_WINDOW_ERROR_KODE);
		}
		
	}
	
	public static JComboBox<String> GetDefaultGroupComboBox() {
		JComboBox<String> comboBox = new JComboBox();
		comboBox = new JComboBox<String>();
		comboBox.setBounds(20,72,220,20);
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
	
	public static void loadExamComboBox() {
		String query = "Select _group.name_, exam.exam_id, plan_date  from exam join _group on _group.group_id=exam.group_id "
				+ "where plan_date >= CURRENT_DATE  ORDER BY  plan_date";
		
		try {
			 
			Connection connection =  postgreSQLConnection.GetConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			exams = new ArrayList<exam>(20);
			while (resultSet.next()) {
				exams.add(new exam(resultSet));
			}
			examComboBox.removeAllItems();
			for(int i=0; i<exams.size();i++) {
				examComboBox.addItem((exams.get(i)).outputData );
			}
			
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.GROUP_CONTROL_FRAME_CREATION_ERROR+e.getMessage());
			System.exit(DefaultErrors.GROUP_CONTROL_WINDOW_ERROR_KODE);
		}
		
	}
	
	public static JComboBox<String> GetDefaultExamComboBox() {
		JComboBox<String> comboBox = new JComboBox();
		comboBox = new JComboBox<String>();
		comboBox.setBounds(20,150,220,20);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String) examComboBox.getSelectedItem();
				for(int i=0; i<exams.size();i++) {
					if(exams.get(i).outputData.equals(selected)) {
						curExamIndexInArray = i;
						break;
					}
				}
			}
		});
		return comboBox;
	}
	
}
