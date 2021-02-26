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
	@Size(min = 5, max = 50, message = "Name must be at least 5 characters long")
	private String topicEn;

	@NotNull
	@Size(min = 5, max = 50, message = "Name must be at least 5 characters long")
	private String topicUk;

	private boolean accepted;

	private String creator;

	@Nullable
	private long speakerId;

	public ReportDto(String author) {
		this.creator = author;
	}

	public Report toReport() {
		Speaker speaker = Speaker.builder().build();
		speaker.setId(speakerId);
		return Report.builder().topicEn(this.topicEn).topicUk(this.topicUk).creator(this.creator).accepted(this.accepted).speaker(speaker).build();
	}

	public Report toReport(long id) {
		Speaker newSpeaker = null;
		if (id != -1) {
			newSpeaker = Speaker.builder().id(id).build();
		}
		return Report.builder().topicEn(this.topicEn).topicUk(this.topicUk).creator(this.creator).accepted(this.accepted).speaker(newSpeaker).build();
	}

}
