package com.hralievskyi.conferences.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.hralievskyi.conferences.entity.user.User;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;

	@NotNull
	@Size(min = 5, message = "Event name must be at least 5 characters long")
	String name;

	@NotBlank(message = "Place is required")
	String place;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate date;

	@DateTimeFormat(pattern = "HH:mm")
	LocalTime time;

	@OneToMany
	@JoinTable(name = "event_reports", joinColumns = { @JoinColumn(name = "event_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "report_topic", referencedColumnName = "topic") })
	List<Report> reports;

	@Transient
	@ManyToMany(mappedBy = "roles")
	private List<User> users;

	public Event(String name) {
		this.name = name;
	}

	// need for EventDto
	public Event(String name, String place, LocalDate date, LocalTime time) {
		this.name = name;
		this.place = place;
		this.date = date;
		this.time = time;
	}

	// need for adding reports
	public Event(long id) {
		this.id = id;
	}

	public List<Report> addNewReports(List<Report> newReports) {
		this.reports.addAll(newReports);
		return this.reports;
	}
}
