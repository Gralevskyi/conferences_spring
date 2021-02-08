package com.hralievskyi.conferences.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hralievskyi.conferences.entity.Report;
import com.hralievskyi.conferences.entity.user.Speaker;
import com.hralievskyi.conferences.repository.SpeakerRepository;

@Service
public class SpeakerService {
	private SpeakerRepository speakerRepo;

	@Autowired
	public SpeakerService(SpeakerRepository speakerRepo) {
		this.speakerRepo = speakerRepo;
	}

	public Iterable<Speaker> getAll() {
		return Optional.ofNullable(speakerRepo.findAll()).orElse(new ArrayList<Speaker>());
	}

	public Iterable<Speaker> getAllDto() {
		Iterable<Speaker> speakers = Optional.ofNullable(speakerRepo.findAll()).orElse(new ArrayList<Speaker>());
		speakers.forEach(speaker -> {
			if (speaker.getReports() != null)
				speaker.setReports(null);
		});
		return speakers;
	}

	@Transactional
	public Speaker addNewReports(Speaker reportsContainer) {
		Speaker speaker = speakerRepo.findByName(reportsContainer.getName());
		speaker.addReports(reportsContainer.getReports());
		speakerRepo.save(speaker);
		return speaker;
	}

	public Iterable<Report> getReports(String speakername) {
		Speaker speaker = speakerRepo.findByName(speakername);
		return Optional.ofNullable(speaker.getReports()).orElse(new HashSet<Report>());
	}
}
