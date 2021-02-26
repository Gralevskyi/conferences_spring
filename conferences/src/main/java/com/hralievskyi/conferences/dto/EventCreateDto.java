package com.hralievskyi.conferences.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.Future;
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

public class EventCreateDto {

	@NotNull
	@Size(min = 5, message = "Event name must be at least 5 characters long")
	String nameEn;

	@NotNull
	@Size(min = 5, message = "Event name must be at least 5 characters long")
	String nameUk;

	@NotBlank(message = "Place is required")
	String placeEn;

	@NotBlank(message = "Place is required")
	String placeUk;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Future
	LocalDate date;

	@DateTimeFormat(pattern = "HH:mm")
	LocalTime time;

	public Event toEvent() {
		return Event.builder().nameEn(this.nameEn).nameUk(this.nameUk).placeEn(this.placeEn).placeUk(this.placeUk).date(this.date).time(this.time).build();
	}

}
