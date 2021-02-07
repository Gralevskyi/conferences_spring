package com.hralievskyi.conferences.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hralievskyi.conferences.entity.Event;
import com.hralievskyi.conferences.entity.Report;
import com.hralievskyi.conferences.entity.user.User;
import com.hralievskyi.conferences.repository.EventRepository;
import com.hralievskyi.conferences.repository.ReportRepository;
import com.hralievskyi.conferences.repository.UserRepository;

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
		return eventRepo.findAll();
	}

	public List<Event> findFuture() {
		return eventRepo.findFuture();
	}

	public List<Event> findPast() {
		return eventRepo.findPast();
	}

	public List<Event> findPastBySpeakerName(String speakername) {
		return eventRepo.findPastBySpeakerName(speakername);
	}

	public Page<Event> findPaginated(Pageable pageable) {
		List<Event> events = eventRepo.findFuture();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Event> list;

		if (events.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, events.size());
			list = events.subList(startItem, toIndex);
		}

		Page<Event> eventPage = new PageImpl<Event>(list, PageRequest.of(currentPage, pageSize), events.size());

		return eventPage;
	}

	@Transactional
	public Optional<Event> findByIdWithTopics(long id) {
		Optional<Event> event = eventRepo.findById(id);
		if (event.isPresent()) {
			Iterable<Report> itrTopics = reportRepo.findByEventid(id);
			List<Report> reports = StreamSupport.stream(itrTopics.spliterator(), false).collect(Collectors.toList());
			event.get().setReports(reports);
		}
		return event;
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

	public User subscribeUser(String username, long eventid) {
		User user = userRepo.findByUsername(username);
		user.addEvent(new Event(eventid));
		return userRepo.save(user);
	}

}
