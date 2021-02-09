package com.hralievskyi.conferences.util;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

public class SortByToLocal {

	public static String convert(String sortBy) {
		if ("name".equals(sortBy) || "place".equals(sortBy)) {
			Locale locales = LocaleContextHolder.getLocale();
			if ("uk".equals(locales.toString())) {
				sortBy = sortBy + "_uk";
			} else {
				sortBy = sortBy + "_en";
			}
		}
		return sortBy;
	}

}
