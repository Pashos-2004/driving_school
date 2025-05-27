package WindowsControl;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import DataBaseControl.authorization;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.commonFunctions;
import driving_school_maven.driving_school_maven.main;
import driving_school_maven.driving_school_maven.screenSettings;
import driving_school_maven.driving_school_maven.userInfo;

public class mainWindow {
	protected static JPanel curPanel = new JPanel();
	
	public static JFrame GetMainJFrame() {
		JFrame JF = new JFrame() {};
		BufferedImage appIcon;
		try {
			JF = screenSettings.SetScreenSizeForMainWindow(JF);
			
			
			JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JF.setTitle("Driving school clinet");
			JF.addComponentListener(commonFunctions.createResizeAdapterForDefWindows(JF));
		
		try {
			appIcon = ImageIO.read(new File("src/pictures/icon.png"));
			JF.setIconImage(appIcon);
		} catch (IOException e) {
			e.printStackTrace();
			LogWriter.WriteLog(DefaultErrors.PICTURE_LOAD_ERROR+" icon.png : \n "+e.getMessage());
		}
		
		} catch (Exception e) {
			System.exit(DefaultErrors.MAIN_WINDOW_ERROR_KODE);
			
			
		}
		
		
			return JF;
		
	}
	
	public static JFrame GetUserMainJFrame() {
		
		//System.out.println("sasda");
		JFrame JF = mainWindow.GetMainJFrame();
		
		JButton exitBTN = new JButton();
		
		exitBTN.setBounds(1180, 0, 100, 20);
		exitBTN.setText("Выход");
		exitBTN.setBackground(new Color(230, 1, 1));
		exitBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userInfo.ClearUser();
				main.JF.dispose();
				main.JF = authWindow.GetAuthWindow();
				currentWindowInfo.SetCurFrame(main.JF);
			}
		});
		
		JButton workoutBTN = new JButton();
		workoutBTN.setBounds(465, 100, 350, 20);
		workoutBTN.setText("Тренировка");
		workoutBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.JF.dispose();
				main.JF = questionWindows.GetWorkoutJFrame();
			}
		});
		JButton examBTN = new JButton();
		examBTN.setBounds(465, 140, 350, 20);
		examBTN.setText("Экзамен");
		examBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.JF.dispose();
				main.JF = questionWindows.GetExamJFrame();
			}
		});
		
		JButton userStatisticBTN = new JButton();
		userStatisticBTN.setBounds(465, 180, 350, 20);
		userStatisticBTN.setText("Посмотреть стистику");
		userStatisticBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.JF.dispose();
				main.JF = userStatistic.GetStatisticJF(userInfo.user_id);
			}
		});
		
		JButton userCabinetBTN = new JButton();
		userCabinetBTN.setBounds(465, 220, 350, 20);
		userCabinetBTN.setText("Личный кабинет");
		userCabinetBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 
				main.JF.dispose();
				main.JF = userCabinet.GetUserCabinetJFrame();
				
			}
		});
		
		JPanel userMainPanel = new JPanel();
		userMainPanel.setLayout(null);
		
		userMainPanel.add(exitBTN);
		userMainPanel.add(userCabinetBTN);
		
		userMainPanel.add(userStatisticBTN);
		userMainPanel.add(workoutBTN);
		userMainPanel.add(examBTN);
		
		JF.add(userMainPanel);
		curPanel = userMainPanel;
		
		JF.setVisible(true);
		
		return JF;
	}
	
	public static JFrame GetAdminMainJFrame() {
		JFrame JF=mainWindow.GetMainJFrame();
		//System.out.println("gsaGHERDSF");
		
		JButton exitBTN = new JButton();
		
		exitBTN.setBounds(1080, 0, 200, 20);
		exitBTN.setText("Выход");
		exitBTN.setBackground(new Color(223, 21, 21));
		exitBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				userInfo.ClearUser();
				main.JF.dispose();
				main.JF = authWindow.GetAuthWindow();
				currentWindowInfo.SetCurFrame(main.JF);
			}
		});
		
		JButton userCabinetBTN = new JButton();
		userCabinetBTN.setBounds(465, 220, 350, 20);
		userCabinetBTN.setText("Личный кабинет");
		userCabinetBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 
				main.JF.dispose();
				main.JF = userCabinet.GetUserCabinetJFrame();
				
			}
		});
		
		JButton groupControlBTN = new JButton();
		groupControlBTN.setBounds(465, 260, 350, 20);
		groupControlBTN.setText("Управление группами");
		groupControlBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 
				main.JF.dispose();
				main.JF = groupControl.GetGroupControlFrame();
				
			}
		});
		
		JButton userControlBTN = new JButton();
		userControlBTN.setBounds(465, 300, 350, 20);
		userControlBTN.setText("Управление пользователями");
		userControlBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 
				main.JF.dispose();
				//main.JF = userControl.GetUserComntrolFrame();
				main.JF = userControlMenu.GetUserControlMenuFrame();
			}
		});
		
		JButton planExamBTN = new JButton();
		planExamBTN.setBounds(465, 340, 350, 20);
		planExamBTN.setText("Планировщик экзамнов");
		planExamBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 
				main.JF.dispose();
				main.JF = planExam.GetPlanExamFrame();
				
			}
		});
		
		JButton userStatisticBTN = new JButton();
		userStatisticBTN.setBounds(465, 380, 350, 20);
		userStatisticBTN.setText("Отчёты");
		userStatisticBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.JF.dispose();
				main.JF = reportsMenu.GetReportsMenuJF();
			}
		});
		
		
		JPanel adminMainPanel = new JPanel();
		adminMainPanel.setLayout(null);
		
		adminMainPanel.add(planExamBTN);
		adminMainPanel.add(groupControlBTN);
		adminMainPanel.add(userControlBTN);
		adminMainPanel.add(userCabinetBTN);
		adminMainPanel.add(exitBTN);
		adminMainPanel.add(userStatisticBTN);
		
		JF.add(adminMainPanel);
		curPanel=adminMainPanel;
		JF.setVisible(true);
		
		return JF;
	}
	
}
