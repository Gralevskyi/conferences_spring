package com.hralievskyi.conferences.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hralievskyi.conferences.entity.Event;
import com.hralievskyi.conferences.entity.Report;
import com.hralievskyi.conferences.entity.user.User;
import com.hralievskyi.conferences.repository.EventRepository;
import com.hralievskyi.conferences.repository.ReportRepository;
import com.hralievskyi.conferences.repository.UserRepository;
import com.hralievskyi.conferences.util.AlreadySubscribed;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EventService {
	private EventRepository eventRepo;
	private ReportRepository reportRepo;
	private UserRepository userRepo;

	@Autowired
	public EventService(EventRepository eventRepo, ReportRepository reportRepo, UserRepository userRepo) {
		this.eventRepo = eventRepo;
		this.reportRepo = reportRepo;
		this.userRepo = userRepo;
	}

	public List<Event> findAll() {
		log.debug("starts");
		return Optional.ofNullable(eventRepo.findAll()).orElse(new ArrayList<Event>());
	}

	public List<Event> findPast() {
		log.debug("starts");
		List<Event> events = Optional.ofNullable(eventRepo.findPast()).orElse(new ArrayList<Event>());
		events.forEach(event -> event.setLocales());
		return events;
	}

	public List<Event> findPastBySpeakerName(String speakername) {
		log.debug("starts");
		List<Event> events = Optional.ofNullable(eventRepo.findPastBySpeakerName(speakername)).orElse(new ArrayList<Event>());
		log.debug("obtained events");
		events.forEach(event -> event.setLocales());
		return events;
	}

	public Page<Event> findFuturePaginated(Pageable pageable) {
		return Optional.ofNullable(eventRepo.findFuture(pageable)).orElse(Page.empty());
	}

	public List<Event> findByUsername(String username) {
		log.debug("starts");
		List<Event> events = eventRepo.findByUsername(username);
		if (events != null) {

			events.forEach(event -> event.setLocales());
			return events;
		}
		log.debug("events are nullable");
		return new ArrayList<Event>();
	}

	@Transactional
	public Event findByIdWithTopics(long id) {
		log.debug("starts");
		Optional<Event> event = eventRepo.findById(id);
		log.debug("obtained current event");
		if (event.isPresent()) {
			log.debug("starts get reports");
			Iterable<Report> itrReports = reportRepo.findByEventid(id);
			List<Report> reports = StreamSupport.stream(itrReports.spliterator(), false).collect(Collectors.toList());
			event.get().setReports(reports);
			log.debug("obtained and set reports");
			event.get().setLocales();
		}
		return event.orElse(Event.builder().build());
	}

	@Transactional
	public boolean update(Event event) {
		log.debug("starts");
		Optional<Event> currentEvent = eventRepo.findById(event.getId());
		log.debug("obtained event");
		if (currentEvent.isPresent()) {
			currentEvent.get().updateFrom(event);
			eventRepo.save(currentEvent.get());
			log.debug("update event");
		}
		return false;
	}

	@Transactional
	public Optional<Event> addNewReports(Event reportsContainer) {
		log.debug("starts");
		Optional<Event> event = eventRepo.findById(reportsContainer.getId());
		log.debug("obtained event");
		if (event.isPresent()) {
			event.get().addNewReports(reportsContainer.getReports());
			eventRepo.save(event.get());
			log.debug("update event");
		}
		return event;
	}

	@Transactional
	public User subscribeUser(String username, final long eventid) throws AlreadySubscribed {
		log.debug("starts");
		User user = userRepo.findByUsername(username);
		log.debug("find user " + username);
		boolean alreadySubsribed = Optional.ofNullable(user.getEvents()).map(Collection::stream).orElseGet(Stream::empty).anyMatch(event -> event.getId() == eventid);
		if (alreadySubsribed) {
			log.warn("user " + username + " is already subscribed");
			throw new AlreadySubscribed("You have subscribed earlie. You can not do it several times");
		}
		user.addEvent(Event.builder().id(eventid).build());
		eventRepo.increseSubscrUsers(eventid);
		log.debug("update event (increase subscribed)");
		return userRepo.save(user);
	}

}
