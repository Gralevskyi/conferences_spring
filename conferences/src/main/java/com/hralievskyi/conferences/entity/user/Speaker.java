package com.hralievskyi.conferences.entity.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hralievskyi.conferences.entity.Report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "speaker")
public class Speaker {

	@Id
	@Column(unique = true)
	private long id;
	private String name;

	@OneToMany(mappedBy = "speaker", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	Set<Report> reports;

	public Speaker(String name) {
		this.name = name;
	}

	public Speaker(long id) {
		this.id = id;
	}

	public Speaker(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Set<Report> addReports(Set<Report> newReports) {
		this.reports.addAll(newReports);
		return this.reports;
	}
}
