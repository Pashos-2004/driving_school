package WindowsControl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.commonFunctions;
import driving_school_maven.driving_school_maven.main;
import driving_school_maven.driving_school_maven.screenSettings;

public class mainWindow {
	public static JFrame GetMainJFrame() {
		main.JF = new JFrame() {};
		BufferedImage appIcon;
		try {
			main.JF = screenSettings.SetScreenSizeForMainWindow(main.JF);
			
			main.JF.setVisible(true);
			main.JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			main.JF.setTitle("Driving school clinet");
			main.JF.addComponentListener(commonFunctions.createResizeAdapterForDefWindows(main.JF));
		
		try {
			appIcon = ImageIO.read(new File("src/pictures/icon.png"));
			main.JF.setIconImage(appIcon);
		} catch (IOException e) {
			e.printStackTrace();
			LogWriter.WriteLog(DefaultErrors.PICTURE_LOAD_ERROR+" icon.png : \n "+e.getMessage());
		}
		
		} catch (Exception e) {
			System.exit(DefaultErrors.MAIN_WINDOW_ERROR_KODE);
		}
		
		
			return main.JF;
		
	}
}
