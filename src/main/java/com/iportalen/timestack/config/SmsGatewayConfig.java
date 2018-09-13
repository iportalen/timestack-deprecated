package com.iportalen.timestack.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.iportalen.timestack.service.sms.DevelopmentSmsService;
import com.iportalen.timestack.service.sms.SmsService;
import com.iportalen.timestack.service.sms.linkmobility.LinkmobilitySmsService;

@Configuration
public class SmsGatewayConfig {

	@Bean
	@Profile("prod")
	SmsService productionSmsService(@Value("${sms.gateway.linkmobility.apikey}") String apiKey,
			@Value("${sms.gateway.linkmobility.uri.send}") String sendUri,
			@Value("${sms.gateway.linkmobility.sender:TimeStack}") String sender) {
		return new LinkmobilitySmsService(apiKey, sendUri, sender);
	}

	@Bean
	@Profile("test")
	SmsService testSmsService(@Value("${sms.gateway.linkmobility.uri.send}") String sendUri,
			@Value("${sms.gateway.linkmobility.sender}") String sender) {
		return new LinkmobilitySmsService("dummy", sendUri, sender);
	}

	@Bean
	@Profile("dev")
	SmsService devSmsService() {
		return new DevelopmentSmsService();
	}

}
