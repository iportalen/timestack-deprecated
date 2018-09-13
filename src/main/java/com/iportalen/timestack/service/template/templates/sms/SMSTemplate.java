package com.iportalen.timestack.service.template.templates.sms;

import java.io.File;

import com.iportalen.timestack.service.template.templates.Template;

public abstract class SMSTemplate extends Template {
	private static final String SMS_PATH = "sms" + File.separator;

	public SMSTemplate(String path) {
		super(SMS_PATH + path);
	}

}
