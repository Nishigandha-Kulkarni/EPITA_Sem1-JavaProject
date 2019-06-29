package fr.epita.quiz.datamodel;

public class Question extends Answer {
	private int QUESTION_ID;
	private String Questions_TEXT;
	private String Topic;
	private int Difficulty;
	
	public Question(String ques, String topic, int diff) {
		this.Questions_TEXT = ques;
		this.Topic = topic;
		this.Difficulty = diff;
	}
	
	public Question(String topic) {
		this.Topic = topic;
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

}
