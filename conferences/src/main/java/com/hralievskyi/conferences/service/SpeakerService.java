package com.hralievskyi.conferences.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hralievskyi.conferences.entity.Report;
import com.hralievskyi.conferences.entity.user.Speaker;
import com.hralievskyi.conferences.entity.user.User;
import com.hralievskyi.conferences.repository.ReportRepository;
import com.hralievskyi.conferences.repository.SpeakerRepository;
import com.hralievskyi.conferences.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SpeakerService {
	private SpeakerRepository speakerRepo;
	private UserRepository userRepo;
	private ReportRepository reportRepo;

	@Autowired
	public SpeakerService(SpeakerRepository speakerRepo, UserRepository userRepo, ReportRepository reportRepo) {
		this.speakerRepo = speakerRepo;
		this.userRepo = userRepo;
		this.reportRepo = reportRepo;
	}

	public Iterable<Speaker> getAll() {
		log.debug("starts");
		return Optional.ofNullable(speakerRepo.findAll()).orElse(new ArrayList<Speaker>());
	}

	public Iterable<Speaker> getAllProxy() {
		log.debug("starts");
		Iterable<Speaker> speakers = Optional.ofNullable(speakerRepo.findAll()).orElse(new ArrayList<Speaker>());
		speakers.forEach(speaker -> {
			speaker.setLocalName();
			if (speaker.getReports() != null)
				speaker.setReports(null);
		});
		return speakers;
	}

	public Iterable<Report> getReports(String speakername) {
		log.debug("starts");
		Speaker speaker = speakerRepo.findByUserName(speakername);
		Iterable<Report> reports = Optional.ofNullable(speaker.getReports()).orElse(new HashSet<Report>());
		reports.forEach(report -> report.setLocalTopic());
		return reports;
	}

	@Transactional
	public Report createReport(Report report, String username) {
		log.debug("starts");
		User user = userRepo.findByUsername(username);
		report.setSpeaker(Speaker.builder().id(user.getId()).build());
		return reportRepo.save(report);
	}
}
