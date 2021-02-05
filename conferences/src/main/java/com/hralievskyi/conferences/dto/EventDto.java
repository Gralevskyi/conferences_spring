package com.hralievskyi.conferences.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.hralievskyi.conferences.entity.Event;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EventDto {

	@NotNull
	@Size(min = 5, message = "Event name must be at least 5 characters long")
	String name;

	@NotBlank(message = "Place is required")
	String place;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate date;

	@DateTimeFormat(pattern = "HH:mm")
	LocalTime time;

	public Event toEvent() {
		return new Event(name, place, date, time);
	}

}
