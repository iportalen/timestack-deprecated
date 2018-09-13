package com.iportalen.timestack.service.template.templates.sms;

public class SMSTemplates {
	
	public static SMSTemplate PhoneVerificationTemplate(String code) {
		return new PhoneVerificationTemplate(code);
	}

}
