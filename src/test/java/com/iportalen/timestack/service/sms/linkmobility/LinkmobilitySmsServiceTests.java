package com.iportalen.timestack.service.sms.linkmobility;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.iportalen.timestack.TimestackApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimestackApplication.class)
@ActiveProfiles("test")
public class LinkmobilitySmsServiceTests {

	private static final int SENDER_MIN_LENGTH = 1;
	private static final int SENDER_MAX_LENGTH = 11;
	
	@Autowired
	LinkmobilitySmsService linkmobilitySmsService;
	
	@Test
	public void correctSenderLength() {
		final int senderLength = linkmobilitySmsService.getSender().length();
		assertTrue(senderLength > SENDER_MIN_LENGTH && senderLength < SENDER_MAX_LENGTH);
	}
	
}
