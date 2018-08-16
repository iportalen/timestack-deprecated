package com.iportalen.timestack.service.sms;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Service
@Profile("!prod")
@Slf4j
public class DevelopmentSmsService implements SmsService {
	
	@Autowired
	protected Configuration freemarkerConfiguration;
	
	public DevelopmentSmsService() {
	}
	
	@Override
	public void sendMessage(SmsMessage message) throws NullPointerException {
		Objects.nonNull(message);
		try {
			StringWriter stringWriter = new StringWriter();
			if(message.getTemplate() != null)
				freemarkerConfiguration.getTemplate(message.getTemplate(), new Locale("da_DK")).process(message.getDataModel(), stringWriter);
			else 
				stringWriter.append(message.getText());
			
			log.info("SMS was sent to phonenumber: " + StringUtils.join(message.getRecipients(), ",") + ". Message: " + stringWriter.toString());
		} catch (TemplateException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		log.info("Started DevelopmentSmsService");
	}

	@Override
	public void stop() {
		log.info("Stopped DevelopmentSmsService");
	}
	
}
