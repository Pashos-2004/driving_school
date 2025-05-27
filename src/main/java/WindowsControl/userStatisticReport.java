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
import javax.swing.JFrame;
import javax.swing.JPanel;

import DataBaseControl.postgreSQLConnection;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.commonData;
import driving_school_maven.driving_school_maven.group;
import driving_school_maven.driving_school_maven.main;
import driving_school_maven.driving_school_maven.user;
import driving_school_maven.driving_school_maven.userInfo;

public class userStatisticReport {
	
	private static ArrayList<group> groups = new ArrayList<group>(20); 
	private static int curGroupIndexInArray = 1;
	private static JComboBox<String> groupComboBox ;
	
	private static ArrayList<user> users = new ArrayList<user>(20); 
	private static int curUserIndexInArray = 1;
	private static JComboBox<String> userComboBox ;
	
	public static JFrame GetUserStatisticReportsMenuJF() {
		JFrame reportsMenuJF = new JFrame();
		BufferedImage appIcon;
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
			
			
			JButton reportOnUser = new JButton();
			
			reportOnUser.setBounds(20, 120, 240, 20);
			reportOnUser.setText("Показать отёт");
			reportOnUser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					main.JF.dispose();
					main.JF = userStatistic.GetStatisticJF(users.get(curUserIndexInArray).user_id);
					
				}
			});
			
			
			
			groupComboBox = GetDefaultGroupComboBox();
			userComboBox = GetDefaultUserComboBox();
			
			loadGroupComboBoxBase();
			
			JPanel reportsMenuJP = new JPanel();
			reportsMenuJP.setLayout(null);
			reportsMenuJP.add(backBTN);
			reportsMenuJP.add(reportOnUser);
			reportsMenuJP.add(userComboBox);
			reportsMenuJP.add(groupComboBox);
			reportsMenuJP.add(backBTN);
			
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
		loadUserComboBoxForStudent();
	}
	
	public static void loadUserComboBoxForStudent() {
		String query;
		
		query = "Select user_id, name_ ,surname, login ,patronymic ,is_deleted, group_id, _role.role_name  "
				+ "from _user join _role on _role.role_id = _user.role_id "
				+ "where group_id = "+groups.get(curGroupIndexInArray).groupId+" "
				+ "ORDER BY surname , name_ , patronymic ";
		
		try {
			 
			Connection connection =  postgreSQLConnection.GetConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			users = new ArrayList<user>(20);
			while (resultSet.next()) {
				users.add(new user(resultSet));
			}
			
			userComboBox.removeAllItems();
			
			for(int i=0; i<users.size();i++) {
				userComboBox.addItem((users.get(i)).id_FIO );
			}
			
			//loadUserInfoInField();
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.REPORTS_FRAME_CREATION_ERROR+e.getMessage());
			System.exit(DefaultErrors.REPORTS_WINDOW_ERROR_KODE);
		}
		
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
						loadUserComboBoxForStudent();
						break;
					}
				}
			}
		});
		return comboBox;
	}
	
	
	public static JComboBox<String> GetDefaultUserComboBox() {
		JComboBox<String> comboBox = new JComboBox();
		comboBox = new JComboBox<String>();
		comboBox.setBounds(20,35,220,20);
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String) userComboBox.getSelectedItem();
				for(int i=0; i<users.size();i++) {
					if(users.get(i).id_FIO.equals(selected)) {
						curUserIndexInArray = i;
						
						//if(curSelectedUser.user_id == userInfo.user_id) ;
						//loadUserInfoInField();
						break;
					}
				}
			}
		});
		return comboBox;
	}
	
}
