/**
 * 
 */
package fr.epita.quiz.datamodel;

/**
 * @author Nishigandha kulkarni
 *
 */
public class MCQChoice {
	
	private int questionId;	
	private int answerId;
	private String answerText;
	private Boolean valid;
	
	public MCQChoice() {
		
	}
	public MCQChoice(int queId, int ansId, String ansText, boolean valid2) {
		this.questionId = queId;
		this.answerId = ansId;
		this.answerText = ansText;
		this.valid = valid2;
	}
	public MCQChoice(int question_ID) {
		this.questionId = question_ID;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getAnswerId() {
		return answerId;
	}
	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}
	public String getAnswerText() {
		return answerText;
	}
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	
	@Override
	public String toString() {
       return answerText;
	}
	
}
