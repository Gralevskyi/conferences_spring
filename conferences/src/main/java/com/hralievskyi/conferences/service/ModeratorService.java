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

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
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
		log.debug("starts");
		return Optional.ofNullable(eventRepo.save(event)).orElse(new Event());
	}

	public boolean createSpeaker(User user, String name_en, String name_uk) {
		log.debug("starts");
		Set<String> roles = new HashSet<>();
		user.getRoles().forEach(role -> roles.add(role.getName()));
		if (roles.contains(Roles.ROLE_SPEAKER.toString())) {
			speakerRepo.save(Speaker.builder().id(user.getId()).nameEn(name_en).nameUk(name_uk).build());
			log.debug("speaker is created");
			return true;
		}
		log.debug("speker is not created");
		return false;
	}

	public Report createReport(Report report) {
		log.debug("starts");
		return reportRepo.save(report);
	}

	@Transactional
	public Report suggestTopic(String topicname, String speakername) {
		log.debug("starts");
		Speaker speaker = speakerRepo.findByName(speakername);
		Report report = reportRepo.findByTopic(topicname);
		report.setSpeaker(speaker);
		report.setAccepted(false);
		return reportRepo.save(report);
	}

}
