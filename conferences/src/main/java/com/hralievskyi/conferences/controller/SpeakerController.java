package com.hralievskyi.conferences.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hralievskyi.conferences.dto.ReportDto;
import com.hralievskyi.conferences.entity.user.Speaker;
import com.hralievskyi.conferences.service.EventService;
import com.hralievskyi.conferences.service.ModeratorService;
import com.hralievskyi.conferences.service.ReportService;
import com.hralievskyi.conferences.service.SpeakerService;

@Controller
@RequestMapping("/speaker")
public class SpeakerController {
	private ModeratorService moderatorService;
	private ReportService reportService;
	private SpeakerService speakerService;
	private EventService eventService;

	@Autowired
	public SpeakerController(ModeratorService moderatorService, ReportService reportService, SpeakerService speakerService, EventService eventService) {
		this.reportService = reportService;
		this.moderatorService = moderatorService;
		this.speakerService = speakerService;
		this.eventService = eventService;
	}

	@GetMapping
	public String getSpeakerCabinet(Model model, Principal principal) {
		Speaker speaker = Speaker.builder().localName(principal.getName()).build();
		model.addAttribute("speaker", speaker);
		model.addAttribute("reports", speakerService.getReports(principal.getName()));
		return "speaker_cabinet";
	}

	@PostMapping(path = "/reports", params = "action=accept")
	public String acceptReports(Speaker speaker) {
		reportService.setAcceptedFor(speaker.getReports());
		return "redirect:/speaker";
	}

	@PostMapping(path = "/reports", params = "action=refuse")
	public String refuseReports(Speaker speaker) {
		reportService.clearSpeaker(speaker.getReports());
		return "redirect:/speaker";
	}

	@GetMapping("/pastevents")
	public String getPastEvents(Model model, Principal principal) {
		model.addAttribute("events", eventService.findPastBySpeakerName(principal.getName()));
		return "past_events";
	}

	@GetMapping("/create")
	public String createReport(Model model, Principal principal) {
		ReportDto reportDto = ReportDto.builder().creator(principal.getName()).build();
		model.addAttribute("reportDto", reportDto);
		return "speaker_create";
	}

	@PostMapping(path = "/create")
	public String createReport(Model model, @Valid ReportDto reportDto, Errors errors, Principal principal) {
		if (errors.hasErrors()) {
			model.addAttribute("reportDto", reportDto);
			return "speaker_create";
		}
		speakerService.createReport(reportDto.toReport(), principal.getName());
		return "redirect:/speaker";
	}
}
