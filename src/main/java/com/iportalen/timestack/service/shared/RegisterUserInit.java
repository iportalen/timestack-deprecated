package com.iportalen.timestack.service.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterUserInit {
	private final String uid;
	private final String phonenumber;
	
}