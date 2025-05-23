package WindowsControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import driving_school_maven.driving_school_maven.user;
import DataBaseControl.postgreSQLConnection;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.group;
import driving_school_maven.driving_school_maven.main;

public class userControl {

	private static ArrayList<group> groups = new ArrayList<group>(20); 
	private static int curGroupIndexInArray = 1;
	private static JComboBox<String> groupComboBox ;
	
	private static ArrayList<user> users = new ArrayList<user>(20); 
	private static int curUserIndexInArray = 1;
	private static JComboBox<String> userComboBox ;
	
	private static JCheckBox isShowNonActiveGroup;
	
	public static JFrame GetUserComntrolFrame() {
		JFrame JF = mainWindow.GetMainJFrame();
		try {
			
			JButton exitBTN = new JButton();

			exitBTN.setBounds(10, 650, 150, 20);
			exitBTN.setText("Назад");
			exitBTN.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					main.JF.dispose();
					main.JF = mainWindow.GetAdminMainJFrame();	
					//exitBTN.setVisible(false);
				}
			});
		
			JLabel groupPickerLabel = new JLabel("Выберите группу"); 
			groupPickerLabel.setBounds(20,53,150,15);
			
			groupComboBox = GetDefaultGroupComboBox();
			userComboBox = GetDefaultUserComboBox();
			
			
			isShowNonActiveGroup = new JCheckBox("Показать неактивные группы?",false);
			//isShowNonActiveGroup.setText();
			isShowNonActiveGroup.setBounds(10,70,270,20);
			isShowNonActiveGroup.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					loadGroupComboBox(! isShowNonActiveGroup.isSelected());
					
				}
			});
			
			userComboBox = GetDefaultUserComboBox();
			groupComboBox = GetDefaultGroupComboBox();
			
			JPanel userControlJP = new JPanel();
			userControlJP.setLayout(null);
			
			userControlJP.add(exitBTN);
			userControlJP.add(userComboBox);
			userControlJP.add(groupComboBox);
			userControlJP.add(isShowNonActiveGroup);
			
			JF.add(userControlJP);
			
			JF.setVisible(true);
			loadGroupComboBox(! isShowNonActiveGroup.isSelected());
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.USER_CONTROL_FRAME_CREATION_ERROR+"\n "+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
		
		
		
		return JF;
		
	}
	
	
	public static void loadGroupComboBox(boolean isShowNonActive) {
		String query;
		if(isShowNonActive) query = "Select group_id , name_ , is_active from _group where is_active = true ORDER BY  creation_time";
		else query = "Select group_id , name_ , is_active from _group ORDER BY  creation_time";
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
			LogWriter.WriteLog(DefaultErrors.USER_CONTROL_FRAME_CREATION_ERROR+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
	}
	
	public static void loadUserComboBox(boolean isShowNonActive) {
		String query;
		
		if(isShowNonActive) query = "Select user_id, name_ ,surname, login ,patronymic ,is_deleted, group_id, _role.role_name  "
				+ "from _user join _role on _role.role_id = _user.role_id "
				+ "where is_deleted = false and group_id = "+groups.get(curGroupIndexInArray).groupId+" "
				+ "ORDER BY surname , name_ , patronymic ";
		else query = "Select user_id, name_ ,surname, login ,patronymic ,is_deleted, group_id, _role.role_name  "
				+ "from _user   join _role on _role.role_id = _user.role_id "
				+ "where group_id = "+groups.get(curGroupIndexInArray).groupId+" "
				+ "ORDER BY surname , name_ , patronymic";
		
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
			
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.USER_CONTROL_FRAME_CREATION_ERROR+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
	}
	
	public static JComboBox<String> GetDefaultGroupComboBox() {
		JComboBox<String> comboBox = new JComboBox();
		comboBox = new JComboBox<String>();
		comboBox.setBounds(10,50,200,20);
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String) groupComboBox.getSelectedItem();
				for(int i=0; i<groups.size();i++) {
					if(groups.get(i).groupName.equals(selected)) {
						curGroupIndexInArray = i;
						loadUserComboBox(! isShowNonActiveGroup.isSelected());
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
		comboBox.setBounds(220,50,220,20);
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String) userComboBox.getSelectedItem();
				for(int i=0; i<users.size();i++) {
					if(users.get(i).name.equals(selected)) {
						curGroupIndexInArray = i;
						
						break;
					}
				}
			}
		});
		return comboBox;
	}
	
}
