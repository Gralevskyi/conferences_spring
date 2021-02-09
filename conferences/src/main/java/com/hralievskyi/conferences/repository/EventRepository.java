package com.hralievskyi.conferences.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hralievskyi.conferences.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

	@Query(value = "SELECT * FROM event WHERE name_en = ?1 OR name_uk = ?1", nativeQuery = true)
	Event findByName(String name);

	@Query(value = "SELECT * FROM event WHERE date > CURDATE()", nativeQuery = true)
	Page<Event> findFuture(Pageable pageable);

	@Query(value = "SELECT * FROM event WHERE date <= CURDATE()", nativeQuery = true)
	List<Event> findPast();

	@Query(value = "SELECT distinct e.* FROM event e INNER JOIN event_reports er ON e.id = er.event_id INNER JOIN reports r ON r.id = er.report_id INNER JOIN speaker s ON s.id = r.speaker INNER JOIN users u on u.id = s.id AND u.username = ?1 WHERE e.date <= CURDATE()", nativeQuery = true)
	List<Event> findPastBySpeakerName(String speakername);

	@Query(value = "SELECT distinct e.* FROM event e INNER JOIN user_events ue ON e.id = ue.event_id INNER JOIN users u ON u.id = ue.user_id WHERE u.username = ?1", nativeQuery = true)
	List<Event> findByUsername(String username);

	@Transactional
	@Modifying
	@Query(value = "UPDATE event r SET r.subscr_users = r.subscr_users + 1 WHERE r.id = ?1", nativeQuery = true)
	void increseSubscrUsers(long eventid);

}
