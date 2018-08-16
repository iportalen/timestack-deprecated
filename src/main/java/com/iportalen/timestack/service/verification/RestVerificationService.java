package com.iportalen.timestack.service.verification;

import org.springframework.stereotype.Service;

import com.iportalen.timestack.domain.model.User;

@Service("restVerificationService")
public class RestVerificationService implements VerificationService {

	@Override
	public User verifyEmail(String verificationToken) throws VerificationTokenExpiredException {
		return null;
	}

	@Override
	public User verifyPhonenumber(String verificationToken, String verificationCode)
			throws VerificationTokenExpiredException {
		return null;
	}

}
