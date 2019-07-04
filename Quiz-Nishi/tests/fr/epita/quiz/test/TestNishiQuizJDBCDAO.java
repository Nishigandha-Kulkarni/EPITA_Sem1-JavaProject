package fr.epita.quiz.test;

import java.util.List;

import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.services.data.QuizJDBCDAO;



public class TestNishiQuizJDBCDAO {

	private static String quizSearchFlag;

	public static void main(String[] args) throws Exception {
		
		
		QuizJDBCDAO quizJdbcDAOQuiz = new QuizJDBCDAO();
		
		//when
		Quiz quiz = new Quiz("New Java Quiz");
		
		//then
		List<Quiz> quizList = quizJdbcDAOQuiz.searchQuiz(quiz,quizSearchFlag);
		//List<Quiz> list = dao.search(new Quiz("Java"));
		if (quizList.isEmpty()) {
			throw new Exception("the list was empty");
		}
		
		System.out.println(quizList);
		
	}
}
