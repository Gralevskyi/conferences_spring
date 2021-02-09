package com.hralievskyi.conferences.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hralievskyi.conferences.entity.user.Speaker;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
	@Query(value = "SELECT t.* FROM speaker t WHERE t.name_en = ?1 OR t.name_uk = ?1", nativeQuery = true)
	Speaker findByName(String name);

	@Query(value = "SELECT t.* FROM speaker t JOIN users u ON t.id = u.id WHERE u.username = ?1", nativeQuery = true)
	Speaker findByUserName(String username);
}
