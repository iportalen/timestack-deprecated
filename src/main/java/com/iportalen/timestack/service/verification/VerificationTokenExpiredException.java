package com.iportalen.timestack.service.verification;

public class VerificationTokenExpiredException extends Exception {
	private static final long serialVersionUID = -8842236160844472735L;
	
	public VerificationTokenExpiredException() {
		super("Token expired!");
	}

}
