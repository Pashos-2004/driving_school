package driving_school_maven.driving_school_maven;


import MyExeptions.*;
import WindowsControl.currentWindowInfo;
import DataBaseControl.*;
import WindowsControl.*;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.beans.Statement;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class main {
	
	
	public static JFrame JF;
	
	public static void main (String arg[]) {
				
		JF = authWindow.GetAuthWindow();
		currentWindowInfo.SetCurFrame(JF);
		
	}
	
	
	
	
	
}
