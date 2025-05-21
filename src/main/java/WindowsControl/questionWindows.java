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
import javax.swing.JDialog;
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
	private static JButton showExplanationBTN = new JButton();
	private static JButton nextQuestionBTN = new JButton();
	private static JLabel imageLabel = new JLabel();
	private static JLabel questionLabel = new JLabel();
	private static JButton buttonArr[];
	private static JPanel buttonGroup;
	private static JButton btns[] = new JButton[5];
	
	
	private static boolean isExam = false;
	private static int errorCount = 0;
	private static int solvedCount = 0;
	
	public static Stack <question> questions = new Stack<question>();
	public static question curQuestion;
	
	public static JPanel GetCommonQuestionsJPanel() {
		JPanel JP = new JPanel();
		try {
		
        imageLabel.setBounds((1280-480)/2, 0, 480, 180); // Центрируем по горизонтали
        questionLabel.setBounds((1280-480)/2, 185, 480, 95);
        buttonGroup = new JPanel();
        buttonGroup.setLayout(new GridLayout(4, 1, 0, 20)); 
        buttonGroup.setBounds(400, 280, 480, 400); 

        for (int i = 1; i <= 4; i++) {
            JButton btn = new JButton("Кнопка " + i);
 
            buttonGroup.add(btn);
        }
        
        showExplanationBTN.setBounds(20,660,150,20);
        showExplanationBTN.setText("Объяснение");
        showExplanationBTN.setVisible(false);
        showExplanationBTN.addActionListener(new ActionListener() {

        	
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane pane = new JOptionPane("<html><center>"+curQuestion.explanation+"</center></html>", 
                        JOptionPane.PLAIN_MESSAGE);
					JDialog dialog = pane.createDialog("Подсказка");
					
					dialog.setSize(900, 720); // Устанавливаем нужный размер
					dialog.setVisible(true);
					/*
				JOptionPane.showMessageDialog(main.JF, 
						"<html><center>"+curQuestion.explanation+"</center></html>", 
		                "Подсказка", JOptionPane.PLAIN_MESSAGE);
				*/
			}
		}
        );
        
        nextQuestionBTN.setBounds(1080,660,150,20);
        nextQuestionBTN.setText("Дальше");
        nextQuestionBTN.setVisible(false);
        nextQuestionBTN.addActionListener(new ActionListener() {

        	
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isExam) showNextQuestionExam();
				else showNextQuestionWorkout();
					/*
				JOptionPane.showMessageDialog(main.JF, 
						"<html><center>"+curQuestion.explanation+"</center></html>", 
		                "Подсказка", JOptionPane.PLAIN_MESSAGE);
				*/
			}
		}
        );
        
        
        JP.setLayout(null);
        JP.add(questionLabel);
        JP.add(imageLabel);
        JP.add(buttonGroup);
        JP.add(nextQuestionBTN);
        JP.add(showExplanationBTN);
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
			ResultSet resultSet = statement.executeQuery("SELECT question_id, question ,picture_file_name,block_num,right_answer, "
					+ "answer_1, answer_2, answer_3, answer_4, explanation "
					+ " FROM question join picture on picture.picture_id = question.picture_id "
					+ "ORDER BY random()  LIMIT 10 ; ");
			while (resultSet.next()) {
				question qu = new question(resultSet);
				questions.add(qu);
			}
			showNextQuestionWorkout();
		}catch (Exception e) {
			e.printStackTrace();
			MyExeptions.LogWriter.WriteLog(MyExeptions.DefaultErrors.QUESTION_FRAME_CREACTIONERROR +"\n"+ e.getMessage() );
			System.exit(DefaultErrors.QUESTION_WINDOW_ERROR_KODE);
		}
		
		
		return JF;
	}
	

	public static void showNextQuestionWorkout() {
		if(questions.isEmpty()) {
			JOptionPane.showMessageDialog(main.JF, 
					"Тренировка закончена", 
	                "Тренажёр", JOptionPane.INFORMATION_MESSAGE);
			main.JF.dispose();
			main.JF = mainWindow.GetUserMainJFrame();
			return;
		}
		curQuestion = questions.pop();
		showExplanationBTN.setVisible(false);
		nextQuestionBTN.setVisible(false);
		
		try {
			ImageIcon questionIcon = new ImageIcon("src/pictures/question_pic/"+curQuestion.picture_name);
			imageLabel.setIcon(questionIcon);
			questionLabel.setText("<html><center>"+curQuestion.question+"</center></html>");
			LoadButtonGroup();
			
			for(int i = 1;i<curQuestion.countOfAnswers+1;i++) {
				JButton btn = new JButton("<html><center>"+curQuestion.answers[i]+"</center></html>");
				btns[i] = btn;
				
				btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						boolean res = true;
						JButton buffBTN = (JButton) e.getSource();
						btns[curQuestion.right_answer].setBackground(new Color(0,255,0));
						if(! buffBTN.getText().equals("<html><center>"+curQuestion.answers[curQuestion.right_answer]+"</center></html>")) {
							btn.setBackground(new Color(255,0,0));
							res = false;
						}
						for(int i=1;i<curQuestion.countOfAnswers+1;i++) {
							btns[i].setEnabled(false);
						}
						try {
							Connection connection =  postgreSQLConnection.GetConnection();
							Statement statement = connection.createStatement();
							statement.execute("insert into answer (user_id, question_id, is_right ) "
									+ "values("+userInfo.user_id +" , "+ curQuestion.question_id+" , "+ res +")");
							}catch (Exception e1) {
								MyExeptions.LogWriter.WriteLog(MyExeptions.DefaultErrors.DB_INSERT_ERROR +"\n"+ e1.getMessage() );
								System.exit(DefaultErrors.QUESTION_WINDOW_ERROR_KODE);
							}
						
						showExplanationBTN.setVisible(true);
						nextQuestionBTN.setVisible(true);
					}
				} );
				buttonGroup.add(btn);
			}
			//workoutJPanel.add(buttonGroup);
			
		}catch (Exception e) {
			e.printStackTrace();
			MyExeptions.LogWriter.WriteLog(MyExeptions.DefaultErrors.QUESTION_FRAME_CREACTIONERROR +"\n"+ e.getMessage() );
				System.exit(DefaultErrors.QUESTION_WINDOW_ERROR_KODE);
		}
		
		
	}
	
	
	public static void showNextQuestionExam() {
		if(questions.isEmpty()) {
			JOptionPane.showMessageDialog(main.JF, 
					"Тренировка закончена", 
	                "Тренажёр", JOptionPane.INFORMATION_MESSAGE);
			main.JF.dispose();
			main.JF = mainWindow.GetUserMainJFrame();
			return;
		}
		curQuestion = questions.pop();
		showExplanationBTN.setVisible(false);
		nextQuestionBTN.setVisible(false);
		
		try {
			ImageIcon questionIcon = new ImageIcon("src/pictures/question_pic/"+curQuestion.picture_name);
			imageLabel.setIcon(questionIcon);
			questionLabel.setText("<html><center>"+curQuestion.question+"</center></html>");
			LoadButtonGroup();
			
			for(int i = 1;i<curQuestion.countOfAnswers+1;i++) {
				JButton btn = new JButton("<html><center>"+curQuestion.answers[i]+"</center></html>");
				btns[i] = btn;
				
				btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						boolean res = false;
						JButton buffBTN = (JButton) e.getSource();
						btns[curQuestion.right_answer].setBackground(new Color(0,255,0));
						if(! buffBTN.getText().equals("<html><center>"+curQuestion.answers[curQuestion.right_answer]+"</center></html>")) {
							btn.setBackground(new Color(255,0,0));
							res = true;
						}
						for(int i=1;i<curQuestion.countOfAnswers+1;i++) {
							btns[i].setEnabled(false);
						}
						try {
							Connection connection =  postgreSQLConnection.GetConnection();
							Statement statement = connection.createStatement();
							/*statement.execute("insert into answer (user_id, question_id, is_right ) "
									+ "values("+userInfo.user_id +" , "+ curQuestion.question_id+" , "+ res +")");
							*/
							}catch (Exception e1) {
								MyExeptions.LogWriter.WriteLog(MyExeptions.DefaultErrors.DB_INSERT_ERROR +"\n"+ e1.getMessage() );
								System.exit(DefaultErrors.QUESTION_WINDOW_ERROR_KODE);
							}
						
						showExplanationBTN.setVisible(true);
						nextQuestionBTN.setVisible(true);
					}
				} );
				buttonGroup.add(btn);
			}
			//workoutJPanel.add(buttonGroup);
			
		}catch (Exception e) {
			e.printStackTrace();
			MyExeptions.LogWriter.WriteLog(MyExeptions.DefaultErrors.QUESTION_FRAME_CREACTIONERROR +"\n"+ e.getMessage() );
				System.exit(DefaultErrors.QUESTION_WINDOW_ERROR_KODE);
		}
		
		
	}
	
	private static void LoadButtonGroup() {
		workoutJPanel.remove(buttonGroup);
		buttonGroup = new JPanel();
		buttonGroup.setLayout(new GridLayout(curQuestion.countOfAnswers, 1, 0, 20)); 
        buttonGroup.setBounds(400, 280, 480, 400); 
        workoutJPanel.add(buttonGroup);
	}
	
	
	
}



class question {
	int question_id;
	String question;
	String answers [];
	String picture_name;
	int block_num;
	int right_answer;
	String explanation;
	int countOfAnswers = 0;
	public question(ResultSet resSet) {
		
		try {
			question_id = resSet.getInt("question_id");
			question = resSet.getString("question");
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


