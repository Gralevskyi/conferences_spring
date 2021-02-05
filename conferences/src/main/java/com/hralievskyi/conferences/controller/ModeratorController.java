package com.hralievskyi.conferences.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hralievskyi.conferences.dto.EventDto;
import com.hralievskyi.conferences.dto.ReportDto;
import com.hralievskyi.conferences.entity.Event;
import com.hralievskyi.conferences.entity.Report;
import com.hralievskyi.conferences.service.EventService;
import com.hralievskyi.conferences.service.ModeratorService;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {
	private ModeratorService moderatorService;
	private EventService eventService;

	@Autowired
	public ModeratorController(ModeratorService moderatorService, EventService eventService) {
		this.eventService = eventService;
		this.moderatorService = moderatorService;
	}

	@GetMapping("/event/create")
	public String createevent(Model model) {
		model.addAttribute("eventDto", new EventDto());
		return "createevent";
	}

	@PostMapping("/event/create")
	public String createEvent(Model model, @Valid EventDto eventDto, Errors errors) {
		if (errors.hasErrors()) {
			model.addAttribute("eventDto", eventDto);
			return "createevent";
		}
		moderatorService.createEvent(eventDto.toEvent());
		return "redirect:/";
	}

	@GetMapping("/report/create")
	public String createReport(Model model) {
		model.addAttribute("reportDto", new ReportDto());
		model.addAttribute("speakers", moderatorService.getAllSpeakers());
		return "createreport";
	}

	@PostMapping("/report/create")
	public String createEvent(Model model, @Valid ReportDto reportDto, Errors errors) {
		if (errors.hasErrors()) {
			model.addAttribute("reportDto", reportDto);
			return "createreport";
		}
		moderatorService.createReport(reportDto.toReport());
		return "redirect:/";
	}

	@GetMapping("/update/event/{id}")
	public String getUpdateEventForm(Model model, @PathVariable(value = "id") long eventid) {
		Optional<Event> event = eventService.findByIdWithTopics(eventid);
		if (event.isPresent()) {
			model.addAttribute("event", event.get());
		}
		return "eventupdate";
	}

	@PostMapping("/update/event/{id}")
	public String updateEvent(Model model, @PathVariable(value = "id") long eventid) {
		Optional<Event> event = eventService.findByIdWithTopics(eventid);
		if (event.isPresent()) {
			model.addAttribute("event", event.get());
		}
		return "eventupdate";
	}

	@GetMapping("/addreports/event/{id}")
	public String getReportsForAdding(Model model, @PathVariable(value = "id") long eventid) {
		Event event = new Event(eventid);
		Iterable<Report> reports = moderatorService.findFreeEventReports();
		model.addAttribute("event", event);
		model.addAttribute("reports", reports);
		return "addreports";
	}

	@PostMapping("/addreports/event/{id}")
	public String addReportsTo(Model model, Event event) {
		eventService.addNewReports(event);
		return "redirect:/";
	}

}