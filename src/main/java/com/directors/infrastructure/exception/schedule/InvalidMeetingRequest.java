package com.directors.infrastructure.exception.schedule;

import com.directors.infrastructure.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class InvalidMeetingRequest extends RuntimeException {
	private String userId;
	private String startTime;
	private String message;
	private HttpStatus statusCode;

	public InvalidMeetingRequest() {
	}

	public InvalidMeetingRequest(ExceptionCode errorCode, LocalDateTime startTime, String userId) {
		super(errorCode.getMessage());
		this.userId = userId;
		this.startTime = startTime.toString();
		this.message = errorCode.getMessage();
		this.statusCode = errorCode.getStatus();
	}
}
