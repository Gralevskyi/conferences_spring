package com.hralievskyi.conferences.controller;

import java.security.Principal;
import java.util.List;
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
import com.hralievskyi.conferences.entity.user.Speaker;
import com.hralievskyi.conferences.service.EventService;
import com.hralievskyi.conferences.service.ModeratorService;
import com.hralievskyi.conferences.service.ReportService;
import com.hralievskyi.conferences.service.SpeakerService;

@Controller
@RequestMapping("/moderator")
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
	public String createReport(Model model, Principal principal) {
		model.addAttribute("reportDto", new ReportDto(principal.getName()));
		model.addAttribute("speakers", speakerService.getAllDto());
		return "createreport";
	}

	@PostMapping("/report/create")
	public String createReport(Model model, @Valid ReportDto reportDto, Errors errors) {
		if (errors.hasErrors()) {
			model.addAttribute("reportDto", reportDto);
			model.addAttribute("speakers", speakerService.getAllDto());
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
		return "redirect:/update/event/" + eventid;
	}

	@GetMapping("/addreports/event/{id}")
	public String getReportsForAdding(Model model, @PathVariable(value = "id") long eventid) {
		Event event = new Event(eventid);
		model.addAttribute("event", event);
		model.addAttribute("reports", reportService.setSpeakerReportsToNull(reportService.findAcceptedFreeOfEvent()));
		return "addreports";
	}

	@PostMapping("/addreports/event/{id}")
	public String addReportsToEvent(Event event, @PathVariable(value = "id") long eventid) {
		eventService.addNewReports(event);
		return "redirect:/moderator/addreports/event/" + eventid;
	}

	@GetMapping("/speakers")
	public String getAllSpeakers(Model model) {
		model.addAttribute("speakers", speakerService.getAll());
		return "speakers";
	}

	@GetMapping("/addreports/speaker/{speakername}")
	public String speakerAddReports(Model model, @PathVariable(value = "speakername") String speakername) {
		Iterable<Report> reports = reportService.findFreeOfSpeaker();
		Speaker speaker = new Speaker(speakername);
		model.addAttribute("speaker", speaker);
		model.addAttribute("reports", reports);
		return "add_rep_speaker";
	}

	@PostMapping("/addreports/speaker/{name}")
	public String addReportsToSpeaker(Model model, Speaker speaker) {
		speakerService.addNewReports(speaker);
		return "redirect:/speakers";
	}

	@GetMapping("/reports")
	public String getAllReports(Model model) {
		model.addAttribute("reports", reportService.setSpeakerReportsToNull(reportService.findAll()));
		return "reports";
	}

	@GetMapping("/pastevents")
	public String getMainpage(Model model) {
		List<Event> events = eventService.findPast();
		model.addAttribute("events", events);
		return "past_events";
	}

}
