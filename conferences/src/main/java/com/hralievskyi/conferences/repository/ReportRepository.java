package com.hralievskyi.conferences.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hralievskyi.conferences.entity.Report;

public interface ReportRepository extends JpaRepository<Report, String> {
	Report findByTopic(String topic);

	@Query(value = "SELECT t.* FROM reports t INNER JOIN event_reports et on t.topic = et.report_topic WHERE et.event_id = ?1", nativeQuery = true)
	Iterable<Report> findByEventid(long id);

	@Query(value = "SELECT t.* FROM reports t where t.speaker is null", nativeQuery = true)
	Iterable<Report> findFreeOfSpeaker();

	@Query(value = "SELECT t.* FROM reports t LEFT JOIN event_reports et on t.topic = et.report_topic WHERE et.report_topic is null", nativeQuery = true)
	Iterable<Report> findFreeOfEvent();

	@Query(value = "SELECT t.* FROM reports t LEFT JOIN event_reports et on t.topic = et.report_topic WHERE et.report_topic is null AND t.speaker is not null and t.accepted = 1", nativeQuery = true)
	Iterable<Report> findAcceptedFreeOfEvent();

	@Transactional
	@Modifying
	@Query(value = "UPDATE reports r SET r.accepted = 1 WHERE r.topic IN (:topics)", nativeQuery = true)
	void setAccepted(@Param("topics") List<String> topics);
}
