package com.hralievskyi.conferences.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hralievskyi.conferences.dto.EventCreateDto;
import com.hralievskyi.conferences.dto.ReportDto;
import com.hralievskyi.conferences.entity.Event;
import com.hralievskyi.conferences.service.EventService;
import com.hralievskyi.conferences.service.ModeratorService;
import com.hralievskyi.conferences.service.ReportService;
import com.hralievskyi.conferences.service.SpeakerService;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/moderator")
@Log4j2
public class ModeratorController {
	private ModeratorService moderatorService;
	private EventService eventService;
	private SpeakerService speakerService;
	private ReportService reportService;

	@Autowired
	public ModeratorController(ModeratorService moderatorService, EventService eventService, SpeakerService speakerService, ReportService reportService) {
		this.eventService = eventService;
		this.moderatorService = moderatorService;
		this.speakerService = speakerService;
		this.reportService = reportService;
	}

	@GetMapping("/event/create")
	public String createevent(Model model) {
		model.addAttribute("eventDto", new EventCreateDto());
		return "event_create";
	}

	@PostMapping("/event/create")
	public String createEvent(Model model, @Valid EventCreateDto eventCreateDto, Errors errors) {
		if (errors.hasErrors()) {
			log.info("dto nameEn: " + eventCreateDto.getNameEn());
			log.info("errors on event dto: " + errors);
			model.addAttribute("eventDto", eventCreateDto);
			return "event_create";
		}
		moderatorService.createEvent(eventCreateDto.toEvent());
		log.debug("finish");
		return "redirect:/";
	}

	@GetMapping("/report/create")
	public String createReport(Model model, Principal principal) {
		model.addAttribute("reportDto", new ReportDto(principal.getName()));
		model.addAttribute("speakers", speakerService.getAllProxy());
		return "report_create";
	}

	@PostMapping("/report/create")
	public String createReport(Model model, @Valid ReportDto reportDto, Errors errors) {
		if (errors.hasErrors()) {
			log.info("errors on report dto: " + errors);
			model.addAttribute("reportDto", reportDto);
			model.addAttribute("speakers", speakerService.getAllProxy());
			return "report_create";
		}
		moderatorService.createReport(reportDto.toReport());
		return "redirect:/";
	}

	@GetMapping("/update/event/{id}")
	public String getUpdateEventForm(Model model, @PathVariable(value = "id") long eventid) {
		model.addAttribute("event", eventService.findByIdWithTopics(eventid));
		return "event_update";
	}

	@PostMapping("/update/event/{id}")
	public String updateEvent(Model model, @PathVariable(value = "id") long eventid, Event event) {
		eventService.update(event);
		return "redirect:/moderator/update/event/" + eventid;
	}

	@GetMapping("/addreports/event/{id}")
	public String getReportsForAdding(Model model, @PathVariable(value = "id") long eventid) {
		Event event = Event.builder().id(eventid).build();
		model.addAttribute("event", event);
		model.addAttribute("reports", reportService.findAcceptedFreeOfEvent());
		return "add_reports";
	}

	@PostMapping("/addreports/event/{id}")
	public String addReportsToEvent(Event event, @PathVariable(value = "id") long eventid) {
		eventService.addNewReports(event);
		return "redirect:/";
	}

	@GetMapping("/reports")
	public String getAllReports(Model model) {
		model.addAttribute("reports", reportService.findAllProxy());
		return "reports";
	}

	@GetMapping("/pastevents")
	public String getMainpage(Model model) {
		List<Event> events = eventService.findPast();
		model.addAttribute("events", events);
		return "past_events";
	}

}
