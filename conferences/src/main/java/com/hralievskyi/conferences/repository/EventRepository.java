package com.hralievskyi.conferences.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hralievskyi.conferences.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	Event findByName(String name);

	@Query(value = "SELECT * FROM event WHERE date > CURDATE()", nativeQuery = true)
	List<Event> findFuture();
}
