package WindowsControl;

import java.awt.Color;
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

import DataBaseControl.authorization;
import DataBaseControl.postgreSQLConnection;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.commonData;
import driving_school_maven.driving_school_maven.group;
import driving_school_maven.driving_school_maven.main;
import driving_school_maven.driving_school_maven.userInfo;

public class groupControl {
	private static JComboBox<String> groupComboBox ;
	private static JTextField newGroupName;
	private static JCheckBox isShowNonActiveGroup;
	private static ArrayList<group> groups = new ArrayList<group>(20); 
	private static JButton ActivateDisableGRoupBTN;
	private static JButton CreateGroupBTN;
	
	private static int curGroupIndexInArray = 1;
	private static JPanel curJP;
	
	public static JFrame GetGroupControlFrame() {
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
			
			CreateGroupBTN = new JButton();

			CreateGroupBTN.setBounds(290, 10, 100, 20);
			CreateGroupBTN.setText("Созадать");
			CreateGroupBTN.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String groupName = newGroupName.getText();
					if(groupName.length()<5){
						JOptionPane.showMessageDialog(main.JF,"Ошибка добавлении группы, название слишком короткое","Ошибка",JOptionPane.ERROR_MESSAGE );
						return;
					}
					try {
						
						Connection connection =  postgreSQLConnection.GetConnection();
						Statement statement = connection.createStatement();
						
						ResultSet resSet = statement.executeQuery("select * from _group where name_ = '" + groupName+"'");
						if(resSet.next()){
							JOptionPane.showMessageDialog(main.JF,"Ошибка добавлении группы, название уже занято","Ошибка",JOptionPane.ERROR_MESSAGE );
							return;
						}
						statement.execute("insert into _group (name_ , is_active) "
								+ "values ('"+groupName+"' , "+"true"+")");
						JOptionPane.showMessageDialog(main.JF,"Успешно","Создание группы",JOptionPane.INFORMATION_MESSAGE );
						newGroupName.setText("");
						loadComboBox(! isShowNonActiveGroup.isSelected());
						
					}catch (Exception e1){
						JOptionPane.showMessageDialog(main.JF,"Ошибка добавлении группы.","Ошибка",JOptionPane.ERROR_MESSAGE );
						LogWriter.WriteLog(DefaultErrors.GROUP_CREATION_ERROR+"\n "+e1.getMessage());
					}
					
					
				}
			});
			
			
			ActivateDisableGRoupBTN = new JButton();
			ActivateDisableGRoupBTN.setBounds(10,120,200,20);
			ActivateDisableGRoupBTN.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						//JOptionPane.showInternalConfirmDialog(exitBTN, e, null, curGroupIndexInArray, curGroupIndexInArray, null)
						if(JOptionPane.showConfirmDialog(main.JF, "ВЫ уверены? Все пользователи в группе так же станут активным/неактивными", "Предупреждение",
								JOptionPane.INFORMATION_MESSAGE )!=JOptionPane.OK_OPTION) return;
						
						Connection connection =  postgreSQLConnection.GetConnection();
						Statement statement = connection.createStatement();
						statement.execute("update _group set is_active = "+!groups.get(curGroupIndexInArray).isActive+" where group_id = "+groups.get(curGroupIndexInArray).groupId );
						statement.execute("update _user set is_deleted = "+groups.get(curGroupIndexInArray).isActive+" where group_id = "+groups.get(curGroupIndexInArray).groupId );
						LogWriter.WriteLog("group State changed by : "+userInfo.login+" "+"\n for : "  +groups.get(curGroupIndexInArray).groupName);
					}catch (Exception e1) {
						JOptionPane.showMessageDialog(main.JF,"Ошибка изменения группы.","Ошибка",JOptionPane.ERROR_MESSAGE );
						LogWriter.WriteLog(DefaultErrors.GROUP_CONTROL_FRAME_CREATION_ERROR+e1.getMessage());
						
					}
					
					loadComboBox(! isShowNonActiveGroup.isSelected());
				}
			});
			
			
			newGroupName = new JTextField();
			newGroupName.setBounds(10,10,270,20);
			
			isShowNonActiveGroup = new JCheckBox("Показать неактивные группы?",false);
			//isShowNonActiveGroup.setText();
			isShowNonActiveGroup.setBounds(10,70,270,20);
			isShowNonActiveGroup.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					loadComboBox(! isShowNonActiveGroup.isSelected());
					
				}
			});
			
			groupComboBox = GetDefaultGroupComboBox();
			
			
			
			
			JPanel groupControlJP = new JPanel();
			curJP = groupControlJP;
			groupControlJP.setLayout(null);
			
			
			
			groupControlJP.add(isShowNonActiveGroup);
			groupControlJP.add(newGroupName);
			groupControlJP.add(groupComboBox);
			groupControlJP.add(CreateGroupBTN);
			groupControlJP.add(ActivateDisableGRoupBTN);
			groupControlJP.add(exitBTN);
			
			JF.add(groupControlJP);
			JF.setVisible(true);
			
			
			
			JF.setBounds(commonData.maxScreenWidtn/2-commonData.GROUP_CONTROL_WINDOW_WIGTH/2,commonData.maxScreenHeigt/2-commonData.GROUP_CONTROL_WINDOW_HEIGHT/2
					,commonData.GROUP_CONTROL_WINDOW_WIGTH,commonData.GROUP_CONTROL_WINDOW_HEIGHT );
			
			loadComboBox(! isShowNonActiveGroup.isSelected());
			
			}catch (Exception e) {
				LogWriter.WriteLog(DefaultErrors.GROUP_CONTROL_FRAME_CREATION_ERROR+e.getMessage());
				System.exit(DefaultErrors.GROUP_CONTROL_WINDOW_ERROR_KODE);
			}
		
		
		return JF;
	}
	
	
	
	public static void loadComboBox(boolean isShowNonActive) {
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
			//curJP.remove(groupComboBox);
			//groupComboBox = GetDefaultGroupComboBox();
			//curJP.add(groupComboBox);
			groupComboBox.removeAllItems();
			//groupComboBox.add("");
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
		comboBox.setBounds(10,50,270,20);
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String) groupComboBox.getSelectedItem();
				for(int i=0; i<groups.size();i++) {
					if(groups.get(i).groupName.equals(selected)) {
						curGroupIndexInArray = i;
						if(groups.get(curGroupIndexInArray).isActive) {
							ActivateDisableGRoupBTN.setText("Сделать неактивной");
							
						}else {
							ActivateDisableGRoupBTN.setText("Сделать активной");
						}
						break;
					}
				}
			}
		});
		return comboBox;
	}
	
}
