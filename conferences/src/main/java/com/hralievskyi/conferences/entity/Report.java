package com.hralievskyi.conferences.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.hralievskyi.conferences.entity.user.Speaker;
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
@Table(name = "reports")
public class Report {
	@Id
	@Column(unique = true)

	@NotNull
	@Size(min = 5, max = 50, message = "Report topic name must be at least 5 and maximum 50 characters long")
	private String topic;

	private boolean accepted;

	@ManyToOne
	@JoinColumn(name = "speaker")
	private Speaker speaker;

	public Report(String topic) {
		this.topic = topic;
	}

	public Report(String topic, boolean accepted, String speaker) {
		this.topic = topic;
		this.accepted = accepted;
		this.speaker = new Speaker(speaker);
	}

}
