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

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ReportService {
	private ReportRepository reportRepo;

	@Autowired
	public ReportService(ReportRepository reportRepo) {
		log.debug("starts");
		this.reportRepo = reportRepo;
	}

	public List<Report> findAll() {
		log.debug("starts");
		return Optional.ofNullable(reportRepo.findAll()).orElse(new ArrayList<Report>());
	}

	public List<Report> findAllProxy() {
		log.debug("starts");
		List<Report> reports = Optional.ofNullable(reportRepo.findAll()).orElse(new ArrayList<Report>());
		return setSpeakerReportsToNull(reports);
	}

	public Iterable<Report> findFreeOfEvent() {
		log.debug("starts");
		return Optional.ofNullable(reportRepo.findFreeOfEvent()).orElse(new ArrayList<Report>());
	}

	public List<Report> findAcceptedFreeOfEvent() {
		log.debug("starts");
		List<Report> reports = Optional.ofNullable(reportRepo.findAcceptedFreeOfEvent()).orElse(new ArrayList<Report>());
		return setSpeakerReportsToNull(reports);
	}

	@Transactional
	public void setSpeakerAccepted(Report report) {
		log.debug("starts");
		reportRepo.setAccepted(report.isAccepted(), report.getLocalTopic());
		if (report.getSpeaker() != null) {
			reportRepo.setSpeaker(report.getSpeaker().getId(), report.getLocalTopic());
		} else {
			reportRepo.setSpeakerToNull(report.getLocalTopic());
		}

	}

	public Report save(Report report) {
		log.debug("starts");
		return reportRepo.save(report);
	}

	@Transactional
	public Report update(Report updatedReport) {
		log.debug("starts");
		Report report = reportRepo.findByTopic(updatedReport.getLocalTopic());
		report.setAccepted(updatedReport.isAccepted());
		report.setSpeaker(updatedReport.getSpeaker());
		return reportRepo.save(report);
	}

	public void setAcceptedFor(Set<Report> reports) {
		log.debug("starts");
		if (reports != null) {
			List<Long> topics = reports.stream().map(Report::getId).collect(Collectors.toList());
			reportRepo.setAccepted(topics);
		}
	}

	public void clearSpeaker(Set<Report> reports) {
		log.debug("starts");
		if (reports != null) {
			List<Long> reportId = reports.stream().map(Report::getId).collect(Collectors.toList());
			reportRepo.setSpeakerToNull(reportId);
		}
	}

	public List<Report> setSpeakerReportsToNull(List<Report> reports) {
		log.debug("starts");
		reports.forEach(report -> {
			report.setLocalTopic();
			if (report.getSpeaker() != null) {
				report.getSpeaker().setLocalName();
				report.getSpeaker().setReports(null);
			}
		});
		return reports;
	}
}
