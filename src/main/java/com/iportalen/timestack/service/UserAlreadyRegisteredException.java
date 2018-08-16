package com.iportalen.timestack.service;

public class UserAlreadyRegisteredException extends RuntimeException {
	private static final long serialVersionUID = 4153701047566201664L;

	public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}