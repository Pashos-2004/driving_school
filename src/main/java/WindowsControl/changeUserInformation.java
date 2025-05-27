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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import DataBaseControl.passwordHashing;
import DataBaseControl.postgreSQLConnection;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.commonData;
import driving_school_maven.driving_school_maven.group;
import driving_school_maven.driving_school_maven.main;
import driving_school_maven.driving_school_maven.user;
import driving_school_maven.driving_school_maven.userInfo;

public class changeUserInformation {

	private static JPanel changeUserInformationJPanel;
	
	private static ArrayList<group> groups = new ArrayList<group>(20); 
	private static int curGroupIndexInArray = 1;
	private static JComboBox<String> groupComboBox ;
	
	private static ArrayList<group> newGroups = new ArrayList<group>(20); 
	private static int curNewGroupIndexInArray = 1;
	private static JComboBox<String> newGroupComboBox ;
	
	private static ArrayList<user> users = new ArrayList<user>(20); 
	private static int curUserIndexInArray = 1;
	private static JComboBox<String> userComboBox ;
	
	private static user curSelectedUser;
	
	private static JTextField nameField;
	private static JTextField surnameField;
	private static JTextField patronymicField;
	private static JTextField loginField;
	private static JPasswordField passwordField1;
	private static JPasswordField passwordField2;
	private static JCheckBox isSuperUser;
	private static JLabel loginLabel;
	
	private static JButton activateDisableUserBTN;
	
	
	private static JFrame GetChangeUserInformationBase(int mode) {
		
		JFrame changeUserInformationJF = new JFrame() {};
		BufferedImage appIcon;

			try {
				
				try {
					appIcon = ImageIO.read(new File("src/pictures/icon.png"));
					changeUserInformationJF.setIconImage(appIcon);
				} catch (IOException e) {
					e.printStackTrace();
					LogWriter.WriteLog(DefaultErrors.PICTURE_LOAD_ERROR+" icon.png : \n "+e.getMessage());
				}
				
				
				changeUserInformationJF.setVisible(true);
				changeUserInformationJF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				changeUserInformationJF.setTitle("Driving school: User control");
				
				changeUserInformationJF.setBounds(commonData.maxScreenWidtn/2-commonData.USER_CHANGE_WINDOW_WIGTH/2,commonData.maxScreenHeigt/2-commonData.USER_CHANGE_WINDOW_HEIGHT/2
						,commonData.USER_CHANGE_WINDOW_WIGTH,commonData.USER_CHANGE_WINDOW_HEIGHT );
				changeUserInformationJF.setMinimumSize(new Dimension(commonData.USER_CHANGE_WINDOW_WIGTH,commonData.USER_CHANGE_WINDOW_HEIGHT));
				changeUserInformationJF.setMaximumSize(new Dimension(commonData.USER_CHANGE_WINDOW_WIGTH,commonData.USER_CHANGE_WINDOW_HEIGHT));
				
				JButton backBTN = new JButton();
				
				backBTN.setBounds(20, 370, 80, 20);
				backBTN.setText("Назад");
				backBTN.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						main.JF.dispose();
						main.JF = userControlMenu.GetUserControlMenuFrame();
					}
				});
				
				
				loginLabel = new JLabel();
				loginLabel.setText("Логин:");
				loginLabel.setBounds(20, 76, 250, 14);
				
				JLabel nameLabel = new JLabel();
				nameLabel.setText("Введите имя:");
				nameLabel.setBounds(20, 90, 250, 12);
				
				nameField = new JTextField();
				nameField.setBounds(20,103,250,20);
				
				JLabel surnameLabel = new JLabel();
				surnameLabel.setText("Введите Фамилию:");
				surnameLabel.setBounds(20, 123, 250, 12);
				
				surnameField = new JTextField();
				surnameField.setBounds(20,136,250,20);
				
				JLabel patronymicLabel = new JLabel();
				patronymicLabel.setText("Введите отчество:");
				patronymicLabel.setBounds(20, 156, 250, 12);
				
				patronymicField = new JTextField();
				patronymicField.setBounds(20,169,250,20);
				
				
				JLabel passwordLabel1 = new JLabel();
				passwordLabel1.setText("Введите пароль:");
				passwordLabel1.setBounds(20, 188, 250, 12);
				
				passwordField1 = new JPasswordField();
				passwordField1.setBounds(20,202,250,20);
				
				JLabel passwordLabel2 = new JLabel();
				passwordLabel2.setText("Повторите пароль:");
				passwordLabel2.setBounds(20, 222, 250, 12);
				
				passwordField2  = new JPasswordField();
				passwordField2.setBounds(20,235,250,20);
				
				JLabel userInfoLabel = new JLabel();
				userInfoLabel.setText("Данные выбранного пользователя:");
				userInfoLabel.setBounds(20,55,250,20);
				
				activateDisableUserBTN = new JButton();
				activateDisableUserBTN.setBounds(40,298,200,20 );
				activateDisableUserBTN.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						try {
							if(JOptionPane.showConfirmDialog(main.JF, "Вы уверены, что хотите сделать пользователя активным/неактивными", "Предупреждение",
									JOptionPane.INFORMATION_MESSAGE )!=JOptionPane.OK_OPTION) return;
							Connection connection =  postgreSQLConnection.GetConnection();
							Statement statement = connection.createStatement();
							
							statement.execute("update _user set is_deleted = "+!curSelectedUser.is_deleted+" where user_id = "+curSelectedUser.user_id);
							LogWriter.WriteLog("user State changed by : "+userInfo.login+" "+"\n for : "  +curSelectedUser.id_FIO );
							
							if(mode == 0)loadGroupComboBoxForStudentChagne();
							else loadUserComboBoxForTeacher();
						}catch (Exception e1) {
							JOptionPane.showMessageDialog(main.JF,"Ошибка изменения данных.","Ошибка",JOptionPane.ERROR_MESSAGE );
							LogWriter.WriteLog(DefaultErrors.STUDENT_INFORMATION_CHANGE_ERROR+e1.getMessage());
							
						}
						
					}
				});
				
				changeUserInformationJPanel = new JPanel();
				changeUserInformationJPanel.setLayout(null);
				
				changeUserInformationJPanel.add(userInfoLabel);
				changeUserInformationJPanel.add(backBTN);
				changeUserInformationJPanel.add(loginLabel);
				changeUserInformationJPanel.add(nameLabel);
				changeUserInformationJPanel.add(nameField);
				changeUserInformationJPanel.add(surnameLabel);
				changeUserInformationJPanel.add(surnameField);
				changeUserInformationJPanel.add(patronymicLabel);
				changeUserInformationJPanel.add(patronymicField);
				changeUserInformationJPanel.add(passwordLabel1);
				changeUserInformationJPanel.add(passwordField1);
				changeUserInformationJPanel.add(passwordLabel2);
				changeUserInformationJPanel.add(passwordField2);
				changeUserInformationJPanel.add(activateDisableUserBTN);
				
				changeUserInformationJF.add(changeUserInformationJPanel);

				changeUserInformationJPanel.add(activateDisableUserBTN);
			}catch (Exception e) {
				LogWriter.WriteLog(DefaultErrors.USER_CONTROL_FRAME_CREATION_ERROR + "\n "+e.getMessage());
				System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
			}
		

			
		
		return changeUserInformationJF;
	}
	
	public static JFrame GetChangeStudentJF() {
		
		groupComboBox = GetDefaultGroupComboBox();
		userComboBox = GetDefaultUserComboBox();
		
		JLabel newGroupLabel = new JLabel();
		newGroupLabel.setText("Групаа ученика:");
		newGroupLabel.setBounds(20, 257, 250, 12);
		
		JFrame ChangeStudentJF = GetChangeUserInformationBase(0);
		newGroupComboBox = new JComboBox<String>();
		newGroupComboBox.setBounds(20,272,240,20);
		newGroupComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String) newGroupComboBox.getSelectedItem();
				for(int i=0; i<groups.size();i++) {
					if(groups.get(i).groupName.equals(selected)) {
						curNewGroupIndexInArray = i;
						
						break;
					}
				}
			}
		});
		
		JButton saveBTN = new JButton();
		saveBTN.setText("Сохранить изменения");
		saveBTN.setBounds(40,340,200,20);
		
		saveBTN.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				try {
					if(JOptionPane.showConfirmDialog(main.JF, "Вы уверены, что хотите изменить данные пользователя", "Предупреждение",
							JOptionPane.INFORMATION_MESSAGE )!=JOptionPane.OK_OPTION) return;
					Connection connection =  postgreSQLConnection.GetConnection();
					Statement statement = connection.createStatement();
					
					String querry = "";
					
					String name = nameField.getText();
					String surname = surnameField.getText();
					String patronymic = patronymicField.getText();
					String password1 = new String(passwordField1.getPassword());
					String password2 = new String(passwordField2.getPassword());
					int group_id =  groups.get(curNewGroupIndexInArray).groupId ;
					
					if(name.equals("") || surname.equals("") || patronymic.equals("")) {
						JOptionPane.showMessageDialog(main.JF,"Ошибка изменения данных: поля должны быть заполнены","Ошибка",JOptionPane.ERROR_MESSAGE );
						return;
					}
					
					if(!name.equals(curSelectedUser.name))  querry+= "update _user set name_ ='"+name+"' where user_id="+curSelectedUser.user_id+" ;";
					if(!surname.equals(curSelectedUser.surname))  querry+= "update _user set surname ='"+surname+"' where user_id="+curSelectedUser.user_id+" ;";
					if(!patronymic.equals(curSelectedUser.patronymic))  querry+= "update _user set patronymic ='"+patronymic+"' where user_id="+curSelectedUser.user_id+" ;";
					if(group_id != curSelectedUser.group_id) querry+= "update _user set group_id = "+group_id+" where user_id="+curSelectedUser.user_id+" ;";
					
					if(! password1.equals("") || password2.equals("")) {
						if(!password1.equals(password2)) {
							JOptionPane.showMessageDialog(main.JF,"Ошибка изменения данных: пароли должны совпадать","Ошибка",JOptionPane.ERROR_MESSAGE );
							return;
						}
						querry+= "update _user set passwd ='"+passwordHashing.GetPasswordHash(password1)+"' where user_id="+curSelectedUser.user_id+" ;";	
					}
					
					//ifd();
					
					//statement.execute("update _user set is_deleted = "+!curSelectedUser.is_deleted+" where user_id = "+curSelectedUser.user_id);
					if(querry.equals("")) {
						JOptionPane.showMessageDialog(main.JF,"Нет изменений","Информация",JOptionPane.INFORMATION_MESSAGE );
						return;
					}
					statement.execute(querry);
					LogWriter.WriteLog("user data changed by : "+userInfo.login+" "+"\n for : "  +curSelectedUser.id_FIO );
					JOptionPane.showMessageDialog(main.JF,"Изменения сохранены","Информация",JOptionPane.INFORMATION_MESSAGE );
					loadGroupComboBoxForStudentChagne();
				}catch (Exception e1) {
					JOptionPane.showMessageDialog(main.JF,"Ошибка изменения данных.","Ошибка",JOptionPane.ERROR_MESSAGE );
					LogWriter.WriteLog(DefaultErrors.STUDENT_INFORMATION_CHANGE_ERROR+e1.getMessage());
					
				}
				
			}
		});
		
		changeUserInformationJPanel.add(newGroupLabel);
		changeUserInformationJPanel.add(groupComboBox);
		changeUserInformationJPanel.add(newGroupComboBox);
		changeUserInformationJPanel.add(userComboBox);
		changeUserInformationJPanel.add(saveBTN);
		
		loadGroupComboBoxForStudentChagne();
		
		return ChangeStudentJF;
	}
	
	public static JFrame GetChangeTeacherJF() {
		
		JFrame ChangeStudentJF = GetChangeUserInformationBase(1);
		
		
		userComboBox = GetDefaultUserComboBox();
		
		activateDisableUserBTN.setBounds(40,262,200,20);
		
		JButton saveBTN = new JButton();
		saveBTN.setText("Сохранить изменения");
		saveBTN.setBounds(40,340,200,20);
		
		saveBTN.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				try {
					if(JOptionPane.showConfirmDialog(main.JF, "Вы уверены, что хотите изменить данные пользователя", "Предупреждение",
							JOptionPane.INFORMATION_MESSAGE )!=JOptionPane.OK_OPTION) return;
					Connection connection =  postgreSQLConnection.GetConnection();
					Statement statement = connection.createStatement();
					
					String querry = "";
					
					String name = nameField.getText();
					String surname = surnameField.getText();
					String patronymic = patronymicField.getText();
					String password1 = new String(passwordField1.getPassword());
					String password2 = new String(passwordField2.getPassword());
					
					if(name.equals("") || surname.equals("") || patronymic.equals("")) {
						JOptionPane.showMessageDialog(main.JF,"Ошибка изменения данных: поля должны быть заполнены","Ошибка",JOptionPane.ERROR_MESSAGE );
						return;
					}
					
					if(!name.equals(curSelectedUser.name))  querry+= "update _user set name_ ='"+name+"' where user_id="+curSelectedUser.user_id+" ;";
					if(!surname.equals(curSelectedUser.surname))  querry+= "update _user set surname ='"+surname+"' where user_id="+curSelectedUser.user_id+" ;";
					if(!patronymic.equals(curSelectedUser.patronymic))  querry+= "update _user set patronymic ='"+patronymic+"' where user_id="+curSelectedUser.user_id+" ;";
					
					if(! password1.equals("") || password2.equals("")) {
						if(!password1.equals(password2)) {
							JOptionPane.showMessageDialog(main.JF,"Ошибка изменения данных: пароли должны совпадать","Ошибка",JOptionPane.ERROR_MESSAGE );
							return;
						}
						querry+= "update _user set passwd ='"+passwordHashing.GetPasswordHash(password1)+"' where user_id="+curSelectedUser.user_id+" ;";	
					}
					
					//ifd();
					
					//statement.execute("update _user set is_deleted = "+!curSelectedUser.is_deleted+" where user_id = "+curSelectedUser.user_id);
					if(querry.equals("")) {
						JOptionPane.showMessageDialog(main.JF,"Нет изменений","Информация",JOptionPane.INFORMATION_MESSAGE );
						return;
					}
					statement.execute(querry);
					LogWriter.WriteLog("user data changed by : "+userInfo.login+" "+"\n for : "  +curSelectedUser.id_FIO );
					JOptionPane.showMessageDialog(main.JF,"Изменения сохранены","Информация",JOptionPane.INFORMATION_MESSAGE );
					loadUserComboBoxForTeacher();
				}catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(main.JF,"Ошибка изменения данных.","Ошибка",JOptionPane.ERROR_MESSAGE );
					LogWriter.WriteLog(DefaultErrors.STUDENT_INFORMATION_CHANGE_ERROR+e1.getMessage());
					
				}
				
			}
		});
		
		changeUserInformationJPanel.add(userComboBox);
		changeUserInformationJPanel.add(saveBTN);
		
		loadUserComboBoxForTeacher();
		
		return ChangeStudentJF;
	}
	
	public static void loadUserInfoInField() {
		
		
		loginLabel.setText("Логин : "+curSelectedUser.login);
		nameField.setText(curSelectedUser.name);
		surnameField.setText(curSelectedUser.surname);
		patronymicField.setText(curSelectedUser.patronymic);
		if(curSelectedUser.role.equals("Ученик")) {
		
			for(int i=0; i<groups.size();i++) {
				if(groups.get(i).groupId == curSelectedUser.group_id) {
				newGroupComboBox.setSelectedIndex(i);
				curNewGroupIndexInArray=i;
				break;
				}
		}}
		if(curSelectedUser.is_deleted) activateDisableUserBTN.setText("Сделать активным");
		else activateDisableUserBTN.setText("Сделать неактивным");
	}
	
	
	public static void loadNewGroupComboBox() {
		try {
			
			newGroupComboBox.removeAllItems();
			
			for(int i=0; i<groups.size();i++) {
				newGroupComboBox.addItem((groups.get(i)).groupName );
			}
			
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.STUDENT_INFORMATION_CHANGE_ERROR+e.getMessage());
			System.exit(DefaultErrors.USER_INFORMATION_CHANGE_WINDOW_ERROR_KODE);
		}
		
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
			loadNewGroupComboBox();
			groupComboBox.removeAllItems();
			
			for(int i=0; i<groups.size();i++) {
				groupComboBox.addItem((groups.get(i)).groupName );
			}
			
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.USER_CONTROL_FRAME_CREATION_ERROR+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
	}
	
	public static void loadGroupComboBoxForStudentChagne() {
		loadGroupComboBoxBase();
		loadUserComboBoxForStudent();
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
						curSelectedUser=users.get(i);
						//if(curSelectedUser.user_id == userInfo.user_id) ;
						loadUserInfoInField();
						break;
					}
				}
			}
		});
		return comboBox;
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
			LogWriter.WriteLog(DefaultErrors.USER_CONTROL_FRAME_CREATION_ERROR+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
	}
	
	public static void loadUserComboBoxForTeacher() {
		String query;
		
		query = "Select user_id, name_ ,surname, login ,patronymic ,is_deleted, group_id, _role.role_name  "
				+ "from _user join _role on _role.role_id = _user.role_id "
				+ "where  (_role.role_name='Преподаватель' or _user.user_id = "+userInfo.user_id+") "
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
			
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.USER_CONTROL_FRAME_CREATION_ERROR+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
	}
	
}
