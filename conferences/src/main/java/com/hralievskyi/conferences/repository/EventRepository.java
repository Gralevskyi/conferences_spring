package com.hralievskyi.conferences.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hralievskyi.conferences.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	Event findByName(String name);
}
