package com.hralievskyi.conferences.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
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

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.annotation.DateTimeFormat;

import com.hralievskyi.conferences.entity.user.User;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	@Size(min = 5, message = "{event.create.name}")
	@Column(name = "name_en")
	private String nameEn;

	@NotNull
	@Size(min = 5, message = "{event.create.name}")
	@Column(name = "name_uk")
	private String nameUk;

	@NotBlank(message = "{event.create.place}")
	@Column(name = "place_en")
	private String placeEn;

	@NotBlank(message = "{event.create.place}")
	@Column(name = "place_uk")
	private String placeUk;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "date", columnDefinition = "DATE")
	private LocalDate date;

	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "time", columnDefinition = "TIME")
	private LocalTime time;

	@OneToMany
	@JoinTable(name = "event_reports", joinColumns = { @JoinColumn(name = "event_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "report_id", referencedColumnName = "id") })
	private List<Report> reports;

	@Transient
	private String localName;

	@Transient
	private String localPlace;

	@Transient
	@ManyToMany(mappedBy = "roles")
	private List<User> users;

	@Column(name = "subscr_users")
	private long subscrUsers;

	@Column(name = "visited_users")
	private long visitedUsers;

	public List<Report> addNewReports(List<Report> newReports) {
		this.reports.addAll(newReports);
		return this.reports;
	}

	public void setLocalName() {
		Locale locales = LocaleContextHolder.getLocale();
		if ("uk".equals(locales.toString())) {
			localName = nameUk;
		} else {
			localName = nameEn;
		}
	}

	public void setLocalPlace() {
		Locale locales = LocaleContextHolder.getLocale();
		if ("uk".equals(locales.toString())) {
			localPlace = placeUk;
		} else {
			localPlace = placeEn;
		}
	}

	public void setLocales() {
		setLocalName();
		setLocalPlace();
		if (reports != null) {
			reports.forEach(report -> report.setLocalTopic());
		}
	}

	public void updateFrom(Event event) {
		this.nameEn = event.getNameEn();
		this.nameUk = event.getNameUk();
		this.placeEn = event.getPlaceEn();
		this.placeUk = event.getPlaceUk();
		this.date = event.getDate();
		this.time = event.getTime();
	}
}
