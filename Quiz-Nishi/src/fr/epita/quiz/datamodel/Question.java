package fr.epita.quiz.datamodel;

public class Question extends Answer {
	private int QUESTION_ID;
	private String Questions_TEXT;
	private String Topic;
	private int Difficulty;
	private String questionType;
	private String hint;
	private int quizID;
	
	public Question(String ques, String topic, int diff, String qType, int quizID1, String hint1) {
		this.Questions_TEXT = ques;
		this.Topic = topic;
		this.Difficulty = diff;
		this.questionType = qType;
		this.quizID = quizID1;
		this.hint = hint1;
	}
	
	public Question(String topic) {
		this.Topic = topic;
	}
	
	public Question(int quizID1) {
		this.quizID = quizID1;
	}
	
	public String getQuestions_TEXT() {
		return Questions_TEXT;
	}
	public void setQuestions_TEXT(String questions) {
		Questions_TEXT = questions;
	}
	public String getTopics() {
		return Topic;
	}
	public void setTopics(String topics) {
		Topic = topics;
	}
	public int getDifficulty() {
		return Difficulty;
	}
	public void setDifficulty(int difficulty) {
		Difficulty = difficulty;
	}
	
	@Override
	public String toString() {
//		return "Question [Question =" + Questions_TEXT + "]";
		return Questions_TEXT;

	}

	public int getQUESTION_ID() {
		return QUESTION_ID;
	}

	public void setQUESTION_ID(int qUESTION_ID) {
		QUESTION_ID = qUESTION_ID;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint1) {
		hint = hint1;
	}

	public int getQuizID() {
		return quizID;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

}
