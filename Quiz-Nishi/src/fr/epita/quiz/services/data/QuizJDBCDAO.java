package fr.epita.quiz.services.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.epita.quiz.datamodel.MCQChoice;
import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.exception.CreateFailedException;
import fr.epita.quiz.exception.SearchFailedException;
import fr.epita.quiz.services.ConfigEntry;
import fr.epita.quiz.services.ConfigurationService;

public class QuizJDBCDAO {

	
	private static QuizJDBCDAO instance;
	
	Connection con = null;
	
	private static final String INSERT_QUIZ_QUERY = "INSERT into QUIZ (name) values(?)";
	private static final String UPDATE_QUIZ_QUERY = "UPDATE QUIZ SET NAME=? WHERE ID = ?";
	private static final String INSERT_QUESTION_QUERY = "INSERT into QUESTIONS (QUESTION_TEXT,TOPIC,DIFFICULTY,QUESTION_TYPE) values(?,?,?,?)";
	private static final String UPDATE_QUESTION_QUERY = "UPDATE QUIZ SET NAME=? WHERE ID = ?";
	private static final String DELETE_QUERY = "DELETE FROM QUIZ  WHERE ID = ?";
	private static final String AUTH_QUERY = "SELECT * FROM USERS  WHERE USER_ID = ? and PASSWORD = ? and STUD_FLAG = ?";
	private static final String GET_QUE_FOR_TOPIC = "SELECT * FROM USERS  WHERE USER_ID = ? and PASSWORD = ?";
	
	private static final String JDBC_DRIVER = "org.h2.Driver"; 
	
	public QuizJDBCDAO() {
	
	}

	public static QuizJDBCDAO getInstance() {
		if (instance == null) {
			instance = new QuizJDBCDAO();
		}
		return instance;
	}
	
	private Connection getConnection() throws SQLException {
		ConfigurationService conf = ConfigurationService.getInstance();
		Connection connection = null;
		String username = conf.getConfigurationValue("db.username", "");
		String password = conf.getConfigurationValue("db.password", "");
		String url = conf.getConfigurationValue("db.url", "");
		
		
		try {
			Class.forName(JDBC_DRIVER);
//			connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa","");
			connection = DriverManager.getConnection(url,username,password);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if (connection == null)
			throw new SQLException("Problem opening a connection with H2 db");
		else
			return connection;
	}
	
	public boolean auth (String un, String pw, String sflag) throws SQLException {
		Connection connection = null;
		
		try { 
			
			connection = getConnection();
			PreparedStatement pstmt = connection.prepareStatement(AUTH_QUERY); 
			pstmt.setString(1, un);
			pstmt.setString(2, pw);
			pstmt.setString(3,sflag);
			
			System.out.println("Connection established...........");
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) 
				return true;
			else 
				return false;
	
		} catch (Exception sqle) {
			// TODO transform into UpdateFailedException
		}finally {
			if(connection !=null)
				connection.close();
		}
		
		
		return false;
	} 

	/**
	 * Creates a quiz in the database, if a problem occurs then it throws an
	 * {@link CreateFailedException} usage example: QuizJDBCDAO dao = new ... try{
	 * dao.create(quizInstance); }catch(CreateFailed e){ //log exception }
	 * 
	 * @param quiz
	 * @throws CreateFailedException
	 */
	public void createQuiz(Quiz quiz) throws CreateFailedException {

		try (Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(INSERT_QUIZ_QUERY);) {
			pstmt.setString(1, quiz.getTitle());
			pstmt.execute();
		} catch (SQLException sqle) {
			throw new CreateFailedException(quiz);
		}

	}
	
	/**
	 * Creates a quiz in the database, if a problem occurs then it throws an
	 * {@link CreateFailedException} usage example: QuizJDBCDAO dao = new ... try{
	 * dao.create(quizInstance); }catch(CreateFailed e){ //log exception }
	 * 
	 * @param quiz
	 * @throws CreateFailedException
	 */
	public void createQuestion(Question que) throws CreateFailedException {

		try (Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(INSERT_QUESTION_QUERY);) {
			pstmt.setString(1, que.getQuestions_TEXT());
			pstmt.setString(2, que.getTopics());
			pstmt.setInt(3, que.getDifficulty());
			pstmt.setString(4, que.getQuestionType());
			pstmt.execute();
			
		}catch (SQLException sqle) {
			// TODO transform into UpdateFailedException
		}

	}

	public void update(Quiz quiz) {
		try (Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(UPDATE_QUIZ_QUERY);) {
			pstmt.setString(1, quiz.getTitle());
			pstmt.setInt(2, quiz.getId());
			pstmt.execute();
		} catch (SQLException sqle) {
			// TODO transform into UpdateFailedException
		}

	}
	
	public void updateQuestion(Question que, String newQuestionText) {
		String updateQuery = ConfigurationService.getInstance()
				.getConfigurationValue(ConfigEntry.DB_QUERIES_QUESTION_UPDATEQUERY,"");
		
		try (Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(updateQuery);) {
			
			pstmt.setString(1, newQuestionText);
			pstmt.setInt(2, que.getQUESTION_ID());
			pstmt.setString(3, que.getQuestions_TEXT());
			
			pstmt.execute();
		} catch (SQLException sqle) {
			// TODO transform into UpdateFailedException
		}

	}
    
	public void insertMCQChoice(MCQChoice mcqChoice) {
		String searchQuery = ConfigurationService.getInstance()
				.getConfigurationValue(ConfigEntry.DB_QUERIES_MCQCHOICE_INSERTQUERY,"");
		
		try (Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(searchQuery);) {
			pstmt.setInt(1, mcqChoice.getQuestionId());
			pstmt.setInt(2, mcqChoice.getAnswerId());
			pstmt.setString(3, mcqChoice.getAnswerText());
			pstmt.setBoolean(4, mcqChoice.getValid());
			pstmt.execute();
		} catch (SQLException sqle) {
			// TODO transform into UpdateFailedException
		}

	}
	
	public void delete(Quiz quiz) {
		try (Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(DELETE_QUERY);) {
			pstmt.setInt(1, quiz.getId());
			pstmt.execute();
		} catch (SQLException sqle) {
			// TODO transform into UpdateFailedException
		}
	}
	
	public void deleteQuestion(Question que) {
		
		String searchQuery = ConfigurationService.getInstance()
				.getConfigurationValue(ConfigEntry.DB_QUERIES_QUESTION_DELETEQUERY,"");
		
		try (Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(searchQuery);) {
			pstmt.setInt(1, que.getQUESTION_ID());
			pstmt.execute();
		} catch (SQLException sqle) {
			// TODO transform into UpdateFailedException
		}
	}

	public void deleteMCQCHOICE(MCQChoice mcqChoice) {
		
		String searchQuery = ConfigurationService.getInstance()
				.getConfigurationValue(ConfigEntry.DB_QUERIES_MCQCHOICE_DELETEQUERY,"");
		
		try (Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(searchQuery);) {
			pstmt.setInt(1, mcqChoice.getQuestionId());
			pstmt.execute();
		} catch (SQLException sqle) {
			// TODO transform into UpdateFailedException
		}
	}
	
	public Quiz getById(int id) {
		return null;

	}

	public List<Quiz> search(Quiz quizCriterion) throws SearchFailedException {
		String searchQuery = ConfigurationService.getInstance()
				.getConfigurationValue(ConfigEntry.DB_QUERIES_QUIZ_SEARCHQUERY,"");
		List<Quiz> quizList = new ArrayList<>();
		try (Connection connection = getConnection();

				PreparedStatement pstmt = connection.prepareStatement(searchQuery)) {

			pstmt.setInt(1, quizCriterion.getId());
			pstmt.setString(2, "%" + quizCriterion.getTitle() + "%");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID");
				String topic = rs.getString("NAME");
				Quiz quiz = new Quiz(topic);
				quiz.setId(id);
				quizList.add(quiz);
			}

			rs.close();
		} catch (SQLException e) {
			throw new SearchFailedException(quizCriterion);
		}
		return quizList;
	}
	
	public List<Question> searchQuestion(Question queCriterion) throws SearchFailedException {
		String searchQuery = ConfigurationService.getInstance()
				.getConfigurationValue(ConfigEntry.DB_QUERIES_QUESTION_SEARCHQUERY,"");
		List<Question> queList = new ArrayList<>();
		try (Connection connection = getConnection();

				PreparedStatement pstmt = connection.prepareStatement(searchQuery)) {

			
			pstmt.setString(1, "%" + queCriterion.getTopics() + "%");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("QUESTION_ID");
				String queText = rs.getString("QUESTION_TEXT");
				String queType = rs.getString("QUESTION_TYPE");
				String hint = rs.getString("HINT");
				Question que = new Question(queText);
				que.setQUESTION_ID(id);
				que.setQuestions_TEXT(queText);
				que.setQuestionType(queType);
				que.setHint(hint);
				queList.add(que);
			}

			rs.close();
		} catch (SQLException e) {
			throw new SearchFailedException(queCriterion);
		}
		return queList;
	}
	
	public List<MCQChoice> searchMCQChoice(MCQChoice queID) throws SearchFailedException {
		String searchQuery = ConfigurationService.getInstance()
				.getConfigurationValue(ConfigEntry.DB_QUERIES_MCQCHOICE_SEARCHQUERY,"");
		List<MCQChoice> mcqChoiceList = new ArrayList<>();
		try (Connection connection = getConnection();

				PreparedStatement pstmt = connection.prepareStatement(searchQuery)) {

			pstmt.setInt(1, queID.getQuestionId());

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int queId = rs.getInt("QUESTION_ID");
				int ansId = rs.getInt("ANSWER_ID");
				String ansText = rs.getString("ANSWER_TEXT");
				boolean valid = rs.getBoolean("VALID");
				MCQChoice mcqChoice = new MCQChoice(queId,ansId,ansText,valid);
				mcqChoice.setQuestionId(queId);
				mcqChoice.setAnswerId(ansId);
				mcqChoice.setAnswerText(ansText);
				mcqChoice.setValid(valid);
				
				mcqChoiceList.add(mcqChoice);
			}

			rs.close();
		} catch (SQLException e) {
			throw new SearchFailedException(queID);
		}
		return mcqChoiceList;
	}
	
	
	
}
