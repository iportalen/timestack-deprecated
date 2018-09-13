package com.iportalen.timestack.service.sms;

public interface SmsService {
	void sendMessage(SMS sms);
	
	void start();

	void stop();
}
