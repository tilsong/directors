package com.directors.infrastructure.exception.question;

import com.directors.infrastructure.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class QuestionDuplicateException extends RuntimeException {
	private String message;
	private HttpStatus statusCode;
	private String questionerId;

	public QuestionDuplicateException() {
		super();
	}

	public QuestionDuplicateException(ExceptionCode exceptionCode, String questionerId) {
		super(exceptionCode.getMessage());
		this.message = exceptionCode.getMessage();
		this.statusCode = exceptionCode.getStatus();
		this.questionerId = questionerId;
	}
}
