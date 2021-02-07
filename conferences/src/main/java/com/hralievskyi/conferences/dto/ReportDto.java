package com.hralievskyi.conferences.dto;

import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import com.hralievskyi.conferences.entity.Report;
import com.hralievskyi.conferences.entity.user.Speaker;
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

public class ReportDto {
	@NotNull
	@Size(min = 5, max = 50, message = "Report topic must be at least 5 and maximum 50 characters long")
	private String topic;

	private boolean accepted;

	private String creator;

	@Nullable
	private String speaker;

	public ReportDto(String author) {
		this.creator = author;
	}

	public Report toReport() {
		Speaker newSpeaker = new Speaker(speaker);
		return new Report(topic, creator, accepted, newSpeaker);
	}

	public Report toReport(long id) {
		Speaker newSpeaker = null;
		if (id != -1) {
			newSpeaker = new Speaker(id);
		}
		return new Report(topic, creator, accepted, newSpeaker);
	}

}
