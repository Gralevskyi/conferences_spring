package com.hralievskyi.conferences.entity;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.springframework.context.i18n.LocaleContextHolder;

import com.hralievskyi.conferences.entity.user.Speaker;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "reports")
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;

	@Column(name = "topic_en", unique = true)
	@NotNull
	@Size(min = 5, max = 50, message = "{report.create.topic}")
	private String topicEn;

	@Column(name = "topic_uk", unique = true)
	@NotNull
	@Size(min = 5, max = 50, message = "{report.create.topic}")
	private String topicUk;

	@NotNull
	private String creator;

	private boolean accepted;

	@ManyToOne
	@JoinColumn(name = "speaker", nullable = true)
	private Speaker speaker;

	@Transient
	private String localTopic;

	public String getLocalTopic() {
		Locale locales = LocaleContextHolder.getLocale();
		if ("uk".equals(locales.toString())) {
			return topicUk;
		}
		return topicEn;
	}

	public void setLocalTopic() {
		Locale locales = LocaleContextHolder.getLocale();
		if ("uk".equals(locales.toString())) {
			localTopic = topicUk;
		}
		localTopic = topicEn;
	}

}
