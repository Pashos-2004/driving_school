package WindowsControl;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.Stack;

import javax.print.attribute.standard.OutputDeviceAssigned;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import DataBaseControl.postgreSQLConnection;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import MyExeptions.MyExeptionBase;
import driving_school_maven.driving_school_maven.commonData;
import driving_school_maven.driving_school_maven.main;
import driving_school_maven.driving_school_maven.userInfo;

public class questionWindows {
	
	private static JPanel workoutJPanel = new JPanel();
	private static JButton exitBTN = new JButton();
	private static JLabel imageLabel = new JLabel();
	private static JButton buttonArr[];
	private static JPanel buttonGroup;
	
	private static int errorCount = 0;
	private static int solvedCount = 0;
	
	public static Stack <question> questions = new Stack<question>();
	public static question curQuestion;
	
	public static JPanel GetCommonQuestionsJPanel() {
		JPanel JP = new JPanel();
		try {
		
        imageLabel.setBounds((1280-480)/2, 50, 480, 180); // Центрируем по горизонтали
        
        buttonGroup = new JPanel();
        buttonGroup.setLayout(new GridLayout(4, 1, 0, 20)); 
        buttonGroup.setBounds(400, 280, 480, 400); 

        for (int i = 1; i <= 4; i++) {
            JButton btn = new JButton("Кнопка " + i);
 
            buttonGroup.add(btn);
        }
        
		
       
        JP.setLayout(null);
        JP.add(imageLabel);
        JP.add(buttonGroup);
        
		//workoutJpanel.add(exitBTN);
		
		
		}catch (Exception e) {
			e.printStackTrace();
			MyExeptions.LogWriter.WriteLog(MyExeptions.DefaultErrors.QUESTION_FRAME_CREACTIONERROR +"\n"+ e.getMessage() );
			System.exit(DefaultErrors.QUESTION_WINDOW_ERROR_KODE);
		}
		
		return JP;
	}
	
	
	public static JFrame GetWorkoutJFrame() {
		JFrame JF = mainWindow.GetMainJFrame();
		workoutJPanel = GetCommonQuestionsJPanel();
		JF.add(workoutJPanel);
		JF.setVisible(true);
		
		exitBTN.setBounds(1130, 0, 150, 20);
		exitBTN.setText("Завершить");
		exitBTN.setBackground(new Color(253, 255, 133));
		exitBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				main.JF.dispose();
				main.JF = mainWindow.GetUserMainJFrame();	
				//exitBTN.setVisible(false);
			}
		});
		
		workoutJPanel.add(exitBTN);
		
		try {
			Connection connection =  postgreSQLConnection.GetConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT question_id, picture_file_name,block_num,right_answer, "
					+ "answer_1, answer_2, answer_3, answer_4, explanation "
					+ " FROM question join picture on picture.picture_id = question.picture_id "
					+ "ORDER BY random()  LIMIT 10 ; ");
			while (resultSet.next()) {
				question qu = new question(resultSet);
				questions.add(qu);
			}
			showNextQuestion();
		}catch (Exception e) {
			e.printStackTrace();
			MyExeptions.LogWriter.WriteLog(MyExeptions.DefaultErrors.QUESTION_FRAME_CREACTIONERROR +"\n"+ e.getMessage() );
			System.exit(DefaultErrors.QUESTION_WINDOW_ERROR_KODE);
		}
		
		
		return JF;
	}
	

	public static void showNextQuestion() {
		if(questions.isEmpty()) {
			JOptionPane.showMessageDialog(main.JF, 
					"Тренировка закончена", 
	                "Тренажёр", JOptionPane.INFORMATION_MESSAGE);
			main.JF.dispose();
			main.JF = mainWindow.GetUserMainJFrame();
		}
		curQuestion = questions.pop();
		
		try {
			ImageIcon questionIcon = new ImageIcon("src/pictures/question_pic/"+curQuestion.picture_name);
			imageLabel.setIcon(questionIcon);
			LoadButtonGroup();
			
			for(int i = 1;i<curQuestion.countOfAnswers;i++) {
				JButton btn = new JButton(curQuestion.answers[i]);
				btn.addActionListener(GetActionListenerForButtonsWorkout(i, curQuestion.right_answer));
				buttonGroup.add(btn);
			}
			//workoutJPanel.add(buttonGroup);
			
		}catch (Exception e) {
			e.printStackTrace();
			MyExeptions.LogWriter.WriteLog(MyExeptions.DefaultErrors.QUESTION_FRAME_CREACTIONERROR +"\n"+ e.getMessage() );
				//System.exit(DefaultErrors.QUESTION_WINDOW_ERROR_KODE);
		}
		
		
	}
	
	private static void LoadButtonGroup() {
		workoutJPanel.remove(buttonGroup);
		buttonGroup = new JPanel();
		buttonGroup.setLayout(new GridLayout(curQuestion.countOfAnswers, 1, 0, 20)); 
        buttonGroup.setBounds(400, 280, 480, 400); 
        workoutJPanel.add(buttonGroup);
	}
	
	private static void ActionListenerForButtonsBase() {
		
		
	}
	
	private static ActionListener GetActionListenerForButtonsWorkout(int index,int right_index) {
		ActionListener act = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionListenerForButtonsBase();
				
			}
		}; 
		
		return act;
	}
	private static ActionListener GetActionListenerForButtonsExam() {
		ActionListener act = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionListenerForButtonsBase();
				
			}
		}; 
		
		return act;
	}
	
}



class question {
	int question_id;
	String answers [];
	String picture_name;
	int block_num;
	int right_answer;
	String explanation;
	int countOfAnswers = 0;
	public question(ResultSet resSet) {
		
		try {
			question_id = resSet.getInt("question_id");
			picture_name = resSet.getString("picture_file_name");
			block_num = resSet.getInt("block_num");
			right_answer = resSet.getInt("right_answer");
			explanation = resSet.getString("explanation");
			answers = new String[5];
			for(int i=1;i<=4; i++) {
				String answ = resSet.getString("answer_"+i);
				
				if(answ.equals("")) break;
				answers[i] = answ;
				countOfAnswers+=1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			MyExeptions.LogWriter.WriteLog(MyExeptions.DefaultErrors.QUESTION_FRAME_CREACTIONERROR +"\n"+ e.getMessage() );
			//System.exit(DefaultErrors.QUESTION_WINDOW_ERROR_KODE);
		}
		
		
		
	}
}


