package com.iportalen.timestack.service.core.sms.linkmobility;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.iportalen.timestack.AtworkApplication;
import com.iportalen.timestack.service.sms.MalformedSmsMessageException;
import com.iportalen.timestack.service.sms.SmsMessage;
import com.iportalen.timestack.service.sms.SmsService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtworkApplication.class)
@ActiveProfiles("test")
public class LinkmobilitySmsServiceTests {
	
	
	@Autowired
	SmsService smsService;

	@BeforeClass
	public static void setUp() {
	}

	@Before
	public void beforeEach() {
	}

	@Test
	public void queueTest() throws InterruptedException, MalformedSmsMessageException {
		smsService.sendMessage(SmsMessage.builder().recipients(new String[] {"+4587654321"}).text("1").build());
		smsService.sendMessage(SmsMessage.builder().recipients(new String[] {"+4587654321"}).text("3").build());
		smsService.sendMessage(SmsMessage.builder().recipients(new String[] {"+4587654321"}).text("4").build());
		smsService.sendMessage(SmsMessage.builder().recipients(new String[] {"+4587654321"}).text("5").build());
		new Thread(() -> {
			try {
				Thread.sleep(2000);
				smsService.sendMessage(SmsMessage.builder().recipients(new String[] {"+4587654321"}).text("2").build());
			} catch (InterruptedException | NullPointerException | MalformedSmsMessageException e) {
			}
			}).start();
		
		Thread.sleep(5000);

	}

}