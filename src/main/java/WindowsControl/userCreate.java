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
import driving_school_maven.driving_school_maven.userInfo;

public class userCreate {
	
	private static ArrayList<group> groups = new ArrayList<group>(20); 
	private static int curGroupIndexInArray = 1;
	private static JComboBox<String> groupComboBox ;
	
	private static JTextField nameField;
	private static JTextField surnameField;
	private static JTextField patronymicField;
	private static JTextField loginField;
	private static JPasswordField passwordField1;
	private static JPasswordField passwordField2;
	
	
	private static JPanel userControlMenuJPanel ;
	
	public static JFrame GetBaseUserCreationFrame() {
		JFrame userCreateJF = new JFrame() {};
		BufferedImage appIcon;

		try {
			
			try {
				appIcon = ImageIO.read(new File("src/pictures/icon.png"));
				userCreateJF.setIconImage(appIcon);
			} catch (IOException e) {
				e.printStackTrace();
				LogWriter.WriteLog(DefaultErrors.PICTURE_LOAD_ERROR+" icon.png : \n "+e.getMessage());
			}
			
			
			userCreateJF.setVisible(true);
			userCreateJF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			userCreateJF.setTitle("Driving school: User control menu");
			
			userCreateJF.setBounds(commonData.maxScreenWidtn/2-commonData.USER_CREATRE_WINDOW_WIGTH/2,commonData.maxScreenHeigt/2-commonData.USER_CREATRE_WINDOW_HEIGHT/2
					,commonData.USER_CREATRE_WINDOW_WIGTH,commonData.USER_CREATRE_WINDOW_HEIGHT );
			userCreateJF.setMinimumSize(new Dimension(commonData.USER_CREATRE_WINDOW_WIGTH,commonData.USER_CREATRE_WINDOW_HEIGHT));
			userCreateJF.setMaximumSize(new Dimension(commonData.USER_CREATRE_WINDOW_WIGTH,commonData.USER_CREATRE_WINDOW_HEIGHT));
			
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
			
			JLabel nameLabel = new JLabel();
			nameLabel.setText("Введите имя:");
			nameLabel.setBounds(20, 10, 250, 12);
			
			nameField = new JTextField();
			nameField.setBounds(20,23,250,20);
			
			JLabel surnameLabel = new JLabel();
			surnameLabel.setText("Введите Фамилию:");
			surnameLabel.setBounds(20, 43, 250, 12);
			
			surnameField = new JTextField();
			surnameField.setBounds(20,56,250,20);
			
			JLabel patronymicLabel = new JLabel();
			patronymicLabel.setText("Введите отчество:");
			patronymicLabel.setBounds(20, 76, 250, 12);
			
			patronymicField = new JTextField();
			patronymicField.setBounds(20,89,250,20);
			
			JLabel loginLabel = new JLabel();
			loginLabel.setText("Введите логин:");
			loginLabel.setBounds(20, 109, 250, 12);
			
			loginField = new JTextField();
			loginField.setBounds(20,122,250,20);
			
			JLabel passwordLabel1 = new JLabel();
			passwordLabel1.setText("Введите пароль:");
			passwordLabel1.setBounds(20, 142, 250, 12);
			
			passwordField1 = new JPasswordField();
			passwordField1.setBounds(20,155,250,20);
			
			JLabel passwordLabel2 = new JLabel();
			passwordLabel2.setText("Повторите пароль:");
			passwordLabel2.setBounds(20, 175, 250, 12);
			
			passwordField2  = new JPasswordField();
			passwordField2.setBounds(20,188,250,20);
			
			userControlMenuJPanel = new JPanel();
			userControlMenuJPanel.setLayout(null);
			
			userControlMenuJPanel.add(backBTN);
			userControlMenuJPanel.add(nameLabel);
			userControlMenuJPanel.add(nameField);
			userControlMenuJPanel.add(surnameLabel);
			userControlMenuJPanel.add(surnameField);
			userControlMenuJPanel.add(patronymicLabel);
			userControlMenuJPanel.add(patronymicField);
			userControlMenuJPanel.add(loginLabel);
			userControlMenuJPanel.add(loginField);
			userControlMenuJPanel.add(passwordLabel1);
			userControlMenuJPanel.add(passwordField1);
			userControlMenuJPanel.add(passwordLabel2);
			userControlMenuJPanel.add(passwordField2);
			
			userCreateJF.add(userControlMenuJPanel);
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.USER_CONTROL_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
		
		
		return userCreateJF;
	}
	
	
	public static JFrame GetStudentCreationFrame() {
		
		JFrame studentCreateJF = GetBaseUserCreationFrame();
		
		
		try {
			
			groupComboBox=GetDefaultGroupComboBox();
			loadGroupComboBox();
			
			JButton addUserBTN = new JButton();
			addUserBTN.setText("Добавить");
			addUserBTN.setBounds(40,250,200,20);
			addUserBTN.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(nameField.getText().equals("") || surnameField.getText().equals("") || patronymicField.getText().equals("") || 
							loginField.getText().equals("") || new String(passwordField1.getPassword()).equals("") || new String(passwordField2.getPassword()).equals("")) {
						JOptionPane.showMessageDialog(main.JF, "Пользователь не может быть создан: заполните все поля", 
				                "Ошибка создания", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if(passwordField1.getPassword().length<5 ) {
						JOptionPane.showMessageDialog(main.JF, "Пользователь не может быть создан: пароль слишком короткий", 
				                "Ошибка создания", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(! new String (passwordField1.getPassword()).equals(new String(passwordField2.getPassword()))) {
						JOptionPane.showMessageDialog(main.JF, "Пользователь не может быть создан: пароли не совпадают", 
				                "Ошибка создания", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					try {
						Connection connection =  postgreSQLConnection.GetConnection();
						Statement statement = connection.createStatement();
						
						ResultSet resSet = statement.executeQuery("select * from _user  where login = '" + loginField.getText()+"'");
						if(resSet.next()) {
							JOptionPane.showMessageDialog(main.JF, "Пользователь не может быть создан: логин уже занят", 
					                "Ошибка создания", JOptionPane.ERROR_MESSAGE);
						}
						
						statement.execute("insert into _user (name_,surname,patronymic, login , passwd, role_id , group_id, is_deleted )"
								+ "values ('"+nameField.getText()+"' , '"+surnameField.getText()+"' , '"
										+patronymicField.getText()+"' , '"+loginField.getText()+"' , '"+passwordHashing.GetPasswordHash(new String(passwordField1.getPassword()))+"' , "
										+"3"+ " , "+ groups.get(curGroupIndexInArray).groupId +" , false" 
								+") ");
						JOptionPane.showMessageDialog(main.JF, "Пользователь успешно добавлен", 
				                "Cоздания пользователя", JOptionPane.INFORMATION_MESSAGE);
						
						
					}catch (Exception e1) {
						JOptionPane.showMessageDialog(main.JF, "Пользователь не может быть создан: ошибка обращения к БД", 
				                "Ошибка создания", JOptionPane.ERROR_MESSAGE);
						LogWriter.WriteLog(DefaultErrors.STUDENT_CREATION_ERROR + "\n "+e1.getMessage());
					}
					
					
				}
			});
			
			
			userControlMenuJPanel.add(addUserBTN);
			userControlMenuJPanel.add(groupComboBox);
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.USER_CONTROL_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
		
		
		return studentCreateJF;
	}
	
	
	public static void loadGroupComboBox( ) {
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
			LogWriter.WriteLog(DefaultErrors.USER_CONTROL_FRAME_CREATION_ERROR+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
	}
	
	public static JComboBox<String> GetDefaultGroupComboBox() {
		JComboBox<String> comboBox = new JComboBox();
		comboBox = new JComboBox<String>();
		comboBox.setBounds(20,218,250,20);
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
