package com.iportalen.timestack.service.template.templates.sms;

import java.io.File;
import java.util.HashMap;

public class PhoneVerificationTemplate extends SMSTemplate {
	
	private static final String PATH = "verificationCode" + File.separator + "verificationCode.ftl";
	private static final String CODE_ARG = "code";

	public PhoneVerificationTemplate(String code) {
		super(PATH);
		this.dataModel = new HashMap<String, String>();
		dataModel.put(CODE_ARG, code);
	}
	
}
