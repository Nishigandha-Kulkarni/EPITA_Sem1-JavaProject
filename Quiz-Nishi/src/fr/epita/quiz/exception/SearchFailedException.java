package fr.epita.quiz.exception;

public class SearchFailedException extends DataAccessException {

	public SearchFailedException(Object badInput) {
		super(badInput);
	}

	public SearchFailedException(Object badInput, Exception initialCause) {
		super(badInput,initialCause);
	}

}
