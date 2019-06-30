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
		
		Scanner scan = new Scanner(System.in);
		if (!authenticated) {
			scanner.close();
			scan.close();
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
				questionCreation(scanner);
				break;
				
			case "4":
				questionUpdation(scanner,scan);
				break;
				
			case "5":
				questionDeletion(scanner);
				break;
				
			case "6":
				mcqChoiceCreation(scanner);
				break;
				
			case "7":
				examSimulation(scanner);
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
		scan.close();
		
	}
	
	private static void questionDeletion(Scanner scanner) {
		
//		Get the name of the topic
		System.out.println("Enter the topic");		
		String queText = scanner.nextLine();
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		Question question = new Question(queText);
				
		try {
//			Search in the table Questions based on the topic provided
			List<Question> questionList = quizJdbcDAO.searchQuestion(question);
//			Display question 1 by 1
			
			for (int i = 0; i < questionList.size(); i++)
				// Display the question
				System.out.println("Question " + (i + 1) + " - " + questionList.get(i));
			
			System.out.println("Enter the question no. to delete");
			int questionIndex = scanner.nextInt();
			
			question.setQUESTION_ID(questionList.get(questionIndex-1).getQUESTION_ID());
			
			quizJdbcDAO.deleteQuestion(question);
		
		}
		catch (SearchFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void questionUpdation(Scanner scanner, Scanner scan) {
		
//		Get the name of the topic
		System.out.println("Enter the topic");		
		String queText = scanner.nextLine();
		
		
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		Question question = new Question(queText);
		
		
		int questionIndex = 0;
				
		try {
//			Search in the table Questions based on the topic provided
			List<Question> questionList = quizJdbcDAO.searchQuestion(question);
//			Display question 1 by 1
			
			for (int i = 0; i < questionList.size(); i++)
				// Display the question
				System.out.println("Question " + (i + 1) + " - " + questionList.get(i));
			
			System.out.println("Enter the question no. to update(1,2,3 ...)");
			questionIndex = scanner.nextInt();
			
			System.out.println("Enter the updated question");
			String newQuestionText = scan.nextLine();
			
			
			question.setQUESTION_ID(questionList.get(questionIndex-1).getQUESTION_ID());
			question.setQuestions_TEXT(questionList.get(questionIndex-1).getQuestions_TEXT());
			
			quizJdbcDAO.updateQuestion(question,newQuestionText);
			
		}
			catch (SearchFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     
		
		
	}
	
	private static void mcqChoiceCreation(Scanner scanner) {
		
//		Get the name of the topic
		System.out.println("Enter the topic");		
		String queText = scanner.nextLine();
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		Question question = new Question(queText);
		MCQChoice mcqChoice = new MCQChoice();
		
		int countMCQChoice = 0;
				
		try {
//			Search in the table Questions based on the topic provided
			List<Question> questionList = quizJdbcDAO.searchQuestion(question);
//			Display question 1 by 1
			
			for (int i = 0; i < questionList.size(); i++) {
				// Display the question
				System.out.println("Question " + (i + 1) + " - " + questionList.get(i));
				
				System.out.println("Enter the count of MCQ choices for this question:");		
				countMCQChoice = scanner.nextInt();
				
				for (int j = 0; j < countMCQChoice; j++) {
					
					System.out.println("Enter choice no- " +j);
					String choice = scanner.nextLine();
					
					mcqChoice.setQuestionId(questionList.get(i).getQUESTION_ID());
					mcqChoice.setAnswerId(j+1);
					mcqChoice.setAnswerText(choice);
					
					System.out.println("Please indicate if this option is a valid option ?(Y or N)");
					String isValid = scanner.nextLine();
					
					if (isValid.equals("Y"))
						mcqChoice.setValid(true);
					else
						mcqChoice.setValid(false);
					
					quizJdbcDAO.insertMCQChoice(mcqChoice);
						
				}
				
			}
		} catch (SearchFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}			
	
    
	private static void examSimulation(Scanner scanner) {
		
//		Get the name of the topic
		System.out.println("Enter the topic");
		String queText = scanner.nextLine();
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		Question question = new Question(queText);
		
		// Actual count of correct MCQ options for the question
		int actualMCQCountCorrect = 0;
		// Count of correct answers
		int correctMCQChoiceCount = 0;
		float actualQuestionCount = 0;
		float correctQuestionCount = 0;
		
		
		
		try {
//			Search in the table Questions based on the topic provided
			List<Question> questionList = quizJdbcDAO.searchQuestion(question);
//			Display question 1 by 1
			
			actualQuestionCount = questionList.size();
			for (int i = 0; i < questionList.size(); i++) {
				// Display the question
				System.out.println("Question " + (i + 1) + " - " + questionList.get(i));
				
			    // Get the MCQ Choice based on the Question ID
				MCQChoice mcqChoice = new MCQChoice(questionList.get(i).getQUESTION_ID());
				List<MCQChoice> mcqChoicesList = quizJdbcDAO.searchMCQChoice(mcqChoice);
								
				for (int j = 0; j < mcqChoicesList.size(); j++) {
					// Display all the Choice for the question i
					System.out.println("Choice " + (j + 1) + " - " + mcqChoicesList.get(j));
					if	(mcqChoicesList.get(j).getValid())
						actualMCQCountCorrect ++;
				}
				// Count of correct choices out of all 
				System.out.println("Enter the count of correct choices (1,2,3,4 ..)");
				int givenMCQCountCorrect = scanner.nextInt();
				
				for (int k = 0; k < givenMCQCountCorrect; k++) {
					System.out.println("Enter your choice " + (k+1));
					int choice = scanner.nextInt();
					
					// Check if the given answer is correct or not
					if (mcqChoicesList.get(choice-1).getValid())
						correctMCQChoiceCount++;
				}
				
//				Evaluation at question level
				if(actualMCQCountCorrect == givenMCQCountCorrect && 
						actualMCQCountCorrect == correctMCQChoiceCount ) 
					
					correctQuestionCount++;
				
//				Clearing iterative variables
				
				actualMCQCountCorrect = 0;
				givenMCQCountCorrect = 0;
				correctMCQChoiceCount = 0;
			}
			
//			Overall Quiz evaluation
			System.out.println("Total no. of questions: " +actualQuestionCount);
			System.out.println("Total no. of correct answers: " +correctQuestionCount);
			System.out.println("Percentage: " + ( correctQuestionCount / actualQuestionCount ) * 100 + "%");
			
			
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
		
		String questionType = "";
		do {
			System.out.println("Enter Question topic");
			questionType = scanner.nextLine();    
        } while (!questionType.equalsIgnoreCase("M") && !questionType.equalsIgnoreCase("O"));
		
		Question question = new Question(queText,queTopic,queDiff,questionType);
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


