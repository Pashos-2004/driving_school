package driving_school_maven.driving_school_maven;

import MyExeptions.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;


public class screenSettings {
	
	protected static Dimension GetMaxDim(int screenHeigh, int screenWidht) {
		
		int multiplier = 80;
		
		while(screenHeigh >= multiplier * 9 && screenWidht >= multiplier * 16 ) {multiplier++;};
		
		multiplier--;
		
		Dimension maxDim = new Dimension(16 * multiplier,9 * multiplier);
		
		return (maxDim);
	}
	
	public static JFrame SetScreenSizeForMainWindow(JFrame JF) throws Exception {
		
		
		Toolkit toolKit = Toolkit.getDefaultToolkit();
		
		commonData.screenHeigh = toolKit.getScreenSize().height;
		commonData.screenWidth = toolKit.getScreenSize().width;
		
		//System.out.println(commonData.screenHeigh);
		//System.out.println(commonData.screenWidth);
		
		if(commonData.screenHeigh<commonData.MIN_SCREEN_HEIGHT || commonData.screenWidth<commonData.MIN_SCREEN_WIDTH) {
			throw new SystemRequirementsError(DefaultErrors.SCREEN_SIZE_IS_TOO_SMALL);
		}
		
		commonData.appWidth = 1280;
		commonData.appHeight = 720;
		
		Dimension minDim = new Dimension(commonData.MIN_SCREEN_WIDTH,commonData.MIN_SCREEN_HEIGHT);
		Dimension maxDim = GetMaxDim(commonData.screenHeigh,commonData.screenWidth);
		
		commonData.maxScreenHeigt = maxDim.height;
		commonData.maxScreenWidtn = maxDim.width;
		
		//System.out.println(commonData.maxScreenHeigt);
		//System.out.println(commonData.maxScreenWidtn);
		
		JF.setBounds(commonData.screenWidth/2-commonData.appWidth/2, commonData.screenHeigh/2-commonData.appHeight/2, commonData.appWidth, commonData.appHeight);
		JF.setMinimumSize(minDim);
		JF.setMaximumSize(maxDim);

		return JF;
	}
	
	
	
	
}
