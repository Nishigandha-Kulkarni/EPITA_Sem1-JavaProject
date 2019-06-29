package fr.epita.quiz.launcher;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import fr.epita.quiz.datamodel.Authentication;
import fr.epita.quiz.datamodel.MCQChoice;
import fr.epita.quiz.datamodel.Question;
import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.exception.CreateFailedException;
import fr.epita.quiz.exception.SearchFailedException;
import fr.epita.quiz.services.data.QuizJDBCDAO;

public class Launcher {

	public static void main(String[] args) throws SQLException {

		Scanner scanner = new Scanner(System.in);
		boolean authenticated = authenticate(scanner);
		if (!authenticated) {
			scanner.close();
			return;
		}
		
		
		String answer = "";
		while (!answer.equals("q")) {

			answer = displayMenu(scanner);

			switch (answer) {
			case "1":
				quizCreation(scanner);
				break;
			case "2":
				questionCreation(scanner);
				break;
				
			case "3":
				examSimulation(scanner);
				break;
			case "q":
//				System.out.println("Good bye!");
				
				System.out.println("Enter Quiz ID");
				int queID = scanner.nextInt();
				
				System.out.println("Enter Quiz Name");
				String queName = scanner.nextLine();
				
				QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
				Quiz quiz = new Quiz(queID,queName);
				
				try {
					List<Quiz> quizList = quizJdbcDAO.search(quiz);
//					System.out.println(quizList);
					for (int i = 0; i < quizList.size(); i++) {
						System.out.println(quizList.get(i));
					}
					
				} catch (SearchFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				break;

			default:
				System.out.println("Option not recognized, please enter an other option");
				break;
			}
		}

		scanner.close();

	}
    
	private static void examSimulation(Scanner scanner) {
		
//		Get the name of the topic
		System.out.println("Enter the topic");
		String queText = scanner.nextLine();
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		Question question = new Question(queText);
		
		try {
//			Search in the table Questions based on the topic provided
			List<Question> questionList = quizJdbcDAO.searchQuestion(question);
//			Display question 1 by 1
			for (int i = 0; i < questionList.size(); i++) {
				// Display the question
				System.out.println("Question " + i + " - " + questionList.get(i));
				
			    // Get the MCQ Choice based on the Question ID
				MCQChoice mcqChoice = new MCQChoice(questionList.get(i).getQUESTION_ID());
				List<MCQChoice> mcqChoicesList = quizJdbcDAO.searchMCQChoice(mcqChoice);
				System.out.println("Choose your Answer");
				
				for (int j = 0; j < mcqChoicesList.size(); j++) 
					// Display all the Choice for the question i
					System.out.println("Choice " + j + " - " + mcqChoicesList.get(j));
							
				
			}
			
		} catch (SearchFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void questionCreation(Scanner scanner) {
		System.out.println("Question creation ...");
		
		System.out.println("Enter Question text");
		String queText = scanner.nextLine();
		
		System.out.println("Enter Question topic");
		String queTopic = scanner.nextLine();
		
		System.out.println("Enter Question diffculty(1,2,3)");
		int queDiff = scanner.nextInt();
		
		Question question = new Question(queText,queTopic,queDiff);
		QuizJDBCDAO qz = new QuizJDBCDAO();
		
		try {
			qz.createQuestion(question);
		} catch (CreateFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void quizCreation(Scanner scanner) {
		System.out.println("Quiz creation ...");
		
		System.out.println("Enter Quiz Name");
		String quizName = scanner.nextLine();
		Quiz quiz = new Quiz(quizName);
		
		QuizJDBCDAO qz = new QuizJDBCDAO();
		try {
			qz.createQuiz(quiz);
		} catch (CreateFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static String displayMenu(Scanner scanner) {
		String answer;
		System.out.println("-- Menu --");
		System.out.println("1. Create Quiz");
		System.out.println("2. Create Question");
		System.out.println("3. Gibe Exam");
		System.out.println("q. Quit the application");
		System.out.println("What is your choice ? (1|2|q) :");
		answer = scanner.nextLine();
		return answer;
	}

	private static boolean authenticate(Scanner scanner) throws SQLException {
		
		System.out.println("Login:");
		System.out.println("Type of login (S - Student or A - Admin login) ");
		String StudFlag = scanner.nextLine();
		
		Authentication.studentFlag = StudFlag;		
		
		System.out.println("Please enter your login : ");
		String login = scanner.nextLine();
		
		System.out.println("Please enter your password : ");
		String password = scanner.nextLine();

        QuizJDBCDAO qz = new QuizJDBCDAO();
		
		if(qz.auth(login, password,StudFlag)) {
			
		System.out.println("Auth succ ");
		return true;
		}
		else {
			System.out.println("Auth failed");
			return false;}
		}
	}


