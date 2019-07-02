package fr.epita.quiz.services;

public enum ConfigEntry {
	
	DB_QUERIES_QUIZ_SEARCHQUERY("db.queries.quiz.searchQuery"),
	DB_QUERIES_QUIZ_SEARCHQUERYALL("db.queries.quiz.searchQueryAll"),
	DB_QUERIES_QUESTION_SEARCHQUERY("db.queries.question.searchQuery"),
	DB_QUERIES_MCQCHOICE_SEARCHQUERY("db.queries.mcqchoice.searchQuery"),
	DB_QUERIES_MCQCHOICE_INSERTQUERY("db.queries.mcqchoice.insertquery"),
	DB_QUERIES_QUESTION_UPDATEQUERY("db.queries.question.updatequery"),
	DB_QUERIES_QUESTION_DELETEQUERY("db.queries.question.deletequery"),
	DB_QUERIES_MCQCHOICE_DELETEQUERY("db.queries.mcqchoice.deletequery"),
	DB_URL("db.url"),
	DB_USERNAME("db.username"),
	DB_PASSWORD("db.password"),
;
	private String key;
	
	public String getKey() {
		return key;
	}

	private ConfigEntry(String key) {
		this.key = key;
	}

}
