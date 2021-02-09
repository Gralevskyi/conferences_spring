package com.hralievskyi.conferences.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hralievskyi.conferences.dto.ReportDto;
import com.hralievskyi.conferences.service.ReportService;
import com.hralievskyi.conferences.service.SpeakerService;

@RestController
@RequestMapping(path = "/report", produces = "application/json")
@CrossOrigin(origins = "*")
public class ReportController {

	private SpeakerService speakerService;
	private ReportService reportService;

	@Autowired
	public ReportController(SpeakerService speakerService, ReportService reportService) {
		this.speakerService = speakerService;
		this.reportService = reportService;
	}

	@GetMapping(path = "/speakers", consumes = "application/json")
	public ResponseEntity<Object> getSpeakers() {
		return new ResponseEntity<>(speakerService.getAllProxy(), HttpStatus.OK);
	}

	@PostMapping(path = "/update/{report}", consumes = "application/json")
	public ResponseEntity<Object> updateReport(@RequestBody ReportDto reportDto) {
		try {
			reportService.setSpeakerAccepted(reportDto.toReport(reportDto.getSpeakerId()));
			return new ResponseEntity<>("OK", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
