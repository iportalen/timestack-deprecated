package com.iportalen.timestack.service.verification.sms;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Data;

@Data
public class SMSVerification {
	
	private String code;
	private String token;
	
	public SMSVerification() {
		this.code = generateCode();
		this.token = generateToken();
	}
	
	private static String generateCode() {
	    int rand = 100000 + new Random().nextInt(900000);
	    return String.valueOf(rand);
	}
	
	private static String generateToken() {
		RandomStringUtils.randomAlphanumeric(64, 128);
		return UUID.randomUUID().toString() + UUID.randomUUID().toString();
	}

}
