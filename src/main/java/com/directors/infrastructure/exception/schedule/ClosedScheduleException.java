package com.directors.infrastructure.exception.schedule;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ClosedScheduleException extends RuntimeException {
	private String userId;
	private String startTime;

	public ClosedScheduleException() {
		super();
	}

	public ClosedScheduleException(LocalDateTime reservedTime, String userId) {
		super(String.format("%s에 이미 예약한 사람이 있습니다.", reservedTime.toString()));
		this.startTime = reservedTime.toString();
		this.userId = userId;
	}

}
