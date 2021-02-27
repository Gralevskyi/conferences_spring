package com.hralievskyi.conferences;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConferencesApplication.class)
@AutoConfigureMockMvc
public class SecurityTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	WebApplicationContext wac;

	@Before
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
	}

	@Test
	public void getCreateEventAdmin_thenReturnView() throws Exception {
		mvc.perform(get("/moderator/event/create")
				.with(SecurityMockMvcRequestPostProcessors.user("admin@mail.com").roles("MODERATOR")))
				.andExpect(status().isOk())
				.andExpect(view().name("event_create"))
				.andExpect(model().attributeExists("eventDto"));
	}

	@Test
	public void getCreateEventNotAdmin_thenForbidden() throws Exception {
		mvc.perform(post("/moderator/event/create")
				.with(SecurityMockMvcRequestPostProcessors.user("test@user").roles("USER")))
				.andExpect(status().isForbidden());
	}

}
