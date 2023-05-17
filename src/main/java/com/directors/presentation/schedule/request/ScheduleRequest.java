package com.directors.presentation.schedule.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {
	@NotBlank(message = "허용 가능한 스케쥴 시간이 입력되지 않았습니다.")
	private List<LocalDateTime> startTimeList;
}
