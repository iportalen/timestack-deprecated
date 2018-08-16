package com.iportalen.timestack.model.user;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.iportalen.timestack.TimestackApplication;
import com.iportalen.timestack.domain.dao.UserRepository;
import com.iportalen.timestack.domain.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimestackApplication.class)
@ActiveProfiles("test")
public class UserRepositoryTests {
	private final String USER_EMAIL = "a@b.c";
	private final String USER_PHONENUMBER = "+4587654321";

	@Autowired
	UserRepository userRepository;

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
		userRepository.deleteAll();
	}

	@Test
	public void saveUserTest() {
		User user = userRepository.save(User.builder()
				.email(USER_EMAIL)
				.username(USER_PHONENUMBER)
				.phonenumber(USER_PHONENUMBER)
				.build());
		User foundUser = userRepository.findById(user.getId())
				.get();
		assertNotNull(foundUser);
	}

}