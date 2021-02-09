package com.hralievskyi.conferences.controller;

import java.security.Principal;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hralievskyi.conferences.entity.Event;
import com.hralievskyi.conferences.service.EventService;
import com.hralievskyi.conferences.util.AlreadySubscribed;
import com.hralievskyi.conferences.util.SortByToLocal;

@Controller
@RequestMapping("/")
public class EventController {
	private EventService eventService;

	@Autowired
	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping
	public String listEvents(Model model, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "4") Integer size, @RequestParam(defaultValue = "date") String sortBy) {
		Page<Event> eventPage = eventService.findFuturePaginated(PageRequest.of(page - 1, size, Sort.by(SortByToLocal.convert(sortBy))));
		eventPage.getContent().forEach(event -> event.setLocales());
		model.addAttribute("eventPage", eventPage);
		model.addAttribute("pageNumbers", IntStream.rangeClosed(1, eventPage.getTotalPages()).boxed().collect(Collectors.toList()));
		return "index";
	}

	@GetMapping("/eventdetails/{id}")
	public String getEventDetails(Model model, @PathVariable(value = "id") long eventid, Principal principal) {
		model.addAttribute("errorMessage", "");
		model.addAttribute("alreadySubsribed", false);
		model.addAttribute("event", eventService.findByIdWithTopics(eventid));
		return "event_details";
	}

	@PostMapping("/subscribe/{id}")
	public String subscribeToEvent(Model model, @PathVariable(value = "id") long eventid, Principal principal) {
		try {
			eventService.subscribeUser(principal.getName(), eventid);
		} catch (AlreadySubscribed ex) {
			model.addAttribute("errorMessage", ex.getMessage());
			model.addAttribute("alreadySubsribed", true);
			model.addAttribute("event", eventService.findByIdWithTopics(eventid));
			return "event_details";
		}
		return "redirect:/";
	}

	@GetMapping("/user")
	public String getPastEvents(Model model, Principal principal) {
		model.addAttribute("events", eventService.findByUsername(principal.getName()));
		return "user";
	}

}
