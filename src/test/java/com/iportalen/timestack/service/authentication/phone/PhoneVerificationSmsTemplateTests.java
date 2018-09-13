package com.iportalen.timestack.service.authentication.phone;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.iportalen.timestack.TimestackApplication;
import com.iportalen.timestack.service.template.freemarker.FreemarkerTemplateProcessingService;
import com.iportalen.timestack.service.template.templates.sms.PhoneVerificationTemplate;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimestackApplication.class)
@ActiveProfiles("test")
public class PhoneVerificationSmsTemplateTests {

	private static final String SMS_VERIFICATION_CODE = "123456";

	@Autowired
	protected FreemarkerTemplateProcessingService freemarkerTemplateService;

	@Test
	public void danishVerificationSms()
			throws ExecutionException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		String expectedMessage = new StringBuilder()
				.append(SMS_VERIFICATION_CODE)
				.append(" er din verificeringskode til Timestack")
				.toString();
		
		PhoneVerificationTemplate template = new PhoneVerificationTemplate(SMS_VERIFICATION_CODE);
		assertEquals(expectedMessage, this.freemarkerTemplateService.process(template, Locale.forLanguageTag("da-DK")));
	}

	@Test
	public void englishVerificationSms()
			throws ExecutionException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {

		String expectedMessage = new StringBuilder()
				.append(SMS_VERIFICATION_CODE)
				.append(" is your verification code for Timestack")
				.toString();

		PhoneVerificationTemplate template = new PhoneVerificationTemplate(SMS_VERIFICATION_CODE);
		assertEquals(expectedMessage, this.freemarkerTemplateService.process(template, Locale.US));
	}
	
}
