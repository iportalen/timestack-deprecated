package com.iportalen.timestack.service.sms;

public interface SmsService {
	
	/**
	 * @param message
	 * @param recipients
	 */
	void sendMessage(SmsMessage message) throws NullPointerException, MalformedSmsMessageException;
	
	void start();
	
	void stop();

}
