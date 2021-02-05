package com.hralievskyi.conferences.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hralievskyi.conferences.entity.Report;

public interface ReportRepository extends JpaRepository<Report, String> {
	Report findByTopic(String topic);

	@Query(value = "SELECT t.* FROM reports t INNER JOIN event_reports et on t.topic = et.report_topic WHERE et.event_id = ?1", nativeQuery = true)
	Iterable<Report> findByEventid(long id);

	@Query(value = "SELECT t.* FROM reports t where t.speaker is null", nativeQuery = true)
	Iterable<Report> findAllFreeSpeakerReports();

	@Query(value = "SELECT t.* FROM reports t LEFT JOIN event_reports et on t.topic = et.report_topic WHERE et.report_topic is null", nativeQuery = true)
	Iterable<Report> findAllFreeEventReports();
}
