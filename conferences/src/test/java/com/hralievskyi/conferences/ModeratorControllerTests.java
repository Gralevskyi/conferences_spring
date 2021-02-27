package com.hralievskyi.conferences;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.hralievskyi.conferences.dto.EventCreateDto;
import com.hralievskyi.conferences.entity.Event;
import com.hralievskyi.conferences.repository.EventRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConferencesApplication.class)
@AutoConfigureMockMvc
public class ModeratorControllerTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	WebApplicationContext wac;

	@Autowired
	private EventRepository eventRepository;

	@Before
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void getCreateEvent_thenReturnView() throws Exception {
		mvc.perform(get("/moderator/event/create"))
				.andExpect(status().isOk())
				.andExpect(view().name("event_create"))
				.andExpect(model().attributeExists("eventDto"));
	}

	@Test
	public void postCreateEvent_thenStatusRedirect() throws Exception {
		EventCreateDto event = EventCreateDto.builder()
				.nameEn("test name En").nameUk("test name Uk")
				.placeEn("test place En").placeUk("test place Uk")
				.date(LocalDate.now().plusDays(1L)).time(LocalTime.of(10, 00)).build();
		mvc.perform(post("/moderator/event/create")
				.param("nameEn", event.getNameEn())
				.param("nameUk", event.getNameUk())
				.param("placeUk", event.getPlaceUk())
				.param("placeEn", event.getPlaceEn())
				.param("date", event.getDate().toString())
				.param("time", event.getTime().toString()))
				.andExpect(status().is3xxRedirection());

		Event foundEvent = eventRepository.findByName(event.getNameEn());
		assertNotNull(foundEvent);
		eventRepository.delete(foundEvent);
		Optional<Event> afterDelete = eventRepository.findById(foundEvent.getId());
		assertFalse(afterDelete.isPresent());
	}

	@Test
	public void postWithErrorsToCreateEvent_thenReturnForm() throws Exception {
		EventCreateDto event = EventCreateDto.builder()
				.nameEn("t") // validation error too short name
				.nameUk("test name Uk")
				.placeEn("test place En").placeUk("test place Uk")
				.date(LocalDate.now().plusDays(1L)).time(LocalTime.of(10, 00)).build();
		mvc.perform(post("/moderator/event/create")
				.param("nameEn", event.getNameEn())
				.param("nameUk", event.getNameUk())
				.param("placeUk", event.getPlaceUk())
				.param("placeEn", event.getPlaceEn())
				.param("date", event.getDate().toString())
				.param("time", event.getTime().toString()))
				.andExpect(view().name("event_create"))
				.andExpect(model().attributeExists("eventDto"));
	}

}
