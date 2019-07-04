package fr.epita.quiz.test;


import fr.epita.quiz.datamodel.MCQChoice;
import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.exception.CreateFailedException;
import fr.epita.quiz.exception.SearchFailedException;
import fr.epita.quiz.services.data.QuizJDBCDAO;
import fr.epita.quiz.exception.DataAccessException;

public class TestDatabaseOperation {

	public static void main(String[] args) throws CreateFailedException, DataAccessException, SearchFailedException{
		
		QuizJDBCDAO qz = new QuizJDBCDAO();
		Quiz quiz = new Quiz("Java Quiz");
		
		System.out.println("Quiz Creation Test");
		qz.createQuiz(quiz);
				
//		Question question1 = new Question(queText);
		Question question = new Question("What is a Class","OOPs",100,"O",3,"Object Creation");
		
		System.out.println("Question Creation Test");
		qz.createQuestion(question);
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		
		System.out.println("Question Deletion Test");
		quizJdbcDAO.deleteQuestion(question);
		
		MCQChoice mcqChoice = new MCQChoice(); 
		System.out.println("MCQ Choice Creation Test");
		
		mcqChoice.setAnswerId(1);
		mcqChoice.setAnswerText("Object Creation");
		mcqChoice.setQuestionId(100);
		mcqChoice.setValid(true);
		quizJdbcDAO.insertMCQChoice(mcqChoice);
		
	}
}
