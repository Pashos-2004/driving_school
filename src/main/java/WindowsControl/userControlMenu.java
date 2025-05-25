package WindowsControl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.commonData;
import driving_school_maven.driving_school_maven.main;
import driving_school_maven.driving_school_maven.userInfo;

public class userControlMenu {

	public static JFrame GetUserControlMenuFrame() {
		
		JFrame userControlJF = new JFrame() {};
		BufferedImage appIcon;
		
		try {
			
			try {
				appIcon = ImageIO.read(new File("src/pictures/icon.png"));
				userControlJF.setIconImage(appIcon);
			} catch (IOException e) {
				e.printStackTrace();
				LogWriter.WriteLog(DefaultErrors.PICTURE_LOAD_ERROR+" icon.png : \n "+e.getMessage());
			}
			
			userControlJF.setVisible(true);
			userControlJF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			userControlJF.setTitle("Driving school: User control menu");
			
			userControlJF.setBounds(commonData.maxScreenWidtn/2-commonData.USER_CONTROL_MENU_WINDOW_WIGTH/2,commonData.maxScreenHeigt/2-commonData.AUTH_WINDOW_HEIGHT/2
					,commonData.USER_CONTROL_MENU_WINDOW_WIGTH,commonData.USER_CONTROL_MENU_WINDOW_HEIGHT );
			userControlJF.setMinimumSize(new Dimension(commonData.USER_CONTROL_MENU_WINDOW_WIGTH,commonData.USER_CONTROL_MENU_WINDOW_HEIGHT));
			userControlJF.setMaximumSize(new Dimension(commonData.USER_CONTROL_MENU_WINDOW_WIGTH,commonData.USER_CONTROL_MENU_WINDOW_HEIGHT));
			
			JButton backBTN = new JButton();
			
			backBTN.setBounds(20, 320, 80, 20);
			backBTN.setText("Назад");
			backBTN.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					main.JF.dispose();
					main.JF = mainWindow.GetAdminMainJFrame();
				}
			});
			
			JButton createTeacher = new JButton();
			
			createTeacher.setBounds(20, 30, 240, 20);
			createTeacher.setText("Добавить учителя");
			createTeacher.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					
					
				}
			});
			if(!userInfo.role.equals("Суперпользователь")) createTeacher.setVisible(false);
			
			
			JButton createStudent = new JButton();
			
			createStudent.setBounds(20, 60, 240, 20);
			createStudent.setText("Добавить ученика");
			createStudent.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					main.JF.dispose();
					main.JF = userCreate.GetStudentCreationFrame();
					
					
				}
			});
			
			
			JButton changeStudentInformation = new JButton();
			
			changeStudentInformation.setBounds(20, 90, 240, 20);
			changeStudentInformation.setText("Изменить данные ученика");
			changeStudentInformation.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					
					
				}
			});
			
			JButton changeTeacherInformation = new JButton();
			
			changeTeacherInformation.setBounds(20, 120, 240, 20);
			changeTeacherInformation.setText("Изменить данные учителя");
			changeTeacherInformation.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					
					
				}
			});
			
			JPanel userControlMenuJPanel = new JPanel();
			userControlMenuJPanel.setLayout(null);
			
			userControlMenuJPanel.add(backBTN);
			userControlMenuJPanel.add(createStudent);
			userControlMenuJPanel.add(createTeacher);
			userControlMenuJPanel.add(changeTeacherInformation);
			userControlMenuJPanel.add(changeStudentInformation);
			
			userControlJF.add(userControlMenuJPanel);
			
		}catch (Exception e) {
			
		}
		
		return userControlJF;
	}
	
	
	
	
	
	
}
