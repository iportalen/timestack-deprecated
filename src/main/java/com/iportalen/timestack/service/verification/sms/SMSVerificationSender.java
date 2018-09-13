package com.iportalen.timestack.service.verification.sms;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iportalen.timestack.service.sms.SMS;
import com.iportalen.timestack.service.sms.SmsService;
import com.iportalen.timestack.service.template.TemplateProcessingService;
import com.iportalen.timestack.service.template.templates.sms.PhoneVerificationTemplate;
import com.iportalen.timestack.service.template.templates.sms.SMSTemplate;

@Service
public class SMSVerificationSender {
	
	@Autowired
    private SmsService smsService;
	
	@Autowired
	TemplateProcessingService templateService;
	
	public void send(String phonenumber, String code, Locale locale) {
		SMS message = createVerificationSMS(phonenumber, code, locale);
		smsService.sendMessage(message);
	}

	private SMS createVerificationSMS(String phonenumber, String code, Locale locale) {
		SMSTemplate template = new PhoneVerificationTemplate(code);
		String text = templateService.process(template, locale);
		return new SMS(text, new String[] { phonenumber });
	}

}
