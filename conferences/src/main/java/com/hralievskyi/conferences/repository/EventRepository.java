package com.hralievskyi.conferences.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hralievskyi.conferences.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	Event findByName(String name);

	@Query(value = "SELECT * FROM event WHERE date <= CURDATE()", nativeQuery = true)
	List<Event> findPast();

	@Query(value = "SELECT distinct e.* FROM event e INNER JOIN event_reports er ON e.id = er.event_id INNER JOIN reports r ON r.topic = er.report_topic INNER JOIN speaker s ON s.id = r.speaker AND s.name = ?1 WHERE e.date <= CURDATE()", nativeQuery = true)
	List<Event> findPastBySpeakerName(String speakername);

	@Query(value = "SELECT distinct e.* FROM event e INNER JOIN user_events ue ON e.id = ue.event_id INNER JOIN users u ON u.id = ue.user_id WHERE u.username = ?1", nativeQuery = true)
	List<Event> findByUsername(String username);

}
