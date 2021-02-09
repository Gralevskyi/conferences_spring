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

@Service
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
		return Optional.ofNullable(eventRepo.findAll()).orElse(new ArrayList<Event>());
	}

	public List<Event> findPast() {
		List<Event> events = Optional.ofNullable(eventRepo.findPast()).orElse(new ArrayList<Event>());
		events.forEach(event -> event.setLocales());
		return events;
	}

	public List<Event> findPastBySpeakerName(String speakername) {
		List<Event> events = Optional.ofNullable(eventRepo.findPastBySpeakerName(speakername)).orElse(new ArrayList<Event>());
		events.forEach(event -> event.setLocales());
		return events;
	}

	public Page<Event> findFuturePaginated(Pageable pageable) {
		return Optional.ofNullable(eventRepo.findFuture(pageable)).orElse(Page.empty());
	}

	public List<Event> findByUsername(String username) {
		List<Event> events = eventRepo.findByUsername(username);
		if (events != null) {
			events.forEach(event -> event.setLocales());
			return events;
		}
		return new ArrayList<Event>();
	}

	@Transactional
	public Event findByIdWithTopics(long id) {
		Optional<Event> event = eventRepo.findById(id);
		if (event.isPresent()) {
			Iterable<Report> itrReports = reportRepo.findByEventid(id);
			List<Report> reports = StreamSupport.stream(itrReports.spliterator(), false).collect(Collectors.toList());
			event.get().setReports(reports);
			event.get().setLocales();
		}
		return event.orElse(Event.builder().build());
	}

	@Transactional
	public boolean update(Event event) {
		Optional<Event> currentEvent = eventRepo.findById(event.getId());
		if (currentEvent.isPresent()) {
			currentEvent.get().updateFrom(event);
			eventRepo.save(currentEvent.get());
		}
		return false;
	}

	@Transactional
	public Optional<Event> addNewReports(Event reportsContainer) {
		Optional<Event> event = eventRepo.findById(reportsContainer.getId());
		if (event.isPresent()) {
			event.get().addNewReports(reportsContainer.getReports());
			eventRepo.save(event.get());
		}
		return event;
	}

	@Transactional
	public User subscribeUser(String username, final long eventid) throws AlreadySubscribed {
		User user = userRepo.findByUsername(username);
		boolean alreadySubsribed = Optional.ofNullable(user.getEvents()).map(Collection::stream).orElseGet(Stream::empty).anyMatch(event -> event.getId() == eventid);
		if (alreadySubsribed) {
			throw new AlreadySubscribed("You have subscribed earlie. You can not do it several times");
		}
		user.addEvent(Event.builder().id(eventid).build());
		eventRepo.increseSubscrUsers(eventid);
		return userRepo.save(user);
	}

}
