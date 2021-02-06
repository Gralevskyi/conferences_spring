package com.hralievskyi.conferences.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hralievskyi.conferences.entity.Event;
import com.hralievskyi.conferences.service.EventService;

@Controller
@RequestMapping("/")
public class EventController {
	private EventService eventService;

	@Autowired
	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping
	public String getMainpage(Model model) {
		model.addAttribute("events", eventService.findFuture());
		return "index";
	}

	@GetMapping("/eventdetails/{id}")
	public String getEventDetails(Model model, @PathVariable(value = "id") long eventid) {
		Optional<Event> event = eventService.findByIdWithTopics(eventid);
		if (event.isPresent()) {
			model.addAttribute("event", event.get());
		}
		return "eventdetails";
	}

	@PostMapping("/subscribe/{id}")
	public String subscribeToEvent(Model model, @PathVariable(value = "id") long eventid, Principal principal) {
		eventService.subscribeUser(principal.getName(), eventid);
		return "redirect:/";
	}

}
