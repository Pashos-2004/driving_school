package WindowsControl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import DataBaseControl.authorization;
import DataBaseControl.passwordChange;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.commonData;
import driving_school_maven.driving_school_maven.main;
import driving_school_maven.driving_school_maven.userInfo;

public class userCabinet {

	public static JFrame GetUserCabinetJFrame() {
		JFrame JF = new JFrame() {};
		
		JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JF.setTitle("Driving school clinet: UserCabinet");
		
		JF.setBounds(commonData.maxScreenWidtn/2-commonData.AUTH_WINDOW_WIGTH/2,commonData.maxScreenHeigt/2-commonData.AUTH_WINDOW_HEIGHT/2
				,commonData.AUTH_WINDOW_WIGTH,commonData.AUTH_WINDOW_HEIGHT );
		JF.setMinimumSize(new Dimension(commonData.AUTH_WINDOW_WIGTH,commonData.AUTH_WINDOW_HEIGHT));
		JF.setMaximumSize(new Dimension(commonData.AUTH_WINDOW_WIGTH,commonData.AUTH_WINDOW_HEIGHT));
		
		JLabel password1Label = new JLabel();
		password1Label.setText("Новый пароль:");
		password1Label.setBounds(40, 100, 100, 15);
		
		JPasswordField passwd1Field = new JPasswordField();
		passwd1Field.setBounds(40, 115, 220, 20);
		
		JPasswordField passwd2Field = new JPasswordField();
		passwd2Field.setBounds(40, 150, 220, 20);
		
		JLabel password2Label = new JLabel();
		password2Label.setText("Повторите пароль:");
		password2Label.setBounds(40, 135, 150, 15);
		
		JButton saveBTN = new JButton();
		
		saveBTN.setBounds(90, 180, 120, 20);
		saveBTN.setText("Сохранить");
		saveBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String password1 = new String( passwd1Field.getPassword());
				String password2 = new String( passwd2Field.getPassword());
				
				if(password1.equals(password2)) {
				if(passwordChange.ChangePassword(password1)) {
					JOptionPane.showMessageDialog(main.JF, 
						"Пароль успешно сменнён", 
		                "Смена пароля", JOptionPane.INFORMATION_MESSAGE);
					LogWriter.WriteLog(commonData.USER_CHANGE_PASSWORD_BY_HIMSELF + "\nUser_ID : "+ userInfo.user_id + "\nUser_login : " + userInfo.login);
				}else JOptionPane.showMessageDialog(main.JF, "Не удалось сменить пароль, пожалуйста обратитесь к системному администратору", 
		                "Ошибка смены пароля", JOptionPane.ERROR_MESSAGE);
				}else JOptionPane.showMessageDialog(main.JF, "Не удалось сменить пароль, пароли не совпадают", 
		                "Ошибка смены пароля", JOptionPane.ERROR_MESSAGE);
			}
			
		});
		
		JButton backBTN = new JButton();
		
		backBTN.setBounds(20, 320, 80, 20);
		backBTN.setText("Назад");
		backBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.JF.dispose();
				main.JF = mainWindow.GetUserMainJFrame();
			}
		});
		
		
		JPanel userCabinetPanel = new JPanel();
		userCabinetPanel.setLayout(null);
		
		userCabinetPanel.add(password1Label);
		userCabinetPanel.add(passwd1Field);
		userCabinetPanel.add(password2Label);
		userCabinetPanel.add(passwd2Field);
		userCabinetPanel.add(saveBTN);
		userCabinetPanel.add(backBTN);
		
		JF.add(userCabinetPanel);
		
		
		JF.setVisible(true);
		
		return JF;
	}
	
}
