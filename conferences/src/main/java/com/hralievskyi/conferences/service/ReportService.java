package com.hralievskyi.conferences.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hralievskyi.conferences.entity.Report;
import com.hralievskyi.conferences.repository.ReportRepository;

@Service
public class ReportService {
	private ReportRepository topicRepo;

	@Autowired
	public ReportService(ReportRepository topicRepo) {
		this.topicRepo = topicRepo;
	}

	public List<Report> findAll() {
		return topicRepo.findAll();
	}
}
