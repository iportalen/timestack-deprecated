package com.iportalen.timestack.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.iportalen.timestack.AtworkApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtworkApplication.class)
@ActiveProfiles("test")
public class WebSecurityConfigTests {

	@Autowired
	private WebApplicationContext wac;

	public MockMvc mockMvc;

	@Before
	public void setup() {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.dispatchOptions(true);
		this.mockMvc = builder.build();
	}

	@Test
	public void corsRejectEvilOrigin() throws Exception {
		mockMvc.perform(
			post("/api/firebase/phonenumber/token")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"phonenumber\":\"+4587654321\"}")
				.header("Access-Control-Request-Method", "POST")
				.header("Origin", "https://evil.com"))
			.andExpect(status().isForbidden());
	}

	@Test
	public void corsAcceptWhitelistedOrigin() throws Exception {
		mockMvc.perform(
			post("/api/firebase/phonenumber/token")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"phonenumber\":\"+4587654321\"}")
				.header("Access-Control-Request-Method", "POST")
				.header("Origin", "http://localhost:8100"))
//			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void rejectUnauthorizedAccess() throws Exception {
		mockMvc.perform(
			get("/api/client/users/me")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Access-Control-Request-Method", "GET")
				.header("Origin", "http://localhost:8080"))
			.andExpect(status().isUnauthorized());
	}

}
