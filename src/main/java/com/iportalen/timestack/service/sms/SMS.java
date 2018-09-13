package com.iportalen.timestack.service.sms;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SMS {

	private String text;
	private String[] recipients;
	
}
