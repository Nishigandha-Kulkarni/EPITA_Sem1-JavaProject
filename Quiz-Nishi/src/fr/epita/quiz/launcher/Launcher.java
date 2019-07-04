package fr.epita.quiz.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

	public static void main(String[] args) throws SQLException, SearchFailedException, IOException {

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
				questionUpdation(scanner);
				break;
				
			case "4":
				questionDeletion(scanner);
				break;
				
			case "5":
				questionSearchBasedOnTopic(scanner);
				break;
				
			case "6":
				mcqChoiceCreation(scanner);
				break;
				
			case "7":
				examSimulation(scanner);
				break;
				
			case "8":
				quizExportToFile(scanner);
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
	
	
	private static void quizExportToFile(Scanner scanner) throws IOException, SearchFailedException {
		
		File file = initializeFile();
		
        System.out.println("Exporting the list of Quiz to a file");
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		Quiz quiz = new Quiz();
		
		// A - To select All, I - Based on ID
		String quizSearchFlag = "A";
		
		List<Quiz> quizList = quizJdbcDAO.searchQuiz(quiz,quizSearchFlag);
		
		if(!quizList.isEmpty()) {

		writeQuiz(quizList, file);
		}			
		
	}
	
	private static File initializeFile() throws IOException {
		File file = new File("quizList.txt");
		if (!file.exists()) {
			File parentFile = file.getAbsoluteFile().getParentFile();
			parentFile.mkdirs();
			file.createNewFile();
		}
		return file;
	}
	
	private static void writeQuiz(List<Quiz> quiz, File file) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		
	    //Display List quiz list
	    for (int i = 0; i < quiz.size(); i++)
		writer.println("Quiz " + (i + 1) + " - " + quiz.get(i));
		writer.flush();
		writer.close();
	}
	

	private static void questionSearchBasedOnTopic(Scanner scanner) {
		
		if((Authentication.studentFlag.contentEquals("A")))
		{
		
		System.out.println("Question Search");
		System.out.println("");
		
		String searchFlag = "T";
		
//		Get the name of the topic
		System.out.println("Enter the topic");		
		String queText = scanner.nextLine();
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		Question question = new Question(queText);
				
		try {
//			Search in the table Questions based on the topic provided
			List<Question> questionList = quizJdbcDAO.searchQuestion(question,searchFlag);
//			Display question 1 by 1
			
			for (int i = 0; i < questionList.size(); i++)
				// Display the question
				System.out.println("Question " + (i + 1) + " - " + questionList.get(i));
		
		}
		
		catch (SearchFailedException e) {
			System.out.println("The search was not successful"+e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else
		{
			System.out.println("This option cannot be accessed by students please select Option 7 to Give Exam, Option 8 to Export the Quiz or q to Quit");
		}
		
	}

	private static void questionDeletion(Scanner scanner) throws SearchFailedException {
		
		if((Authentication.studentFlag.contentEquals("A")))
		{
		
		System.out.println("Question Deletion");
		System.out.println("");
		
		//Display the quiz list
		QuizJDBCDAO quizJdbcDAOQuiz = new QuizJDBCDAO();
		Quiz quiz = new Quiz();
				
		// A - To select All, I - Based on ID
		String quizSearchFlag = "A";
								
	    List<Quiz> quizList = quizJdbcDAOQuiz.searchQuiz(quiz,quizSearchFlag);	
			    
	    if(!quizList.isEmpty()) {
					
		//Display List quiz list
		for (int i = 0; i < quizList.size(); i++) 
								
		// Display the Quiz
		System.out.println("Quiz " + (i + 1) + " - " + quizList.get(i));
							
		// Ask for the quiz name
		System.out.println("Choose the quiz(1,2,3,...)");
		int quizID = Integer.parseInt(scanner.nextLine());
							
		//Get the actual quiz id
		int actualQuizID = quizList.get(quizID-1).getId();
				
				
		Question question = new Question(actualQuizID);
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		MCQChoice mcqChoice = new MCQChoice();
				
		try {
			
			String searchFlag = "I";
			
//			Search in the table Questions based on the topic provided
			List<Question> questionList = quizJdbcDAO.searchQuestion(question,searchFlag);
//			Display question 1 by 1
			
			for (int i = 0; i < questionList.size(); i++)
				// Display the question
				System.out.println("Question " + (i + 1) + " - " + questionList.get(i));
			
			System.out.println("Enter the question number to delete");
			int questionIndex = Integer.parseInt(scanner.nextLine());
			
			question.setQUESTION_ID(questionList.get(questionIndex-1).getQUESTION_ID());
			
			quizJdbcDAO.deleteQuestion(question);
			System.out.println("Question has been deleted Successfully");
			
			if(questionList.get(questionIndex-1).getQuestionType().equals("M")) {
				mcqChoice.setQuestionId(questionList.get(questionIndex-1).getQUESTION_ID());
				quizJdbcDAO.deleteMCQCHOICE(mcqChoice);
				
			}
		}
		catch (SearchFailedException e) {
			System.out.println("The search was not successful"+e.getMessage());

			e.printStackTrace();
		}
		}
	    else {
					System.out.println("No Quiz available!");
		}
	    }
		else
		{
			System.out.println("This option cannot be accessed by students please select Option 7 to Give Exam, Option 8 to Export the Quiz or q to Quit");
		}
	}
	
	private static void questionUpdation(Scanner scanner) throws SearchFailedException {
		
		if((Authentication.studentFlag.contentEquals("A")))
		{
		System.out.println("Question Updation");
		System.out.println("");
		
        //Display the quiz list
		QuizJDBCDAO quizJdbcDAOQuiz = new QuizJDBCDAO();
		Quiz quiz = new Quiz();
		
		// A - To select All, I - Based on ID
		String quizSearchFlag = "A";
						
	    List<Quiz> quizList = quizJdbcDAOQuiz.searchQuiz(quiz,quizSearchFlag);	
	    
	    if(!quizList.isEmpty()) {
			
			//Display List quiz list
			for (int i = 0; i < quizList.size(); i++) 
						
		    // Display the Quiz
		    System.out.println("Quiz " + (i + 1) + " - " + quizList.get(i));
					
		    // Ask for the quiz name
			System.out.println("Choose the quiz(1,2,3,...)");
		    int quizID = Integer.parseInt(scanner.nextLine());
					
			//Get the actual quiz id
			int actualQuizID = quizList.get(quizID-1).getId();
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		Question question = new Question(actualQuizID);
		
		
		int questionIndex = 0;
				
		try {
			
			String searchFlag = "I";
			
//			Search in the table Questions based on the topic provided
			List<Question> questionList = quizJdbcDAO.searchQuestion(question, searchFlag);
//			Display question 1 by 1
		
			for (int i = 0; i < questionList.size(); i++)
				// Display the question
				System.out.println("Question " + (i + 1) + " - " + questionList.get(i));
			
			System.out.println("Enter the question number to update(1,2,3 ...)");
			questionIndex = Integer.parseInt(scanner.nextLine());
			
			System.out.println("Enter the updated question");
			String newQuestionText = scanner.nextLine();	
			
			question.setQUESTION_ID(questionList.get(questionIndex-1).getQUESTION_ID());
			question.setQuestions_TEXT(questionList.get(questionIndex-1).getQuestions_TEXT());
			
			quizJdbcDAO.updateQuestion(question,newQuestionText);
			System.out.println("The Question has been updated successfully");
			
		}
			catch (SearchFailedException e) {
				System.out.println("The search was not successful"+e.getMessage());

				e.printStackTrace();
			}
	    }
		
		else {
			System.out.println("No Quiz available!");
		}
		}
		else
		{
			System.out.println("This option cannot be accessed by students please select Option 7 to Give Exam, Option 8 to Export the Quiz or q to Quit");
		}
	}
	
	private static void mcqChoiceCreation(Scanner scanner) throws SearchFailedException {
		
		if((Authentication.studentFlag.contentEquals("A")))
		{
		System.out.println("MCQ Choice creation");
		System.out.println("");
		QuizJDBCDAO quizJdbcDAOQuiz = new QuizJDBCDAO();
		Quiz quiz = new Quiz();
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		
		// A - To select All, I - Based on ID
		String quizSearchFlag = "A";
				
	    List<Quiz> quizList = quizJdbcDAOQuiz.searchQuiz(quiz,quizSearchFlag);
				
	    if(!quizList.isEmpty()) {
					
		//Display List quiz list
		for (int i = 0; i < quizList.size(); i++) 
					
	    // Display the Quiz
	    System.out.println("Quiz " + (i + 1) + " - " + quizList.get(i));
				
	    // Ask for the quiz name
		System.out.println("Choose the quiz(1,2,3,...)");
	    int quizID = Integer.parseInt(scanner.nextLine());
				
		//Get the actual quiz id
		int actualQuizID = quizList.get(quizID-1).getId();
		
		
		Question question = new Question(actualQuizID);
		MCQChoice mcqChoice = new MCQChoice();

		
		int countMCQChoice = 0;
				
		try {
			
			String searchFlag = "I";
			
//			Search in the table Questions based on the topic provided
			List<Question> questionList = quizJdbcDAO.searchQuestion(question, searchFlag);
//			Display question 1 by 1
			
			for (int i = 0; i < questionList.size(); i++) {
				
				if(questionList.get(i).getQuestionType().equals("M")) {
					
				// Check if the question has already MCQ choices -- Skip the question if it has!

				MCQChoice mcqChoiceSearch = new MCQChoice(questionList.get(i).getQUESTION_ID());
				List<MCQChoice> mcqChoicesList = quizJdbcDAO.searchMCQChoice(mcqChoiceSearch);
				
				if(mcqChoicesList.isEmpty())  {
				
				// Display the question
				System.out.println("Question " + (i + 1) + " - " + questionList.get(i));
				
				System.out.println("Enter the count of MCQ choices for this question:");		
				countMCQChoice = Integer.parseInt(scanner.nextLine());
				
				for (int j = 0; j < countMCQChoice; j++) {
					
					System.out.println("Enter choice no- " + (j+1));
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
				System.out.println("MCQ Choices have been created");
				}
				
				if(!mcqChoicesList.isEmpty())
					mcqChoicesList.clear();
				}
			}
		} catch (SearchFailedException e) {
			System.out.println("The search was not successful"+e.getMessage());

			e.printStackTrace();
		}
	    }
	    else {
			System.out.println("No Quiz available!");
		}
		}
		else
		{
			System.out.println("This option cannot be accessed by students please select Option 7 to Give Exam, Option 8 to Export the Quiz or q to Quit");
		}		
	}			
	
    
	private static void examSimulation(Scanner scanner) throws SearchFailedException {
		
		if((Authentication.studentFlag.contentEquals("S")))
		{
		
		System.out.println("Quiz Execution");
		System.out.println("");
		QuizJDBCDAO quizJdbcDAOQuiz = new QuizJDBCDAO();
		Quiz quiz = new Quiz();
		
		// A - To select All, I - Based on ID
		String quizSearchFlag = "A";
		
		List<Quiz> quizList = quizJdbcDAOQuiz.searchQuiz(quiz,quizSearchFlag);
		
		if(!quizList.isEmpty()) {
			
		//Display List quiz list
		for (int i = 0; i < quizList.size(); i++) 
			
		// Display the Quiz
	    System.out.println("Quiz " + (i + 1) + " - " + quizList.get(i));
		
		// Ask for the quiz name
		System.out.println("Choose the quiz(1,2,3,...)");
		int quizID = Integer.parseInt(scanner.nextLine());
		
		//Get the actual quiz id
		int actualQuizID = quizList.get(quizID-1).getId();
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		Question question = new Question(actualQuizID);
		
		// Actual count of correct MCQ options for the question
		int actualMCQCountCorrect = 0;
		// Count of correct answers
		int correctMCQChoiceCount = 0;
		float actualQuestionCount = 0;
		float correctQuestionCount = 0;
		
		
		
		try {
			
			String searchFlag = "I";
			
//			Search in the table Questions based on the topic provided
			List<Question> questionList = quizJdbcDAO.searchQuestion(question, searchFlag);
//			Display question 1 by 1
						
			for (int i = 0; i < questionList.size(); i++) {
				
				
				if(questionList.get(i).getQuestionType().equals("M")) {
					
				// Display the question
				System.out.println("Question " + (i + 1) + " - " + questionList.get(i));
					
				actualQuestionCount++;
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
				int givenMCQCountCorrect = Integer.parseInt(scanner.nextLine());
				
				for (int k = 0; k < givenMCQCountCorrect; k++) {
					System.out.println("Enter your choice " + (k+1));
					int choice = Integer.parseInt(scanner.nextLine());
					
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
				else {
					System.out.println("This is an open Question!");
					// Display the question
					System.out.println("Question " + (i + 1) + " - " + questionList.get(i));
					System.out.println("Hint:" + questionList.get(i).getHint());
					String openQuestion = scanner.nextLine();
					
					
				}
			}
			
//			Overall Quiz evaluation
			System.out.println("Total no. of MCQ questions: " +actualQuestionCount);
			System.out.println("Total no. of correct answers: " +correctQuestionCount);
			System.out.println("Percentage: " + ( correctQuestionCount / actualQuestionCount ) * 100 + "%");
			
			
		} catch (SearchFailedException e) {
			System.out.println("The search was not successful"+e.getMessage());

			e.printStackTrace();
		}
		
		}
		else {
			System.out.println("No Quiz available!");
		}
		}
		else
		{
			System.out.println("This option cannot be accessed by Admin please select other Option");
		}	
	}
	

	private static void questionCreation(Scanner scanner) throws SearchFailedException {
		
		if((Authentication.studentFlag.contentEquals("A")))
		{
		System.out.println("Question creation ...");
		
		QuizJDBCDAO quizJdbcDAO = new QuizJDBCDAO();
		Quiz quiz = new Quiz();
		
		// Hint for open question
		String hint = "";
		
		// A - To select All, I - Based on ID
		String quizSearchFlag = "A";
		
		List<Quiz> quizList = quizJdbcDAO.searchQuiz(quiz,quizSearchFlag);
		
		if(!quizList.isEmpty()) {
			
		//Display List quiz list
		for (int i = 0; i < quizList.size(); i++) 
			
		// Display the Quiz
	    System.out.println("Quiz " + (i + 1) + " - " + quizList.get(i));
		
		// Ask for the quiz name
		System.out.println("Enter the quiz number where question has to be added(1,2,3,...)");
		int quizID = Integer.parseInt(scanner.nextLine());
		
		//Get the actual quiz id
		int actualQuizID = quizList.get(quizID-1).getId();
		
		System.out.println("Enter Question text");
		String queText = scanner.nextLine();
		
		System.out.println("Enter Question topic");
		String queTopic = scanner.nextLine();
		
		System.out.println("Enter Question diffculty(1,2,3,...)");
		int queDiff = Integer.parseInt(scanner.nextLine());
		
		System.out.println("Enter Question Type (O - Open, M - MCQ)");
		
		String questionType = "";
		int i = 0;
		do {
			if (i != 0)
				System.out.println("Enter Question Type (O - Open, M - MCQ)");
			questionType = scanner.nextLine();    
        } while (!questionType.equalsIgnoreCase("M") && !questionType.equalsIgnoreCase("O"));
		
		if (questionType.equals("O")) {
			System.out.println("Enter a hint for the Question");
		    hint = scanner.nextLine();
		}
		
		Question question = new Question(queText,queTopic,queDiff,questionType,actualQuizID,hint);
		QuizJDBCDAO qz = new QuizJDBCDAO();
		
		try {
			qz.createQuestion(question);
			System.out.println("Question has been created successfully");
		} catch (CreateFailedException e) {
			System.out.println("The creation was not successful"+e.getMessage());
			
			e.printStackTrace();
		}
		
		}
		else {
			System.out.println("No Quiz available!");
		}
	   }
	   else
	    {
		System.out.println("This option cannot be accessed by students please select Option 7 to Give Exam, Option 8 to Export the Quiz or q to Quit");
	    }
	}

	private static void quizCreation(Scanner scanner) {
		
		if((Authentication.studentFlag.contentEquals("A")))
		{
		
		System.out.println("Quiz creation ...");
		
		System.out.println("Enter Quiz Name");
		String quizName = scanner.nextLine();
		Quiz quiz = new Quiz(quizName);
		
		QuizJDBCDAO qz = new QuizJDBCDAO();
		try {
			qz.createQuiz(quiz);
			System.out.println("Quiz has been created successfully");
		} catch (CreateFailedException e) {
			System.out.println("The creation was not successful"+e.getMessage());
			e.printStackTrace();
		}
		}
		   else
		    {
			System.out.println("This option cannot be accessed by students please select Option 7 to Give Exam, Option 8 to Export the Quiz or q to Quit");
		    }
	}

	private static String displayMenu(Scanner scanner) {
		String answer;
		System.out.println("-- Menu --");
		System.out.println("1. Create Quiz");
		System.out.println("2. Create Question");	
		System.out.println("3. Update Question");
		System.out.println("4. Delete Question");
		System.out.println("5. Search based on the Question topic");
		System.out.println("6. Create MCQ Choices");
        System.out.println("7. Give Exam");
        System.out.println("8. Export list of Quiz");
		System.out.println("q. Quit the application");
		
		if((Authentication.studentFlag.contentEquals("A")))
		{
		System.out.println("What is your choice ? (1,2,3,4,5,6,8,q) :");
		}
		else
		{
			System.out.println("What is your choice ? (7,8,q) :");
		}
		
		answer = scanner.nextLine();
		return answer;
	}

	private static boolean authenticate(Scanner scanner) throws SQLException {
		
		System.out.println("Login:");
		System.out.println("Type of login (S - Student or A - Admin/Teacher login) ");
		String StudFlag = scanner.nextLine();
		
		Authentication.studentFlag = StudFlag;		
		
		System.out.println("Please enter your Login ID: ");
		String login = scanner.nextLine();
		
		System.out.println("Please enter your Password : ");
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


