package com.iportalen.timestack.service.template.freemarker;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.iportalen.timestack.TimestackApplication;
import com.iportalen.timestack.service.template.TemplateProcessingException;
import com.iportalen.timestack.service.template.templates.Template;
import com.iportalen.timestack.service.template.templates.sms.PhoneVerificationTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimestackApplication.class)
@ActiveProfiles("test")
public class FreemarkerTemplateServiceTests {

	@Autowired
	FreemarkerTemplateProcessingService freemarkerService;

	@Test(expected = TemplateProcessingException.class)
	public void noSuchSMSTemplate() {
		Template template = new Template("gibberish");
		this.freemarkerService.process(template);
	}
	
	@Test
	public void ShouldBeThere() {
		PhoneVerificationTemplate template = new PhoneVerificationTemplate("123456");
		assertNotNull(this.freemarkerService.process(template));
	}

}
