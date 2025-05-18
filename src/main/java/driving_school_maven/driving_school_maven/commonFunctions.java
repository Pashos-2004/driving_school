package driving_school_maven.driving_school_maven;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class commonFunctions {

	public static ComponentAdapter createResizeAdapterForDefWindows(JFrame JF) {
		
		ComponentAdapter resizeAdapterForDefWindows = new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	            
	        	double curWidth = JF.getWidth();
	        	double curHeight = JF.getHeight();
	            
	        	if (curWidth != commonData.appWidth) {
	        		curHeight = commonData.appHeight + (curWidth-commonData.appWidth)/commonData.screenWidth*commonData.screenHeigh ;
	        	}else if (curHeight != commonData.appHeight) {
	        		curWidth = commonData.appWidth + (curHeight-commonData.appHeight)/commonData.screenHeigh*commonData.screenWidth ;
	        	}
	        	
	        	JF.setSize((int) curWidth,(int) curHeight);
	            
	        }
	    };
	    
	    return (resizeAdapterForDefWindows);
	}
	
	
	
	
}
