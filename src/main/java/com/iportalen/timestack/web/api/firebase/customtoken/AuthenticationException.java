package com.iportalen.timestack.web.api.firebase.customtoken;

public class AuthenticationException extends RuntimeException {
	private static final long serialVersionUID = 4153701047566201664L;

	public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}