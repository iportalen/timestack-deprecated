package com.iportalen.timestack;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtworkApplication.class)
@ActiveProfiles("test")
public class AtworkApplicationTests {
	
	@Autowired private ApplicationContext applicationContext;

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void firebaseDisabledWithTestProfile() {
		boolean enabled = applicationContext.getEnvironment().getProperty("firebase.enabled", Boolean.class);
		assertTrue(enabled == false);
	}

}
