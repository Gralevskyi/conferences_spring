package com.hralievskyi.conferences.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	/*
	 * @GetMapping public String getMainpage(Model model) {
	 * model.addAttribute("events", eventService.findFuture()); return "index"; }
	 */

	@GetMapping
	public String listEvents(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(4);

		Page<Event> eventPage = eventService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

		model.addAttribute("eventPage", eventPage);

		int totalPages = eventPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

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
