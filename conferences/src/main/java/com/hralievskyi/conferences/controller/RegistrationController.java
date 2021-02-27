package com.hralievskyi.conferences.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hralievskyi.conferences.dto.RegistrationForm;
import com.hralievskyi.conferences.service.UserService;

import lombok.extern.log4j.Log4j2;

/*handle registration of new user*/

@Controller
@RequestMapping("/register")
@Log4j2
public class RegistrationController {

	@Autowired
	private UserService userService;

	@GetMapping
	public String registerForm(Model model) {
		model.addAttribute("registrationForm", new RegistrationForm());
		return "registration";
	}

	@PostMapping
	public String processRegistration(Model model, @Valid RegistrationForm registrationForm, Errors errors) {
		if (errors.hasErrors()) {
			log.info("errors in registration form");
			model.addAttribute("registrationForm", registrationForm);
			return "registration";
		}
		userService.save(registrationForm.toUser());
		return "redirect:/login";
	}

}
