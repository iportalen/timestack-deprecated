package com.iportalen.timestack.service.verification.sms;

public class SMSVerificationCreationException extends Exception {
	private static final long serialVersionUID = 6008176655269875589L;

	public SMSVerificationCreationException(String message) {
		super(message);
	}
	
	public SMSVerificationCreationException(Throwable cause) {
		super(cause);
	}
	
	public SMSVerificationCreationException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
