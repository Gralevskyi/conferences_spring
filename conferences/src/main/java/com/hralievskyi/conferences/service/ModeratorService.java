package com.hralievskyi.conferences.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hralievskyi.conferences.entity.Event;
import com.hralievskyi.conferences.entity.Report;
import com.hralievskyi.conferences.entity.user.Roles;
import com.hralievskyi.conferences.entity.user.Speaker;
import com.hralievskyi.conferences.entity.user.User;
import com.hralievskyi.conferences.repository.EventRepository;
import com.hralievskyi.conferences.repository.ReportRepository;
import com.hralievskyi.conferences.repository.SpeakerRepository;

@Service
public class ModeratorService {
	private SpeakerRepository speakerRepo;
	private EventRepository eventRepo;
	private ReportRepository reportRepo;

	@Autowired
	public ModeratorService(SpeakerRepository speakerRepo, EventRepository eventRepo, ReportRepository reportRepo) {
		this.speakerRepo = speakerRepo;
		this.eventRepo = eventRepo;
		this.reportRepo = reportRepo;
	}

	public Event createEvent(Event event) {
		return Optional.ofNullable(eventRepo.save(event)).orElse(new Event());
	}

	public boolean createSpeaker(User user) {
		Set<String> roles = new HashSet<>();
		user.getRoles().forEach(role -> roles.add(role.getName()));
		if (roles.contains(Roles.ROLE_SPEAKER.toString())) {
			speakerRepo.save(new Speaker(user.getId(), user.getUsername()));
			return true;
		}
		return false;
	}

	@Transactional
	public Report createReport(Report report) {
		if (report.getSpeaker() != null) {
			Speaker speaker = speakerRepo.findByName(report.getSpeaker().getName());
			report.setSpeaker(speaker);
		}
		return reportRepo.save(report);
	}

	@Transactional
	public Report suggestTopic(String topicname, String speakername) {
		Speaker speaker = speakerRepo.findByName(speakername);
		Report report = reportRepo.findByTopic(topicname);
		report.setSpeaker(speaker);
		report.setAccepted(false);
		return reportRepo.save(report);
	}

}
