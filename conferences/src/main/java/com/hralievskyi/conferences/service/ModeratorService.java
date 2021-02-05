package com.hralievskyi.conferences.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
		return eventRepo.save(event);
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

	public Iterable<Speaker> getAllSpeakers() {
		return speakerRepo.findAll();
	}

	@Transactional
	public boolean appointSpeaker(String speakername, String topicname) {
		Speaker speaker = speakerRepo.findByName(speakername);
		Report report = reportRepo.findByTopic(topicname);
		report.setSpeaker(speaker);
		report.setAccepted(true);
		reportRepo.save(report);
		return true;
	}

	@Transactional
	public boolean suggestTopic(String topicname, String speakername) {
		Speaker speaker = speakerRepo.findByName(speakername);
		Report report = reportRepo.findByTopic(topicname);
		report.setSpeaker(speaker);
		report.setAccepted(false);
		reportRepo.save(report);
		return true;
	}

	public Iterable<Report> findFreeEventReports() {
		return reportRepo.findAllFreeEventReports();
	}

	public boolean changeEventTimeTo(Date datetime) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean changeEventPlaceTo(String placename) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean fetchEventsFromTo(Date from, Date to) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Event> sortEventByDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Event> sortEventByTopicNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Event> sortEventByMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	public int eventRegistration(Event event) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int eventMembers(Event event) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean sendMessageEventChanges(Event oldVersion, Event newVersion) {
		// TODO Auto-generated method stub
		return false;
	}

}
