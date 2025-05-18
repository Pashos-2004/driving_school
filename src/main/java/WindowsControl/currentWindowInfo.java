package WindowsControl;

import javax.swing.*;

public class currentWindowInfo {
	protected static JFrame curJF;
	
	public static void SetCurFrame(JFrame JF) {
		curJF = JF;
	}
	public static JFrame GetCurFrame() {
		return (curJF);
	}
}
