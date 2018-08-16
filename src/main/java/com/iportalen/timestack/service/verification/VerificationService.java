package com.iportalen.timestack.service.verification;

import com.iportalen.timestack.domain.model.User;

public interface VerificationService {
	
	User verifyEmail(String verificationToken) throws VerificationTokenExpiredException;
	
	User verifyPhonenumber(String verificationToken, String verificationCode) throws VerificationTokenExpiredException;

}
