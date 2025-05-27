package WindowsControl;

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
import java.util.LinkedHashMap;
import java.util.Locale.Category;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import DataBaseControl.postgreSQLConnection;
import MyExeptions.DefaultErrors;
import MyExeptions.LogWriter;
import driving_school_maven.driving_school_maven.commonData;
import driving_school_maven.driving_school_maven.main;
import driving_school_maven.driving_school_maven.user;
import driving_school_maven.driving_school_maven.userInfo;

public class userStatistic {
	
	
	public static JFrame GetStatisticJF(long user_id) {
	JFrame UserStatisticJF = new JFrame() {};
	BufferedImage appIcon;

		try {
			
			try {
				appIcon = ImageIO.read(new File("src/pictures/icon.png"));
				UserStatisticJF.setIconImage(appIcon);
			} catch (IOException e) {
				e.printStackTrace();
				LogWriter.WriteLog(DefaultErrors.PICTURE_LOAD_ERROR+" icon.png : \n "+e.getMessage());
			}
			
			UserStatisticJF.setVisible(true);
			UserStatisticJF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			UserStatisticJF.setTitle("Driving school: User Statistic");
			
			UserStatisticJF.setBounds(commonData.maxScreenWidtn/2-commonData.USER_STATISTIC_WINDOW_WIGTH/2,commonData.maxScreenHeigt/2-commonData.USER_STATISTIC_WINDOW_HEIGHT/2
					,commonData.USER_STATISTIC_WINDOW_WIGTH,commonData.USER_STATISTIC_WINDOW_HEIGHT );
			UserStatisticJF.setMinimumSize(new Dimension(commonData.USER_STATISTIC_WINDOW_WIGTH,commonData.USER_STATISTIC_WINDOW_HEIGHT));
			UserStatisticJF.setMaximumSize(new Dimension(commonData.USER_STATISTIC_WINDOW_WIGTH,commonData.USER_STATISTIC_WINDOW_HEIGHT));
			
			JButton backBTN = new JButton();
			
			backBTN.setBounds(20, 650, 80, 20);
			backBTN.setText("Назад");
			backBTN.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					main.JF.dispose();
					if(userInfo.role.equals("Ученик")) {
						main.JF = mainWindow.GetUserMainJFrame();
						return;
					}
					main.JF = reportsMenu.GetReportsMenuJF();
				}
			});
			
			JLabel statisticLabel  = new JLabel();
			statisticLabel.setBounds(20,15,200,20);
			statisticLabel.setText("Статистика  " + FindUserLogin(user_id)+ " : ");
			
			
			int countOfAnswers = FindCounOfSolvedQuestions(user_id);
			int counOfRightSolvedQuestions = FindCounOfRightSolvedQuestions(user_id);
			double averageMark =FindAverageMarkForTheExam(user_id);
			int countOfExams = FindCountOfExams(user_id);
			int сountOfNonPassedExams = FindCountOfNonPassedExams(user_id);
			DefaultPieDataset diagramData = FindErrorsByTopic(user_id);
			JFreeChart chart = ChartFactory.createPieChart(
		            "Ошибки по темам",  // Заголовок
		            (PieDataset) diagramData,    // Данные
		            true,       // Легенда
		            true,       // Подсказки
		            false
		        );
			PiePlot plot = (PiePlot) chart.getPlot();
			plot.setLabelGenerator(null);
			JLabel countOfAnswersLabel = new JLabel();
			countOfAnswersLabel.setText("Всего ответов: "+countOfAnswers);
			countOfAnswersLabel.setBounds(20,40,200,20);

			JLabel countOfRightAnswersLabel = new JLabel();
			countOfRightAnswersLabel.setText("% правильных ответов: "+(counOfRightSolvedQuestions*1.0)/countOfAnswers*100);
			countOfRightAnswersLabel.setBounds(20,63,200,20);

			JLabel averageMarkLabel = new JLabel();
			averageMarkLabel.setText("Средняя оценка за экзамены "+averageMark);
			averageMarkLabel.setBounds(20,86,200,20);
			
			JLabel countOfExamsLabel = new JLabel();
			countOfExamsLabel.setText("Всего экзамнов пройдено "+countOfExams);
			countOfExamsLabel.setBounds(20,109,200,20);
			
			JLabel сountOfNonPassedExamsLabel = new JLabel();
			сountOfNonPassedExamsLabel.setText("Незакрытые экзамены "+сountOfNonPassedExams);
			сountOfNonPassedExamsLabel.setBounds(20,132,200,20);
			
			JPanel UserStatisticJP = new JPanel();
			UserStatisticJP.setLayout(null);
			
			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setBounds(300,20,800,600);
			
			UserStatisticJP.add(backBTN);
			UserStatisticJP.add(countOfAnswersLabel);
			UserStatisticJP.add(statisticLabel);
			UserStatisticJF.add(countOfRightAnswersLabel);
			UserStatisticJF.add(averageMarkLabel);
			UserStatisticJF.add(countOfExamsLabel);
			UserStatisticJF.add(сountOfNonPassedExamsLabel);
			UserStatisticJF.add(chartPanel);
			
			UserStatisticJF.add(UserStatisticJP);
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.UUSER_STATISTIC_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.USER_STATISTIC_WINDOW_ERROR_KODE);
		}
		
		return UserStatisticJF;
	}
	
	public static DefaultPieDataset  FindErrorsByTopic(long user_id) {
		DefaultPieDataset  data = new DefaultPieDataset();
		double countOfIncorrectAnswers = 0;
		
		try {
			Connection connection =  postgreSQLConnection.GetConnection();
			Statement statement = connection.createStatement();
			ResultSet resSet = statement.executeQuery("select count(*) as countOfIncorrectAnswers from answer where user_id = "+user_id + " and is_right = false" );
			ResultSet buff;
			resSet.next();
			countOfIncorrectAnswers = resSet.getInt("countOfIncorrectAnswers")*1.0;
			
			ArrayList<String> topics = new ArrayList<String>(20);
			resSet = statement.executeQuery("select tema_name, tema_id from tema" );
			int topic_id;
			String topic;
			int countOfAnswersOnTopic;
			while(resSet.next()) {
				topic_id = resSet.getInt("tema_id");
				topic = resSet.getString("tema_name");
				buff = connection.createStatement().executeQuery("select count(*) as countOfAnsOnTopic from answer "+
						"join question on question.question_id = answer.question_id "
						+ "join tema on question.tema_id = tema.tema_id "
						+ "where user_id =" + user_id +" and tema.tema_id = " + topic_id);
				buff.next();
				countOfAnswersOnTopic  = buff.getInt("countOfAnsOnTopic");
				data.setValue(topic, countOfAnswersOnTopic/countOfIncorrectAnswers);
			}
					
			
		}catch (Exception e) {
			e.printStackTrace();
			LogWriter.WriteLog(DefaultErrors.UUSER_STATISTIC_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
		
		return data;
	}
	
	public static String FindUserLogin(long user_id) {
		String userLogin = "";
		try {
			Connection connection =  postgreSQLConnection.GetConnection();
			Statement statement = connection.createStatement();
			ResultSet resSet = statement.executeQuery("select login from _user where user_id = "+user_id );
			resSet.next();
			userLogin = resSet.getString("login");
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.UUSER_STATISTIC_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		return userLogin;
		
	}
	
	public static int FindCountOfNonPassedExams(long user_id) {
		
		int res = 0;
		try {
			Connection connection =  postgreSQLConnection.GetConnection();
			Statement statement = connection.createStatement();
			ResultSet resSet = statement.executeQuery("select count(*) as CountOfExams from mark where user_id = "+user_id + " and mark < 3" );
			resSet.next();
			res = resSet.getInt("CountOfExams");
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.UUSER_STATISTIC_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		return res;
	}
	
	public static int FindCountOfExams(long user_id) {
		
		int res = 0;
		try {
			Connection connection =  postgreSQLConnection.GetConnection();
			Statement statement = connection.createStatement();
			ResultSet resSet = statement.executeQuery("select count(*) as CountOfExams from mark where user_id = "+user_id );
			resSet.next();
			res = resSet.getInt("CountOfExams");
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.UUSER_STATISTIC_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		return res;
	}
	
	public static double FindAverageMarkForTheExam(long user_id) {
		
		double res = 0;
		try {
			Connection connection =  postgreSQLConnection.GetConnection();
			Statement statement = connection.createStatement();
			ResultSet resSet = statement.executeQuery("select AVG(mark) as AverageMark from mark where user_id = "+user_id );
			resSet.next();
			res = resSet.getInt("AverageMark");
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.UUSER_STATISTIC_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		return res;
	}
	
	public static int FindCounOfRightSolvedQuestions(long user_id) {
		int res = 0;
		
		try {
			Connection connection =  postgreSQLConnection.GetConnection();
			Statement statement = connection.createStatement();
			ResultSet resSet = statement.executeQuery("select count(*) as CounOfRightSolvedQuestions from answer where user_id = "+user_id + " and is_right = true" );
			resSet.next();
			res = resSet.getInt("CounOfRightSolvedQuestions");
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.UUSER_STATISTIC_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
		return res;
	}
	
	public static int FindCounOfSolvedQuestions(long user_id) {
		int res = 0;
		
		try {
			Connection connection =  postgreSQLConnection.GetConnection();
			Statement statement = connection.createStatement();
			ResultSet resSet = statement.executeQuery("select count(*) as CounOfSolvedQuestions from answer where user_id = "+user_id );
			resSet.next();
			res = resSet.getInt("CounOfSolvedQuestions");
			
		}catch (Exception e) {
			LogWriter.WriteLog(DefaultErrors.UUSER_STATISTIC_FRAME_CREATION_ERROR + "\n "+e.getMessage());
			System.exit(DefaultErrors.USER_CONTROL_WINDOW_ERROR_KODE);
		}
		
		return res;
	}
	
}
