package com.hralievskyi.conferences.service;

import java.util.List;
import java.util.Set;

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
		return reportRepo.findAll();
	}

	public Iterable<Report> findFreeOfEvent() {
		return reportRepo.findFreeOfEvent();
	}

	public Iterable<Report> findFreeOfSpeaker() {
		return reportRepo.findFreeOfSpeaker();
	}

	public Iterable<Report> findAcceptedFreeOfEvent() {
		return reportRepo.findAcceptedFreeOfEvent();
	}

	public Report save(Report report) {
		return reportRepo.save(report);
	}

	public Report update(Report updatedReport) {
		Report report = reportRepo.findByTopic(updatedReport.getTopic());
		report.setAccepted(updatedReport.isAccepted());
		report.setSpeaker(updatedReport.getSpeaker());
		return report;
	}

	public List<Report> saveAll(Set<Report> reports) {
		List<Report> reprts = reportRepo.saveAll(reports);
		return reprts;
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
