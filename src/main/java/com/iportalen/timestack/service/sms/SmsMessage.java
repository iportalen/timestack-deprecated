package com.iportalen.timestack.service.sms;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SmsMessage {
	
	private String text;
	private String template;
	private String[] recipients;
	private Map<String, String> dataModel;

}
