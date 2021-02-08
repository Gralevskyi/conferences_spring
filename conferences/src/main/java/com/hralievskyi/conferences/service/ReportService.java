package com.hralievskyi.conferences.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hralievskyi.conferences.entity.Report;
import com.hralievskyi.conferences.repository.ReportRepository;

@Service
public class ReportService {
	private ReportRepository reportRepo;

	@Autowired
	public ReportService(ReportRepository reportRepo) {
		this.reportRepo = reportRepo;
	}

	public List<Report> findAll() {
		return Optional.ofNullable(reportRepo.findAll()).orElse(new ArrayList<Report>());
	}

	public Iterable<Report> findFreeOfEvent() {
		return Optional.ofNullable(reportRepo.findFreeOfEvent()).orElse(new ArrayList<Report>());
	}

	public Iterable<Report> findFreeOfSpeaker() {
		return Optional.ofNullable(reportRepo.findFreeOfSpeaker()).orElse(new ArrayList<Report>());
	}

	public Iterable<Report> findAcceptedFreeOfEvent() {
		return Optional.ofNullable(reportRepo.findAcceptedFreeOfEvent()).orElse(new ArrayList<Report>());
	}

	public Report save(Report report) {
		return reportRepo.save(report);
	}

	@Transactional
	public Report update(Report updatedReport) {
		Report report = reportRepo.findByTopic(updatedReport.getTopic());
		report.setAccepted(updatedReport.isAccepted());
		report.setSpeaker(updatedReport.getSpeaker());
		return reportRepo.save(report);
	}

	public void setAccetedFor(Set<Report> reports) {
		reports.forEach(report -> System.err.println(report.getTopic() + " " + report.isAccepted()));
		List<String> topics = reports.stream().map(Report::getTopic).collect(Collectors.toList());
		reportRepo.setAccepted(topics);
	}

	public void clearSpeaker(Set<Report> reports) {
		reports.forEach(report -> report.setSpeaker(null));
		reportRepo.saveAll(reports);
	}

	public Iterable<Report> setSpeakerReportsToNull(Iterable<Report> reports) {
		reports.forEach(report -> {
			if (report.getSpeaker() != null)
				report.getSpeaker().setReports(null);
		});
		return reports;
	}
}
