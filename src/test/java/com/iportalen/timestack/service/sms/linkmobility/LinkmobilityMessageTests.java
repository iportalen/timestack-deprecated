package com.iportalen.timestack.service.sms.linkmobility;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iportalen.timestack.TimestackApplication;
import com.iportalen.timestack.service.sms.linkmobility.LinkmobilityMessage;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimestackApplication.class)
@ActiveProfiles("test")
public class LinkmobilityMessageTests {
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	public void messageSerialization() throws JsonProcessingException {
		LinkmobilityMessage message = LinkmobilityMessage.builder().recipients(new String[]{"+4587654321"}).message("testmessage").sender("sender").build();
		assertEquals("{\"message\":{\"recipients\":\"+4587654321\",\"sender\":\"sender\",\"message\":\"testmessage\"}}", objectMapper.writeValueAsString(message));
	}

}
