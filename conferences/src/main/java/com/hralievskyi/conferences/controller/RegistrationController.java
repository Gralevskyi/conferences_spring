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

/*handle registration of new user*/

@Controller
@RequestMapping("/register")
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
			model.addAttribute("registrationForm", registrationForm);
			return "registration";
		}
		userService.save(registrationForm.toUser());
		return "redirect:/login";
	}

}
