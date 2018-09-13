package com.iportalen.timestack.web.api.firebase.customtoken.phone;

import org.springframework.http.HttpStatus;

public class TokenRequestException extends RuntimeException {
	private static final long serialVersionUID = -3277212146308109719L;
	
	private HttpStatus httpStatus;
	
	public TokenRequestException(HttpStatus httpStatus, String message) {
        super(message);
    }
	
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

}
