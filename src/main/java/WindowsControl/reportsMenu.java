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

public class reportsMenu {

	public static JFrame GetReportsMenuJF() {
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
			reportsMenuJF.setTitle("Driving school: Reports");
			
			reportsMenuJF.setBounds(commonData.maxScreenWidtn/2-commonData.REPORTS_WINDOW_WIGTH/2,commonData.maxScreenHeigt/2-commonData.REPORTS_WINDOW_HEIGHT/2
					,commonData.REPORTS_WINDOW_WIGTH,commonData.REPORTS_WINDOW_HEIGHT );
			reportsMenuJF.setMinimumSize(new Dimension(commonData.REPORTS_WINDOW_WIGTH,commonData.REPORTS_WINDOW_HEIGHT));
			reportsMenuJF.setMaximumSize(new Dimension(commonData.REPORTS_WINDOW_WIGTH,commonData.REPORTS_WINDOW_HEIGHT));
			
			JButton backBTN = new JButton();
			
			backBTN.setBounds(15, 330, 80, 20);
			backBTN.setText("Назад");
			backBTN.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					main.JF.dispose();
					main.JF = mainWindow.GetAdminMainJFrame();
				}
			});
			
			
			JButton reportOnUser = new JButton();
			
			reportOnUser.setBounds(20, 20, 240, 20);
			reportOnUser.setText("Статистика ученика");
			reportOnUser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					main.JF.dispose();
					main.JF = userStatisticReport.GetUserStatisticReportsMenuJF();
				}
			});
			
			JButton reportOnGroup = new JButton();
			
			reportOnGroup.setBounds(20, 50, 240, 20);
			reportOnGroup.setText("Отчёт по группе");
			reportOnGroup.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					main.JF.dispose();
					main.JF = reportOnGroupMenu.GetReportOnGroupMenuJF();
				}
			});
			
			JButton reportOnGroups = new JButton();
			
			reportOnGroups.setBounds(20, 80, 240, 20);
			reportOnGroups.setText("Отчёт по группам");
			reportOnGroups.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					main.JF.dispose();
					main.JF = reportOnGroupsMenu.GetReportOnGroupsMenuJF();
				}
			});
			
			JPanel reportsMenuJP = new JPanel();
			reportsMenuJP.setLayout(null);
			reportsMenuJP.add(backBTN);
			reportsMenuJP.add(reportOnUser);
			reportsMenuJP.add(reportOnGroup);
			reportsMenuJP.add(reportOnGroups);
			//reportsMenuJP.add(backBTN);
			
			reportsMenuJF.add(reportsMenuJP);
			
		
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.REPORTS_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.REPORTS_WINDOW_ERROR_KODE);
		}
		
		
		return reportsMenuJF;
		
	}
	
	
}
