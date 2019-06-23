package fr.epita.quiz.services.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.exception.CreateFailedException;
import fr.epita.quiz.exception.SearchFailedException;
import fr.epita.quiz.services.ConfigEntry;
import fr.epita.quiz.services.ConfigurationService;

public class QuizJDBCDAO {

	
	private static QuizJDBCDAO instance;
	
	Connection con = null;
	
	private static final String INSERT_QUERY = "INSERT into QUIZ (name) values(?)";
	private static final String UPDATE_QUERY = "UPDATE QUIZ SET NAME=? WHERE ID = ?";
	private static final String DELETE_QUERY = "DELETE FROM QUIZ  WHERE ID = ?";
	private static final String AUTH_QUERY = "SELECT * FROM USERS  WHERE USER_ID = ? and PASSWORD = ?";
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
//		String username = conf.getConfigurationValue("db.username", "");
//		String password = conf.getConfigurationValue("db.password", "");
//		String url = conf.getConfigurationValue("db.url", "");
		
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa","");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if (connection == null)
			throw new SQLException("Problem opening a connection with H2 db");
		else
			return connection;
	}
	
	public boolean auth (String un, String pw) throws SQLException {
		Connection connection = null;
		
		try { 
			
			connection = getConnection();
			PreparedStatement pstmt = connection.prepareStatement(AUTH_QUERY); 
			pstmt.setString(1, un);
			pstmt.setString(2, pw);
			
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
	public void create(Quiz quiz) throws CreateFailedException {

		try (Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(INSERT_QUERY);) {
			pstmt.setString(1, quiz.getTitle());
			pstmt.execute();
		} catch (SQLException sqle) {
			throw new CreateFailedException(quiz);
		}

	}

	public void update(Quiz quiz) {
		try (Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(UPDATE_QUERY);) {
			pstmt.setString(1, quiz.getTitle());
			pstmt.setInt(2, quiz.getId());
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
}
