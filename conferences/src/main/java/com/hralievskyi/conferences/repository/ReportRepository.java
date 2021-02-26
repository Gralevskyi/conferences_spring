package com.hralievskyi.conferences.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hralievskyi.conferences.entity.Report;

public interface ReportRepository extends JpaRepository<Report, String> {

	@Query(value = "SELECT t.* FROM reports t WHERE t.topic_en = ?1 OR t.topic_uk = ?1", nativeQuery = true)
	Report findByTopic(String topic);

	@Query(value = "SELECT t.* FROM reports t INNER JOIN event_reports et on t.id = et.report_id WHERE et.event_id = ?1", nativeQuery = true)
	Iterable<Report> findByEventid(long id);

	@Query(value = "SELECT t.* FROM reports t WHERE t.speaker is null", nativeQuery = true)
	Iterable<Report> findFreeOfSpeaker();

	@Query(value = "SELECT t.* FROM reports t LEFT JOIN event_reports et on t.id = et.report_id WHERE et.report_id is null", nativeQuery = true)
	Iterable<Report> findFreeOfEvent();

	@Query(value = "SELECT t.* FROM reports t LEFT JOIN event_reports et on t.id = et.report_id WHERE et.report_id is null AND t.speaker is not null and t.accepted = 1", nativeQuery = true)
	List<Report> findAcceptedFreeOfEvent();

	@Transactional
	@Modifying
	@Query(value = "UPDATE reports r SET r.accepted = 1 WHERE r.id IN (:reportId)", nativeQuery = true)
	void setAccepted(@Param("reportId") List<Long> reportId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE reports r SET r.speaker = NULL WHERE r.id IN (:reportId)", nativeQuery = true)
	void setSpeakerToNull(@Param("reportId") List<Long> reportId);

	@Modifying
	@Query(value = "UPDATE reports r SET r.accepted = ?1 WHERE r.topic_en = ?2 OR r.topic_uk = ?2", nativeQuery = true)
	void setAccepted(boolean accepted, String localTopic);

	@Modifying
	@Query(value = "UPDATE reports r SET r.speaker = ?1 WHERE r.topic_en = ?2 OR r.topic_uk = ?2", nativeQuery = true)
	void setSpeaker(long speakerId, String localTopic);

	@Modifying
	@Query(value = "UPDATE reports r SET r.speaker = NULL WHERE r.topic_en = ?1 OR r.topic_uk = ?1", nativeQuery = true)
	void setSpeakerToNull(String localTopic);
}
