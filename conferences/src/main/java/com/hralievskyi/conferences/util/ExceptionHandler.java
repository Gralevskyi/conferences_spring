package com.hralievskyi.conferences.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandler {

	public ModelAndView handleError(HttpServletRequest sr, Exception ex) {
		ModelAndView model = new ModelAndView();
		model.addObject("message", ex.getMessage());
		model.setViewName("error");
		return model;
	}

}
