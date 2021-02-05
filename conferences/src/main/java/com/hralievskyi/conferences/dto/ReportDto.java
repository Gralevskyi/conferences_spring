package com.hralievskyi.conferences.dto;

import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import com.hralievskyi.conferences.entity.Report;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ReportDto {
	@NotNull
	@Size(min = 5, max = 50, message = "Report name must be at least 5 and maximum 50 characters long")
	private String name;

	private boolean accepted;

	@Nullable
	private String speaker;

	public Report toReport() {
		return new Report(name, accepted, speaker);
	}
}
