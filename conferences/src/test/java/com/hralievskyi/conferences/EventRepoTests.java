package com.hralievskyi.conferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hralievskyi.conferences.entity.Event;
import com.hralievskyi.conferences.repository.EventRepository;

import lombok.extern.log4j.Log4j2;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConferencesApplication.class)
@Log4j2
public class EventRepoTests {

	@Autowired
	private EventRepository eventRepository;

	@Test
	public void saveFindByIdDeleteEvent() {
		Event event = Event.builder().nameEn("test name En").nameUk("test name uK")
				.placeEn("test place En").placeUk("test place Uk").date(LocalDate.now())
				.time(LocalTime.now()).build();
		try {
			event = eventRepository.save(event);
		} catch (Exception ex) {
			log.error(ex);
		}
		Event foundEvent = Optional.ofNullable(eventRepository.findById(event.getId()).get()).orElse(Event.builder().build());

		assertNotNull(foundEvent);
		assertEquals(foundEvent.getNameEn(), event.getNameEn());

		eventRepository.delete(event);
		Optional<Event> afterDelete = eventRepository.findById(event.getId());
		assertFalse(afterDelete.isPresent());
	}
}
