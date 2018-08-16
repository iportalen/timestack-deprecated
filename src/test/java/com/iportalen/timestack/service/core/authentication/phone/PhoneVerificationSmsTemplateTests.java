package com.iportalen.timestack.service.core.authentication.phone;

import static com.iportalen.timestack.service.sms.SmsTemplateConstants.PHONENUMBER_VERIFICATION_SMS_ARG_CODE;
import static com.iportalen.timestack.service.sms.SmsTemplateConstants.PHONENUMBER_VERIFICATION_SMS_PATH;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.iportalen.timestack.AtworkApplication;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtworkApplication.class)
@ActiveProfiles("test")
public class PhoneVerificationSmsTemplateTests {

	private static final String SMS_VERIFICATION_CODE = "123456";

	private static Map<String, String> inputs;
	private StringWriter stringWriter;

	@Autowired
	protected Configuration freemarkerConfiguration;

	@BeforeClass
	public static void setUp() {
		inputs = new HashMap<String, String>();
		inputs.put(PHONENUMBER_VERIFICATION_SMS_ARG_CODE, SMS_VERIFICATION_CODE);
	}

	@Before
	public void beforeEach() {
		this.stringWriter = new StringWriter();
	}

	@Test
	public void danishVerificationSms()
			throws ExecutionException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {

		String expectedMessage = new StringBuilder()
				.append(SMS_VERIFICATION_CODE)
				.append(" er din verificeringskode til AtWork")
				.toString();

		freemarkerConfiguration.getTemplate(PHONENUMBER_VERIFICATION_SMS_PATH, new Locale("da_DK"))
				.process(inputs, stringWriter);
		assertEquals(expectedMessage, stringWriter.toString());
	}

	@Test
	public void englishVerificationSms()
			throws ExecutionException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {

		String expectedMessage = new StringBuilder()
				.append(SMS_VERIFICATION_CODE)
				.append(" is your verification code for AtWork")
				.toString();

		freemarkerConfiguration.getTemplate(PHONENUMBER_VERIFICATION_SMS_PATH, Locale.US)
				.process(inputs, stringWriter);
		assertEquals(expectedMessage, stringWriter.toString());

	}

}
