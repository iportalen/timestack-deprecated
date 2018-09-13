package com.iportalen.timestack.service.sms;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DevelopmentSmsService implements SmsService {
	
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
