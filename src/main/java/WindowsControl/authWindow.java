package WindowsControl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import driving_school_maven.driving_school_maven.main;
import driving_school_maven.driving_school_maven.userInfo;
import DataBaseControl.authorization;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.commonData;
import driving_school_maven.driving_school_maven.commonFunctions;

public class authWindow {
	public static JFrame GetAuthWindow() {
		
		JFrame AuthJF = new JFrame() {};
		BufferedImage appIcon;
		
		try {
		
			try {
				appIcon = ImageIO.read(new File("src/pictures/icon.png"));
				AuthJF.setIconImage(appIcon);
			} catch (IOException e) {
				e.printStackTrace();
				LogWriter.WriteLog(DefaultErrors.PICTURE_LOAD_ERROR+" icon.png : \n "+e.getMessage());
			}
				
			JLabel loginLabel = new JLabel();
			loginLabel.setText("Логин:");
			loginLabel.setBounds(40, 100, 100, 15);
			
			JTextField loginField = new JTextField(15);
			loginField.setBounds(40, 115, 220, 20);
			
			JPasswordField passwdField = new JPasswordField();
			passwdField.setBounds(40, 150, 220, 20);
			
			JLabel passwdLabel = new JLabel();
			passwdLabel.setText("Пароль:");
			passwdLabel.setBounds(40, 135, 100, 15);
			
			JButton authBTN = new JButton();
			
			authBTN.setBounds(90, 180, 120, 20);
			authBTN.setText("Войти");
			authBTN.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					String passwd = new String(passwdField.getPassword());
					String login = loginField.getText();
					
					
					if(authorization.authUser(login,passwd) && passwd.length()>0 && login.length()>0) {
						userInfo.LoadUserFromDB(login);
						main.JF.dispose();
						
						//main.JF=mainWindow.GetMainJFrame();	
						if(userInfo.role.equals("Ученик")) main.JF=mainWindow.GetUserMainJFrame();
						else main.JF=mainWindow.GetAdminMainJFrame();
						
						currentWindowInfo.SetCurFrame(main.JF);
						return;
					}					
					JOptionPane.showMessageDialog(main.JF, "Пользователь не авторизован, проверьте логин и пароль", 
			                "Ошибка авторизации", JOptionPane.ERROR_MESSAGE);
					LogWriter.WriteLog(DefaultErrors.AUTH_ERROR+"\n"+"User authorization denied, incorrect data");
				}
				
			});
			
			
			JPanel authPanel = new JPanel();
			authPanel.setLayout(null);
			
			authPanel.add(loginLabel);
			authPanel.add(loginField);
			authPanel.add(passwdLabel);
			authPanel.add(passwdField);
			authPanel.add(authBTN);
			
			
			AuthJF.setVisible(true);
			AuthJF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			AuthJF.setTitle("Driving school clinet: Authorization");
			
			AuthJF.setBounds(commonData.maxScreenWidtn/2-commonData.AUTH_WINDOW_WIGTH/2,commonData.maxScreenHeigt/2-commonData.AUTH_WINDOW_HEIGHT/2
					,commonData.AUTH_WINDOW_WIGTH,commonData.AUTH_WINDOW_HEIGHT );
			AuthJF.setMinimumSize(new Dimension(commonData.AUTH_WINDOW_WIGTH,commonData.AUTH_WINDOW_HEIGHT));
			AuthJF.setMaximumSize(new Dimension(commonData.AUTH_WINDOW_WIGTH,commonData.AUTH_WINDOW_HEIGHT));
			authPanel.setMinimumSize(new Dimension(commonData.AUTH_WINDOW_WIGTH,commonData.AUTH_WINDOW_HEIGHT));
			authPanel.setMaximumSize(new Dimension(commonData.AUTH_WINDOW_WIGTH,commonData.AUTH_WINDOW_HEIGHT));
			
			AuthJF.add(authPanel);
			
			AuthJF.addComponentListener(commonFunctions.createResizeAdapterForDefWindows(AuthJF));
			
			} catch (Exception e) {
				System.exit(DefaultErrors.AUTH_WINDOW_ERROR_KODE);
			}
		
		
		return  AuthJF;
	}
	
	ComponentAdapter createResizeAdapterForDefWindows(JFrame JF) {
		
		ComponentAdapter resizeAdapterForDefWindows = new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	        	JF.setSize(commonData.AUTH_WINDOW_WIGTH,commonData.AUTH_WINDOW_HEIGHT);
	            
	        }
	    };
	    
	    return (resizeAdapterForDefWindows);
	}
	
}
