package com.iportalen.timestack.service.sms;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
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
	public void sendMessage(SMS message) {
		Objects.nonNull(message);
		log.info("SMS was sent to phonenumber: " + StringUtils.join(message.getRecipients(), ",") + ". Message: " + message.getText());
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
