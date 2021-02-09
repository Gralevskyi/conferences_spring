package com.hralievskyi.conferences.entity.user;

import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.springframework.context.i18n.LocaleContextHolder;

import com.hralievskyi.conferences.entity.Report;
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
@Table(name = "speaker")
public class Speaker {

	@Id
	@Column(unique = true)
	private long id;

	@NotNull
	@Size(min = 2, message = "{speaker.create.name}")
	@Column(name = "name_en", unique = true)
	private String nameEn;

	@NotNull
	@Size(min = 2, message = "{speaker.create.name}")
	@Column(name = "name_uk", unique = true)
	private String nameUk;

	@OneToMany(mappedBy = "speaker", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	Set<Report> reports;

	@Transient
	private String localName;

	public Set<Report> addReports(Set<Report> newReports) {
		this.reports.addAll(newReports);
		return this.reports;
	}

	public String getLocalName() {
		Locale locales = LocaleContextHolder.getLocale();
		if ("uk".equals(locales.toString())) {
			return nameUk;
		}
		return nameEn;
	}

	public void setLocalName() {
		Locale locales = LocaleContextHolder.getLocale();
		if ("uk".equals(locales.toString())) {
			localName = nameUk;
		} else {
			localName = nameEn;
		}
	}
}
