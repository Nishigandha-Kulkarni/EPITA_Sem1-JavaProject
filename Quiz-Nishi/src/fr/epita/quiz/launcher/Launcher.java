package fr.epita.quiz.launcher;

import java.sql.SQLException;
import java.util.Scanner;

import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.exception.CreateFailedException;
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
			case "q":
				System.out.println("Good bye!");
				break;

			default:
				System.out.println("Option not recognized, please enter an other option");
				break;
			}
		}

		scanner.close();

	}

	private static void questionCreation(Scanner scanner) {
		System.out.println("Question creation ...");
		
		
	}

	private static void quizCreation(Scanner scanner) {
		System.out.println("Quiz creation ...");
		
		System.out.println("Enter Quiz Name");
		String quizName = scanner.nextLine();
		Quiz quiz = new Quiz(quizName);
		
		QuizJDBCDAO qz = new QuizJDBCDAO();
		try {
			qz.create(quiz);
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
		System.out.println("q. Quit the application");
		System.out.println("What is your choice ? (1|2|q) :");
		answer = scanner.nextLine();
		return answer;
	}

	private static boolean authenticate(Scanner scanner) throws SQLException {
		System.out.println("Please enter your login : ");
		String login = scanner.nextLine();
		System.out.println("Please enter your password : ");
		String password = scanner.nextLine();

        QuizJDBCDAO qz = new QuizJDBCDAO();
		
		if(qz.auth(login, password)) {
			
		System.out.println("Auth succ ");
		return true;
		}
		else {
			System.out.println("Auth fqiled");
			return false;}
		}
	}


