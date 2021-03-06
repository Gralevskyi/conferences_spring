package com.hralievskyi.conferences.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.hralievskyi.conferences.entity.Report;

@Component
public class ReportConverter implements Converter<String, Report> {
	@Override
	public Report convert(String id) {
		return Report.builder().id(Long.parseLong(id)).build();
	}
}
