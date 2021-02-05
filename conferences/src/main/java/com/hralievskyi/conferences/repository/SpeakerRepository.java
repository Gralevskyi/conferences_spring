package com.hralievskyi.conferences.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hralievskyi.conferences.entity.user.Speaker;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
	Speaker findByName(String name);
}
